package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.view.View;

import com.algorithm.android.widget.dialog.AlAlertDialog;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;

import org.json.JSONException;

import java.util.HashMap;

import butterknife.OnClick;

public class MySettingActivity extends BaseTitleActivity {
    @Override
    protected int onSetContentView() {
        return R.layout.layout_my_setting;
    }
    private AlAlertDialog alAlertDialog;
    @Override
    protected void onInitData() {
        setTitle(R.drawable.back,"设置",0);

        alAlertDialog = new AlAlertDialog(mContext).builder().setMsg("确认退出当前用户吗？").setPositiveButton(getString(R.string.sure), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.exitUserLogin(mContext);
                loginOut();
            }
        }).setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
    @OnClick({
            R.id.fv_copyright,
            R.id.btn_login_out
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fv_copyright:
                Intent intent=new Intent(this,CopyrightWebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login_out:
                alAlertDialog.show();
                break;
        }
    }

    private void loginOut(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("LoginResult",mApplication.getUserInfo());

        HttpRequestUtils.getInstance().getNovate().executePost(HttpService.API_ACCOUNT_LOGOUT, map, new Novate.ResponseCallBack<Object>() {
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

                finish();
            }
        });
    }
}
