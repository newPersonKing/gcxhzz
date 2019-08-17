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

public class TabContent2Fragment extends BaseFragment {

    @BindView(R.id.tqhdqddmszlb)
    TextView tqhdqddmszlb;
    @BindView(R.id.tqhdyddmszlb)
    TextView tqhdyddmszlb;
    @BindView(R.id.szyyslhdcdzb)
    TextView szyyslhdcdzb;
    @BindView(R.id.szslshdcddzb)
    TextView szslshdcddzb;
    @BindView(R.id.szwlhdcdzb)
    TextView szwlhdcdzb;
    @BindView(R.id.szlwlshddcdzb)
    TextView szlwlshddcdzb;
    @BindView(R.id.sgnqszdbl)
    TextView sgnqszdbl;
    @BindView(R.id.tv_content)
    TextView tv_content;


    public static TabContent2Fragment newInstance(){
        return new TabContent2Fragment();
    }



    @Override
    protected int onSetContentView() {
        return R.layout.home2_tab2_content_fragment;
    }

    @Override
    protected void onInitData() {
        RxBus.getDefault().toObservable(CVEntity.class).subscribe(new Action1<CVEntity>() {
            @Override
            public void call(CVEntity cvEntity) {
                 CVEntity.ContentBean data=cvEntity.getContent();
                if (cvEntity!=null){
                    tqhdqddmszlb.setText(getResources().getString(R.string.tqhdqddmszlb,data.getC3V01()));
                    tqhdyddmszlb.setText(getResources().getString(R.string.tqhdyddmszlb,data.getC3V02()));
                    szyyslhdcdzb.setText(getResources().getString(R.string.szyyslhdcdzb,data.getC3V03()+""));
                    szslshdcddzb.setText(getResources().getString(R.string.szslshdcddzb,data.getC3V04()+""));
                    szwlhdcdzb.setText(getResources().getString(R.string.szwlhdcdzb,data.getC3V05()+""));
                    szlwlshddcdzb.setText(getResources().getString(R.string.szlwlshddcdzb,data.getC3V06()+""));
                    sgnqszdbl.setText(getResources().getString(R.string.sgnqszdbl,data.getC3V07()+""));
                }
            }
        });

        RxBus.getDefault().toObservable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String str) {
                switch (str){
                    case "1":
                        tv_content.setText(getString(R.string.A2));
                        break;
                    case "2":
                        tv_content.setText(getString(R.string.B2));
                        break;
                    case "3":
                        tv_content.setText(getString(R.string.C2));
                        break;
                    case "4":
                        tv_content.setText(getString(R.string.D2));
                        break;
                    case "5":
                        tv_content.setText(getString(R.string.E2));
                        break;
                    case "6":
                        tv_content.setText(getString(R.string.F2));
                        break;
                    case "7":
                        tv_content.setText(getString(R.string.G2));
                        break;
                }
            }
        });
    }
}
