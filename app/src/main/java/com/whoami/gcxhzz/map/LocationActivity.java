package com.whoami.gcxhzz.map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.github.florent37.viewanimator.ViewAnimator;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/***
 * 单点定位示例，用来展示基本的定位结果，配置在LocationService.java中
 * 默认配置也可以在LocationService中修改
 * 默认配置的内容自于开发者论坛中对开发者长期提出的疑问内容
 *
 * @author baidu
 *
 */
public class LocationActivity extends Activity {
	private LocationService locationService;

	private MapView mMapView;
	private AppCompatImageView iv_back;
	private ImageView iv_down_up;
    private LinearLayout ll_content;
    private LinearLayout ll_trans;

	private Polyline mPolyline;
	private BaiduMap mBaiduMap;
	private ImageView iv_show;
	private SimpleMarqueeView simpleMarqueeView;
	private String preAddress;/*记录上一次定位的详细地址*/
	private int tag = 0;
	ViewAnimator roteAnimator,tranAnimator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// -----------demo view config ------------
		setContentView(R.layout.location);

		iv_show = findViewById(R.id.iv_show);
		ll_content = findViewById(R.id.ll_content);
		ll_trans = findViewById(R.id.ll_trans);
		iv_back = findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		iv_down_up = findViewById(R.id.iv_down_up);
		simpleMarqueeView = findViewById(R.id.simpleMarqueeView);

		mMapView = findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true);
		drawLine();

		iv_down_up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                tag++;
				if(tag%2 == 0){
					roteAnimator = ViewAnimator.animate(iv_down_up).rotation(180).duration(500).start();
					tranAnimator = ViewAnimator.animate(ll_content).translationY(ll_trans.getHeight()).duration(500).start();
				}else{
					iv_down_up.clearAnimation();
					ll_content.clearAnimation();
					ViewAnimator.animate(iv_down_up).rotation(0).duration(500).start();
					ViewAnimator.animate(ll_content).translationY(0).duration(500).start();
				}
			}
		});

		iv_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectRividerFragmentDialog dialog = new SelectRividerFragmentDialog();
                dialog.show(getFragmentManager(),LocationActivity.class.getSimpleName());
			}
		});

	}


	private float mCurrentZoom = 18;
	/*动态绘制移动路线*/
	private void drawLine(){
		/**添加地图缩放状态变化监听，当手动放大或缩小地图时，拿到缩放后的比例，然后获取到下次定位，
		 *  给地图重新设置缩放比例，否则地图会重新回到默认的mCurrentZoom缩放比例
		 */
		mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus) {

			}

			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

			}

			@Override
			public void onMapStatusChange(MapStatus mapStatus) {

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus mapStatus) {
				mCurrentZoom = mapStatus.zoom;//获取手指缩放地图后的值
			}
		});

		mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.FOLLOWING,true,null));

	}



	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// -----------location config ------------
		locationService = ((MyApplication) getApplication()).locationService;
		//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		//注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}

		locationService.start();
	}


	private Boolean isFirstLoc = true;
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

			/*地址改变 再切换*/
			String currentAddress = location.getAddrStr();
			if(!currentAddress.equals(preAddress)){
				/*顶部跑马灯*/
				//SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
				SimpleMF<String> marqueeFactory = new SimpleMF(LocationActivity.this);
				if(simpleMarqueeView.isFlipping()){
					simpleMarqueeView.stopFlipping();
				}
				List<String> simpleData = new ArrayList<>();
				simpleData.add("当前位置是:"+location.getAddrStr());
				simpleData.add("当前位置是:"+location.getAddrStr());
				preAddress = location.getAddrStr();
				marqueeFactory.setData(simpleData);
				simpleMarqueeView.setMarqueeFactory(marqueeFactory);
				simpleMarqueeView.startFlipping();
			}
			;
			//选一个精度相对较高的起始点
//					ll = getMostAccuracyLocation(location);
//					Log.i("cccccccccccc","ll====="+ll);
//					if(ll == null){
//						return;
//					}
			LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
			if(points.size() == 0){
				points.add(current);
			}else{
				LatLng last = points.get(points.size()-1);
				if(DistanceUtil.getDistance(last, current ) > 5){
					points.add(current);
				}else{
					return;
				}
			}

			//清除上一次轨迹，避免重叠绘画
			mMapView.getMap().clear();

			//显示当前定位点，缩放地图
			locateAndZoom(location, points.get(0));

			//起始点 设置 开始图标
			MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
			oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
			oStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.start_point)));// 设置覆盖物图片
			mBaiduMap.addOverlay(oStart); // 在地图上添加此图层

			if(points.size()>2){
				//将points集合中的点绘制轨迹线条图层，显示在地图上
				OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);

				mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
			}
		}
	};

	private List<LatLng> points = new ArrayList<>();


	private Double mCurrentLat;
	private Double  mCurrentLon;
	private MyLocationData locData;
	private MapStatus.Builder builder;
	//显示当前定位点，缩放地图
	private void locateAndZoom(BDLocation location, LatLng ll) {
/**
 * 记录当前经纬度，当位置不变，手机转动，取得方向传感器的方向，
 给地图重新设置位置参数，在跟随模式下可使地图箭头随手机转动而转动
 */
		mCurrentLat = location.getLatitude();
		mCurrentLon = location.getLongitude();
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

}
