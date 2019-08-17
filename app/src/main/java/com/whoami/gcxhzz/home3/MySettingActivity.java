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
import constacne.UiType;
import model.UiConfig;
import update.UpdateAppUtils;

public class MySettingActivity extends BaseTitleActivity {

    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
    private String updateTitle = "发现新版本";
    private String updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";

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
            R.id.btn_login_out,
            R.id.fv_check_version
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
            case R.id.fv_check_version:
               UiConfig uiConfig = new UiConfig();
               uiConfig.setUiType(UiType.SIMPLE);
                UpdateAppUtils
                        .getInstance()
                        .apkUrl(apkUrl)
                        .updateTitle(updateTitle)
                        .uiConfig(uiConfig)
                        .updateContent(updateContent)
                        .update();
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
