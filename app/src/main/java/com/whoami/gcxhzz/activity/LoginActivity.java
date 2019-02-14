package com.whoami.gcxhzz.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.UserInfo;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseTitleActivity {

    @BindView(R.id.iv_login_password_tran)
    ImageView ivLoginPasswordTran;

    @BindView(R.id.et_login_main_username)
    EditText etAccount;
    @BindView(R.id.et_login_password)
    EditText etPassword;

    @Override
    protected int onSetContentView() {
        return R.layout.layout_login;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"登陆",0);

    }
    @OnClick(R.id.btn_login_in)
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.btn_login_in:
                /*点击登陆*/
                if (CheckData()){
                    login();
                }
                break;
        }
    }

    private boolean CheckData() {

        if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void login(){

        HashMap<String,Object> map=new HashMap<>();
        map.put("userName", etAccount.getText().toString());
        map.put("password", etPassword.getText().toString());

        HttpRequestUtils.getInstance().getNovate().executeBody(HttpService.API_ACCOUNT_LOGIN, map, new Novate.ResponseCallBack<Object>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                UserInfo userInfo=BaseUtils.getGson().fromJson(originalResponse, UserInfo.class);
                /*登陆成功走这里*/
                mApplication.saveLoginInfo(userInfo.getContent(),userInfo.getContent().getUsername(),userInfo.getContent().getRealName(),etPassword.getText().toString());
                finish();
            }
        });
    }
}
