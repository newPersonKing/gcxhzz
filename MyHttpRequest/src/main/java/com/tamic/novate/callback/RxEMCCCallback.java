/*
 *    Copyright (C) 2017 Tamic
 *
 *    link :https://github.com/Tamicer/Novate
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.tamic.novate.callback;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.LogWraper;
import com.tamic.novate.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * RxGenericsCallback<T> 泛型回调
 * Created by Tamic on 2016/6/23.
 */
public abstract class RxEMCCCallback<T> extends ResponseCallback<T, ResponseBody> {

    protected T dataResponse = null;
    protected int code = -1;
    protected String msg = "";
    protected String dataStr = "";
    protected boolean success = false;
    
    private Type dataType;
    public RxEMCCCallback() {
        
    }

    @Override
    public T onHandleResponse(ResponseBody response) throws Exception {

        dataType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String jstring = new String(response.bytes());
        return transform(jstring, dataType);
    }

    @Override
    public void onNext(final Object tag, Call call, T response) {

        if (Utils.checkMain()) {
            onNext(tag,success, code, msg, dataResponse);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onNext(tag,success, code, msg, dataResponse);
                }
            });
        }
    }

    public abstract void onNext(Object tag,boolean success, int code, String message, T response);

    public T transform(String response, final Type classOfT) throws ClassCastException {

        if (classOfT == NovateResponse.class) {
            return (T) new Gson().fromJson(response, classOfT);
        }

        JSONObject jsonObject = null;
        LogWraper.e(TAG, response);
        try {
            jsonObject = new JSONObject(response);
            code = jsonObject.optInt("state");
            msg = jsonObject.optString("msg");
            success = jsonObject.optBoolean("success");
            
            if (TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("error");
            }

            if(TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("message");
            }

            dataStr = jsonObject.opt("data").toString();
            if (TextUtils.isEmpty(dataStr)) {
                dataStr = jsonObject.opt("result").toString();
            }


            if(!TextUtils.isEmpty(dataStr)){
                dataResponse = (T) new Gson().fromJson(dataStr, classOfT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataResponse;
    }
}
