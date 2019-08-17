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

public class TabContent3Fragment extends BaseFragment {

    @BindView(R.id.dndlts)
    TextView dndlts;
    @BindView(R.id.dndlcd)
    TextView dndlcd;
    @BindView(R.id.zrwhzynhqsl)
    TextView zrwhzynhqsl;
    @BindView(R.id.gjzdstgnqsl)
    TextView gjzdstgnqsl;
    @BindView(R.id.zdfjmsqsl)
    TextView zdfjmsqsl;
    @BindView(R.id.tv_content)
    TextView tv_content;

    public static TabContent3Fragment newInstance(){
        return new TabContent3Fragment();
    }



    @Override
    protected int onSetContentView() {
        return R.layout.home2_tab3_content_fragment;
    }

    @Override
    protected void onInitData() {
        RxBus.getDefault().toObservable(CVEntity.class).subscribe(new Action1<CVEntity>() {
            @Override
            public void call(CVEntity cvEntity) {
                CVEntity.ContentBean data=cvEntity.getContent();

                dndlts.setText(getResources().getString(R.string.dndlts,data.getC4V01()+""));
                dndlcd.setText(getResources().getString(R.string.dndlcd,data.getC4V02()+""));
                zrwhzynhqsl.setText(getResources().getString(R.string.zrwhzynhqsl,data.getC4V03()+""));
                gjzdstgnqsl.setText(getResources().getString(R.string.gjzdstgnqsl,data.getC4V04()+""));
                zdfjmsqsl.setText(getResources().getString(R.string.zdfjmsqsl,data.getC4V05()+""));

            }
        });

        RxBus.getDefault().toObservable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String str) {
                switch (str){
                    case "1":
                        tv_content.setText(getString(R.string.A3));
                        break;
                    case "2":
                        tv_content.setText(getString(R.string.B3));
                        break;
                    case "3":
                        tv_content.setText(getString(R.string.C3));
                        break;
                    case "4":
                        tv_content.setText(getString(R.string.D3));
                        break;
                    case "5":
                        tv_content.setText(getString(R.string.E3));
                        break;
                    case "6":
                        tv_content.setText(getString(R.string.F3));
                        break;
                    case "7":
                        tv_content.setText(getString(R.string.G3));
                        break;
                }
            }
        });
    }
}
