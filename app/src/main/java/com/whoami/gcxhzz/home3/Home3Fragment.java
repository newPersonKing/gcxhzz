package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.MyMessageActivity;
import com.whoami.gcxhzz.base.fragment.BaseFragment;
import com.whoami.gcxhzz.base.fragment.BaseTitleFragment;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class Home3Fragment extends BaseTitleFragment {

    @BindView(R.id.tv_me_center_user_name)
    TextView tv_me_center_user_name;
    @BindView(R.id.civ_me_center_user_img)
    ImageView civ_me_center_user_img;

    @Override
    protected int onSetContentView() {
        return R.layout.home3_fragment;
    }

    @Override
    protected void onInitData() {
        setTitle(0,"个人中心",0);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mApplication.isLogin()){
            tv_me_center_user_name.setText(mApplication.getUserInfo().getUsername());
        }
    }

    @OnClick({
            R.id.ll_me_center_info,
            R.id.fv_xhrw,
            R.id.fv_xhrz,
            R.id.fv_xhsb,
            R.id.fv_shezhi
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.ll_me_center_info:
                if (mApplication.isLogin()){
                    /*个人资料暂时不要*/
//                    Intent intent=new Intent(mContext, MyMessageActivity.class);
//                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.fv_xhrw:
                if (mApplication.isLogin()){
                    Intent intent=new Intent(mContext, MyTaskActivity.class);
                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.fv_xhrz:
                if (mApplication.isLogin()){
                    Intent intent=new Intent(mContext, MyRecordActivity.class);
                    startActivity(intent);

                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.fv_xhsb:
                if (mApplication.isLogin()){
                    Intent intent=new Intent(mContext, MyEventActivity.class);
                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.fv_shezhi:
                if (mApplication.isLogin()){
                    Intent intent=new Intent(mContext, MySettingActivity.class);
                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
        }
    }
}
