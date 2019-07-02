package com.whoami.gcxhzz.base.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.luck.picture.lib.permissions.RxPermissions;
import com.whoami.gcxhzz.base.app.MyApplication;
import com.whoami.gcxhzz.until.RxBus;

import butterknife.ButterKnife;

import static com.whoami.gcxhzz.until.RxBus.getDefault;

/**
 * Created by algorithm on 2017/10/25.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected static String TAG = "BaseFragment";

    public MyApplication mApplication;
    protected Activity mContext;
    protected View mRootView;// Fragment的View
    protected ProgressDialog mProgressDialog;

    protected boolean isPrepare;
    public boolean isTouched=false;

    public RxPermissions rxPermissions;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
        mApplication = (MyApplication) getActivity().getApplication();
        mContext = (Activity) context;

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        isTouched=true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(onSetContentView(), container, false);
        } else {
        }
        /*Fragment 中使用ButterKnife*/
        ButterKnife.bind(this, mRootView);
        isPrepare=true;
        onInitData(savedInstanceState);
        onInitData();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    protected abstract int onSetContentView();

    protected abstract void onInitData();

    @Override
    public void onClick(View v) {

    }
    public void loadDate(){};

    public  void onInitData(Bundle savedInstanceState){

    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
