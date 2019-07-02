package com.whoami.gcxhzz.home1;


import android.Manifest;
import android.content.Intent;

import android.os.Build;
import android.util.Log;
import android.view.View;


import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.luck.picture.lib.permissions.RxPermissions;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.fragment.BaseFragment;
import com.whoami.gcxhzz.map.LocationActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class Home1MapFragment extends BaseFragment {


    @BindView(R.id.home_webview)
    WebView mWebView;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;


    @BindView(R.id.btn_record)
    Button btn_record;
    @BindView(R.id.btn_event)
    Button btn_event;
    @BindView(R.id.btn_go_map)
    Button btn_go_map;

    @BindView(R.id.forum_context)
    com.tencent.smtt.sdk.WebView forum_context;


    @Override
    protected int onSetContentView() {
        return R.layout.home1_map_fragment;
    }

    @Override
    protected void onInitData() {


//        mWebView.getSettings().setBlockNetworkImage(false);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.setVisibility(View.GONE);
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        // 设置可以被显示的屏幕控制
//        mWebView.getSettings().setDisplayZoomControls(true);
//        mWebView.getSettings().setSupportMultipleWindows(true);
//        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setAllowContentAccess(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//          mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                mWebView.loadUrl("http://office.leshuiiwater.com:18181/gisroot");
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.i("cccccccccccc","onPageFinished"+view.getProgress());
//                if (view.getProgress() == 100) {
//                    view.setVisibility(View.VISIBLE);
//                    progressLayout.showContent();
//                }
//            }
//
//
//        });
//
//        mWebView.loadUrl("http://office.leshuiiwater.com:18181/gisroot");
//        progressLayout.showLoading();
        forum_context.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
                webView.loadUrl("http://office.leshuiiwater.com:18181/gisroot");
                return true;
            }
        });
        forum_context.getSettings().setJavaScriptEnabled(true);// 支持js
        forum_context.getSettings().setUseWideViewPort(true); //自适应屏幕
        forum_context.loadUrl("http://office.leshuiiwater.com:18181/gisroot");



    }

    @OnClick({
            R.id.btn_event,
            R.id.btn_record,
            R.id.btn_go_map
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_event:
                Intent intent1=new Intent(mContext,MyEventRepotActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_record:
                Intent intent2=new Intent(mContext,MyRecordReportActivity.class);
                intent2.putExtra("TAG","Home1MapFragment");
                startActivity(intent2);
                break;
            case R.id.btn_go_map:
                requestPermissions();
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
                    Intent intent3 = new Intent(mContext,LocationActivity.class);
                    startActivity(intent3);
                }
            }
        });
    }


}
