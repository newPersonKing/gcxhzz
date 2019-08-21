package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.algorithm.android.widget.dialog.AlAlertDialog;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import constacne.UiType;
import model.UiConfig;
import update.UpdateAppUtils;

public class MySettingActivity extends BaseTitleActivity {

    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
    private String updateTitle = "发现新版本";
    private String updateContent = "1、版本更新\n2、修改了一些bug\n3、增加了一些功能\n4、更多功能等你探索";

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
                checkVersion();
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

    private void checkVersion(){

        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo =  pm.getPackageInfo(getPackageName(),0);
            String versionCode = packageInfo.versionName;
            Map<String,Object> map = new HashMap<>();
            map.put("versionCode",versionCode);
            HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.CHECK_VERSION, map, new Novate.ResponseCallBack<Object>() {
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
                    JSONObject jsonObject = new JSONObject(originalResponse);
                    boolean success = jsonObject.optBoolean("succeeded");
                    if(success){
                        JSONObject content = jsonObject.getJSONObject("content");
                        String appUrl = content.optString("appUrl");
                        updateDialog(appUrl);
                    }
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateDialog(String appUrl){
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUiType(UiType.SIMPLE);
        UpdateAppUtils
                .getInstance()
                .apkUrl(appUrl)
                .updateTitle(updateTitle)
                .uiConfig(uiConfig)
                .updateContent(updateContent)
                .update();
    }

}
