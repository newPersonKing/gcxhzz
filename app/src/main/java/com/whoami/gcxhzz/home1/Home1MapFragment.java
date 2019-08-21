package com.whoami.gcxhzz.home1;


import android.Manifest;
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;


import android.widget.Button;
import android.widget.Toast;


import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.gongwen.marqueen.SimpleMF;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.fragment.BaseFragment;
import com.whoami.gcxhzz.home3.MyRecordActivity;
import com.whoami.gcxhzz.home3.MyTaskActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.map.LocationActivity;
import com.whoami.gcxhzz.until.BaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class Home1MapFragment extends BaseFragment {


    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;


    @BindView(R.id.btn_record)
    Button btn_record;
    @BindView(R.id.btn_event)
    Button btn_event;
    @BindView(R.id.iv_go_map)
    AppCompatImageView iv_go_map;
    @BindView(R.id.btn_task_issue)
    Button btn_task_issue;

    //    @BindView(R.id.forum_context)
//    com.tencent.smtt.sdk.WebView forum_context;
    @BindView(R.id.mapview)
    MapView mMapView;

    private BaiduMap mBaiduMap;
    @Override
    protected int onSetContentView() {
        return R.layout.home1_map_fragment;
    }

    @Override
    protected void onInitData() {

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
//        forum_context.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
//                webView.loadUrl("http://office.leshuiiwater.com:18181/gisroot");
//                return true;
//            }
//        });
//        forum_context.getSettings().setJavaScriptEnabled(true);// 支持js
//        forum_context.getSettings().setUseWideViewPort(true); //自适应屏幕
//        forum_context.loadUrl("http://office.leshuiiwater.com:18181/gisroot");
        requestPermissionsHome();

        AnimationDrawable animationDrawable = (AnimationDrawable) iv_go_map.getBackground();
        animationDrawable.start();

    }
    private LocationClientOption mOption;
    private LocationClient client = null;

    private void initLocation(){
        if(client == null){
            client = new LocationClient(getContext());
            client.setLocOption(getDefaultLocationClientOption());
            client.registerLocationListener(mListener);
        }
        client.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(client!=null){
            client.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(client!=null){
            client.restart();
        }
        checkHasTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();
    }

    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption;
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (location == null || mMapView == null) {
                return;
            }
            Log.i("ccccccccccccc","首页定位返回结果了");

            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            //显示当前定位点，缩放地图 一直定位到当前点
            locateAndZoom(location, latLng);

            //起始点 设置 开始图标
//            MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
//            oStart.position(latLng);// 覆盖物位置点，第一个点为起点
//            oStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.foot)));// 设置覆盖物图片
//            mBaiduMap.addOverlay(oStart); // 在地图上添加此图层
        }
    };

    private MyLocationData locData;
    private MapStatus.Builder builder;
    private float mCurrentZoom = 18;
    //显示当前定位点，缩放地图
    private void locateAndZoom(BDLocation location, LatLng ll) {
        locData = new MyLocationData.Builder().accuracy(0)//去掉精度圈
                //此mCurrentDirection为自己获取到的手机传感器方向信息，顺时针0-360
//				.direction(mCurrentDirection)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);//显示当前定位位置点

        //给地图设置缩放中心点，和缩放比例值
        builder = new MapStatus.Builder();
        builder.target(ll).zoom(mCurrentZoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    @OnClick({
            R.id.btn_event,
            R.id.btn_record,
            R.id.iv_go_map,
            R.id.btn_task,
            R.id.btn_task_issue
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_event:
                if (mApplication.isLogin()){
                    Intent intent1=new Intent(mContext,MyEventRepotActivity.class);
                    startActivity(intent1);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.btn_record:
                Intent intent2=new Intent(mContext,MyRecordReportActivity.class);
                intent2.putExtra("TAG","Home1MapFragment");
                startActivity(intent2);
                break;
            case R.id.iv_go_map:
                if (mApplication.isLogin()){
                    requestPermissions();
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.btn_task:
                if (mApplication.isLogin()){
                    Intent intent=new Intent(mContext, MyTaskActivity.class);
                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
            case R.id.btn_task_issue:
                if(mApplication.isLogin()){
                    Intent intent = new Intent(mContext,IssuseTaskActivity.class);
                    startActivity(intent);
                }else{
                    BaseUtils.gotoLogin();
                }
                break;
        }
    }

    private void requestPermissions(){
        if(rxPermissions == null){
            rxPermissions = new RxPermissions(getActivity());
        }
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean){
                    Intent locationIntent = new Intent(mContext,LocationActivity.class);
                    locationIntent.putExtra("TAG","Home1MapFragment");
                    startActivity(locationIntent);
                }
            }
        });
    }

    private void requestPermissionsHome(){
        if(rxPermissions == null){
            rxPermissions = new RxPermissions(getActivity());
        }
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(false){
                    Toast.makeText(getContext(),"请同意定位权限",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    initLocation();
                }
            }
        });
    }

    private void checkHasTask(){
        Map<String,Object> map = new HashMap<>();
        HttpRequestUtils.getInstance().getNovate().rxBody(HttpService.CHECK_HAS_TASK, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject content = jsonObject.getJSONObject("content");
                    boolean isHas = content.optBoolean("isTaskHas");
                    btn_task_issue.setVisibility(isHas ? View.VISIBLE : View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });

    }

}
