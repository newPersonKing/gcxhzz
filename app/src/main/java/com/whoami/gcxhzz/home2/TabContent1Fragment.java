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

public class TabContent1Fragment extends BaseFragment {

    @BindView(R.id.qsksl)
    TextView qsksl;
    @BindView(R.id.xkqszl)
    TextView xkqszl;
    @BindView(R.id.sjnqszl)
    TextView sjnqszl;
    @BindView(R.id.yyssydsl)
    TextView yyssydsl;
    @BindView(R.id.sydngsl)
    TextView sydngsl;
    @BindView(R.id.sydszlb)
    TextView sydszlb;


    public static TabContent1Fragment newInstance(){
        return new TabContent1Fragment();
    }

    @Override
    protected int onSetContentView() {
        return R.layout.home2_tab1_content_fragment;
    }

    @Override
    protected void onInitData() {

        RxBus.getDefault().toObservable(CVEntity.class).subscribe(new Action1<CVEntity>() {
            @Override
            public void call(CVEntity cvEntity) {
                CVEntity.ContentBean data=cvEntity.getContent();
                if (cvEntity!=null){
                    qsksl.setText(getResources().getString(R.string.QSKSL,data.getC1V01()+""));
                    xkqszl.setText(getResources().getString(R.string.XKQSZL,data.getC1V02()+""));
                    sjnqszl.setText(getResources().getString(R.string.SJNQSZL,data.getC1V03()+""));
                    yyssydsl.setText(getResources().getString(R.string.YYSSYDSL,data.getC1V04()+""));
                    sydngsl.setText(getResources().getString(R.string.SYDNGSL,data.getC1V05()+""));
                    sydszlb.setText(getResources().getString(R.string.SYDSZLB,data.getC1V06()+""));
                }
            }
        });
    }

}
