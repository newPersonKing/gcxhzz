package com.whoami.gcxhzz.base.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.algorithm.android.widget.item.ForwardView;
import com.whoami.gcxhzz.activity.MainActivity;
import com.whoami.gcxhzz.entity.UserInfo;
import com.whoami.gcxhzz.entity.UserInforData;
import com.whoami.gcxhzz.until.AppManager;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.SPUtils;



public class MyApplication extends Application {

    /**
     * 登录信息
     */
    private static UserInforData loginInfo;

    private static boolean isLogin=false;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);

    }


    public void saveLoginInfo(UserInforData loginInfo, String loginName, String RealName,String password) {
        //SPUtils.getInstance().set(SPUtils.SIGN_IN, true);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_LOGIN_INFO);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_NAME);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_REAL_NAME);

        SPUtils.getInstance().set(SPUtils.SIGN_IN_LOGIN_INFO, BaseUtils.getGson().toJson(loginInfo));
        SPUtils.getInstance().set(SPUtils.SIGN_IN_NAME, loginName);
        SPUtils.getInstance().set(SPUtils.SIGN_IN_REAL_NAME, RealName);
        SPUtils.getInstance().set(SPUtils.SIGN_IN_PASSWORD,password);

        this.isLogin = true;
        this.loginInfo = loginInfo;
    }

    public boolean isLogin(){
        return isLogin;
    }

    public UserInforData getUserInfo(){
        if (loginInfo!=null) return loginInfo;
        return null;
    }

    /**
     * 退出登陆
     */
    public void exitUserLogin(Context mContext) {
        clearLoginInfo();
        AppManager.getAppManager().finishAllActivity();
        mContext.startActivity(new Intent(mContext, MainActivity.class));
    }

    /**
     * 清除登陆信息
     */
    public void clearLoginInfo() {
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_LOGIN_INFO);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_NAME);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_REAL_NAME);

        this.isLogin = false;
        this.loginInfo = null;
    }

}
