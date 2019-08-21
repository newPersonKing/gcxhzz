package com.whoami.gcxhzz.home3;

import android.webkit.WebView;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;

import butterknife.BindView;



/*版权说明*/
public class CopyrightWebViewActivity extends BaseTitleActivity {

    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected int onSetContentView() {
        return R.layout.layout_copyright;
    }

    @Override
    protected void onInitData() {
        mWebView.loadUrl(HttpRequestUtils.HOST_URL+"/About/PhoneCopyright");
    }
}
