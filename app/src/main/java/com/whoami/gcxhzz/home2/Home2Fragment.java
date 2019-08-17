package com.whoami.gcxhzz.home2;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.algorithm.android.widget.dialog.AlActionSheetDialog;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.app.MyApplication;
import com.whoami.gcxhzz.base.fragment.BaseFragment;
import com.whoami.gcxhzz.base.fragment.BaseTitleFragment;
import com.whoami.gcxhzz.entity.CVEntity;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.entity.TaskEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.RxBus;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class Home2Fragment extends BaseTitleFragment {
    @BindView(R.id.vp_viewpager)
    ViewPager vp_viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    private List<String> tabIndicators;
    private List<BaseFragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    AlActionSheetDialog sheetDialog;

    private String riverCode;
    private List<String> riverCodes=new ArrayList<>();
    private boolean isAdd=false;

    String[] strs = new String[]{"大通河","布哈河","哈尔盖河","吉尔孟河","泉吉河","沙柳河","青海湖北岸"};

    /*这里只是为了预防fragment没有attache*/
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
              if (msg.what==1){

                  for (BaseFragment fragment:tabFragments){
                      if (fragment.getActivity()==null){
                          isAdd=false;
                          break;
                      }
                      isAdd=true;
                  }
                  if (isAdd){
//                      getDTdata();

                  }else {
                      handler.sendEmptyMessageDelayed(1,2000);
                  }
              }
        }
    };

    Handler tempHandler = new Handler();
    @Override
    protected int onSetContentView() {
        return R.layout.home2_fragment;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"XX河段",0);

        sheetDialog= new AlActionSheetDialog(mContext).builder();

        initTab();
        initContent();
        tempHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxBus.getDefault().post(""+1);
                mTitle.setText(strs[0]);
            }
        },2000);
    }

    private void initTab(){
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setTabTextColors(ContextCompat.getColor(mContext, R.color.black), ContextCompat.getColor(mContext, R.color.theme_color));
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(mContext, R.color.theme_color));
//        ViewCompat.setElevation(tab_layout, 10);
        tab_layout.setupWithViewPager(vp_viewpager);

        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.show();
            }
        });
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
//        tabIndicators.add("取用水");
//        tabIndicators.add("水质");
//        tabIndicators.add("水生态");
//        tabIndicators.add("岸线");
        tabIndicators.add("水资源保护利用");
        tabIndicators.add("水域岸线管理保护");
        tabIndicators.add("水污染防治");
        tabIndicators.add("水环境保护");
        tabIndicators.add("水生态保护");
        tabIndicators.add("执法监管");

        tabFragments = new ArrayList<>();
        tabFragments.add(TabContent1Fragment.newInstance());
        tabFragments.add(TabContent2Fragment.newInstance());
        tabFragments.add(TabContent3Fragment.newInstance());
        tabFragments.add(TabContent4Fragment.newInstance());
        tabFragments.add(TabContent5Fragment.newInstance());
        tabFragments.add(TabContent6Fragment.newInstance());

        contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        vp_viewpager.setAdapter(contentAdapter);
        vp_viewpager.setOffscreenPageLimit(5);
        getHDData();
    }
    /*获取河段信息*/
    private void getHDData(){
        createDialog(null);
//        Map<String,Object> map=new HashMap<>();
//        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_RIVERBASEINFO_GET, map, new Novate.ResponseCallBack<Object>() {
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCompleted() {}
//
//            @Override
//            public void onError(Throwable e) {}
//
//            @Override
//            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
//                RiverEntity taskEntity=BaseUtils.getGson().fromJson(originalResponse,RiverEntity.class);
//                createDialog(taskEntity.getContent());
//                handler.sendEmptyMessage(1);
//            }
//        });
    }
    /*获取动态数据*/
    private void getDTdata(){
        Map<String,Object> map=new HashMap<>();
        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_INFORMATION_GET+"?riverCode="+riverCode, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {

                CVEntity cvEntity=BaseUtils.getGson().fromJson(originalResponse, CVEntity.class);
                RxBus.getDefault().post(cvEntity);
            }
        });
    }

    private void createDialog(List<RiverEntity.ContentBean> datas){

//        for (final RiverEntity.ContentBean contentBean:datas){
//            riverCodes.add(contentBean.getCode());
//            sheetDialog.addSheetItem(contentBean.getName(), AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
//                @Override
//                public void onClick(int which) {
//                    riverCode=contentBean.getCode();
//                    mTitle.setText(contentBean.getName());
//                    getDTdata();
//                }
//            });
//        }
//        if (riverCode==null){
//            riverCode=riverCodes.get(0);
//            mTitle.setText(datas.get(0).getName());
//        }

        for(final String str:strs){
            sheetDialog.addSheetItem(str, AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    RxBus.getDefault().post(""+which);
                    mTitle.setText(str);
                }
            });
        }
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().destroy();
    }
}
