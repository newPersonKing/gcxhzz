package com.whoami.gcxhzz.home3;

import android.webkit.WebView;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;

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
        mWebView.loadUrl("http://119.57.114.28:18181/About/PhoneCopyright");
    }
}
