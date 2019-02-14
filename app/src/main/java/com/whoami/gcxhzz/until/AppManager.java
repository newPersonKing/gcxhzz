/*
 * Copyright (c) EMCC 2015 All Rights Reserved
 */
package com.whoami.gcxhzz.until;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;


import java.util.Stack;

/**
 * <p>
 * app管理类。
 * <p>
 * <p>
 * 创建日期 2015年7月15日 下午5:05:53<br>
 *
 * @author 高炎<p>
 * @since 1.0.0
 */
public class AppManager {
    private static final String TAG = AppManager.class.getSimpleName();
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?>... cls) {
        if (null != cls) {
            for (Class<?> c1 : cls) {
                for (int i = 0; i < activityStack.size(); i++) {
                    Activity activity = activityStack.get(i);
                    if (activity.getClass().equals(c1)) {
                        finishActivity(activity);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 判断有没有
     *
     * @param cls
     * @return
     */
    public boolean ifExist(Class<?> cls) {
        if (null == activityStack) {
            return false;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    @SuppressLint("NewApi")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
