package com.whoami.gcxhzz.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseActivity;
import com.whoami.gcxhzz.entity.UserInfo;
import com.whoami.gcxhzz.home1.Home1MapFragment;
import com.whoami.gcxhzz.home2.Home2Fragment;
import com.whoami.gcxhzz.home3.Home3Fragment;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.until.SPUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.main_view_pager)
    ViewPager main_view_pager;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    private List<Fragment> fragments=new ArrayList<>();
    @Override
    protected int onSetContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitData() {
        checkLogin();
        fragments.add(new Home1MapFragment());
        fragments.add(new Home2Fragment());
        fragments.add(new Home3Fragment());

        mRadioGroup.setOnCheckedChangeListener(this);

        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        main_view_pager.setAdapter(adapter);

    }

    private void checkLogin(){
        String username= (String) SPUtils.getInstance().get(SPUtils.SIGN_IN_NAME,"");
        final String password= (String) SPUtils.getInstance().get(SPUtils.SIGN_IN_PASSWORD,"");

        if (ObjectUtils.isNotNull(username) && ObjectUtils.isNotNull(password)) {
            HashMap<String,Object> map=new HashMap<>();
            map.put("userName", username);
            map.put("password",password);

            HttpRequestUtils.getInstance().getNovate().executeBody(HttpService.API_ACCOUNT_LOGIN, map, new Novate.ResponseCallBack<Object>() {
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
                    UserInfo userInfo= BaseUtils.getGson().fromJson(originalResponse, UserInfo.class);
                    /*登陆成功走这里*/
                    mApplication.saveLoginInfo(userInfo.getContent(),userInfo.getContent().getUsername(),userInfo.getContent().getRealName(),password);
                }
            });
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.homeTab1:
                main_view_pager.setCurrentItem(0);
                break;

            case R.id.homeTab2:
                main_view_pager.setCurrentItem(1);
                break;

            case R.id.homeTab3:
                main_view_pager.setCurrentItem(2);
                break;

        }

        main_view_pager.setOffscreenPageLimit(3);
        main_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               RadioButton radioButton= (RadioButton) mRadioGroup.getChildAt(position);
               radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}
