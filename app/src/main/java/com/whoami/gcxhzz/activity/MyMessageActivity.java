package com.whoami.gcxhzz.activity;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;

public class MyMessageActivity extends BaseTitleActivity {
    @Override
    protected int onSetContentView() {
        return R.layout.layout_my_message;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"个人中心",0);

    }
}
