package com.whoami.gcxhzz.base.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.app.MyApplication;
import com.whoami.gcxhzz.until.AppManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by algorithm on 2017/10/24.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected static String TAG = "BaseActivity";

    /**
     * 当前应用环境
     */
    public MyApplication mApplication;
    protected Activity mContext;

    protected Unbinder mUnBinder;

    protected ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //设置TAG 和 context
        TAG = this.getClass().getSimpleName();
        mApplication = (MyApplication) getApplication();
        mContext = this;
        AppManager.getAppManager().addActivity(mContext);
        //设置layout布局之前
        beforeSetView();
        //设置layout布局并绑定布局控件
        setContentView(onSetContentView());
        mUnBinder = ButterKnife.bind(this);
        
        //获取Intent
        onGetIntent();
        //初始化
        init();
        //设置事件监听
        setListener();
        //初始化数据
        onInitDataWithsavedInstanceState(savedInstanceState);
        onInitData();
        // 设置Activity禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

   

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void init() {
        mProgressDialog = new ProgressDialog(mContext);
    }


    /**
     * 设置layout之前的操作
     */
    protected void beforeSetView() {
        
    }

    /**
     * 设置layout布局
     * @return
     */
    protected abstract int onSetContentView();
   
    /**
     * 在 onInitData() 处理数据之前 接受Activity间传递的数据
     */
    protected void onGetIntent() {}

    /**
     * 设置监听事件
     */
    protected void setListener() {}
    
    protected abstract void onInitData();

    public  void onInitDataWithsavedInstanceState(Bundle savedInstanceState){

    }
    @Override
    public void onClick(View v) {}




    //==================================常用方法=======================================

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action跳转界面
     **/
    protected void startActivity(String action) {
        startActivity(action, null);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class 返回跳转界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 返回跳转界面
     *
     * @param cls
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 常见头布局的公共方法，其他的用  mToolBar.inflateMenu(R.menu.menu)，添加菜单
     *
     * @param toolbar
     * @param textView
     * @param canBack  是否显示返回键
     * @param title    标题
     */
    protected void setToolbar(Toolbar toolbar, @NonNull TextView textView, boolean canBack, String title) {
        textView.setText(title);
        if (canBack) {
            if (null != toolbar) {
                setSupportActionBar(toolbar);
            }
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

}
