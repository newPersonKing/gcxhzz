package com.whoami.gcxhzz.home2;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.fragment.BaseFragment;
import com.whoami.gcxhzz.entity.CVEntity;
import com.whoami.gcxhzz.until.RxBus;

import butterknife.BindView;
import rx.functions.Action1;

public class TabContent4Fragment extends BaseFragment {

    @BindView(R.id.zaxcd)
    TextView zaxcd;
    @BindView(R.id.yaxcd)
    TextView yaxcd;
    @BindView(R.id.jxzaxcd)
    TextView jxzaxcd;
    @BindView(R.id.hjaxcd)
    TextView hjaxcd;
    @BindView(R.id.bhqaxgnqfcd)
    TextView bhqaxgnqfcd;
    @BindView(R.id.blqaxgnqfcd)
    TextView blqaxgnqfcd;
    @BindView(R.id.kzlyqaxgnfqcd)
    TextView kzlyqaxgnfqcd;
    @BindView(R.id.kflyqaxgnfqcd)
    TextView kflyqaxgnfqcd;
    @BindView(R.id.kflycd)
    TextView kflycd;
    @BindView(R.id.kflyl)
    TextView kflyl;

    public static TabContent4Fragment newInstance(){
        return new TabContent4Fragment();
    }

    @Override
    protected int onSetContentView() {
        return R.layout.home2_tab4_content_fragment;
    }

    @Override
    protected void onInitData() {

        RxBus.getDefault().toObservable(CVEntity.class).subscribe(new Action1<CVEntity>() {
            @Override
            public void call(CVEntity cvEntity) {
                CVEntity.ContentBean data=cvEntity.getContent();
                zaxcd.setText(getResources().getString(R.string.zaxcd,data.getC5V01()+""));
                yaxcd.setText(getResources().getString(R.string.yaxcd,data.getC5V03()+""));
                jxzaxcd.setText(getResources().getString(R.string.jxzaxcd,data.getC5V05()+""));
                hjaxcd.setText(getResources().getString(R.string.hjaxcd,data.getC5V07()+""));
                bhqaxgnqfcd.setText(getResources().getString(R.string.bhqaxgnqfcd,data.getC5V02()+""));
                blqaxgnqfcd.setText(getResources().getString(R.string.blqaxgnqfcd,data.getC5V04()+""));
                kzlyqaxgnfqcd.setText(getResources().getString(R.string.kzlyqaxgnfqcd,data.getC5V06()+""));
                kflyqaxgnfqcd.setText(getResources().getString(R.string.kflyqaxgnfqcd,data.getC5V08()+""));
                kflycd.setText(getResources().getString(R.string.kflycd,data.getC5V09()+""));
                kflyl.setText(getResources().getString(R.string.kflyl,data.getC5V10()+""));
            }
        });
    }
}
