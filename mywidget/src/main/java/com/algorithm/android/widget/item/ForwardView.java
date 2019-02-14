package com.algorithm.android.widget.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorithm.android.widget.R;

/**
 * Created by algorithm on 2017/11/3.
 */

public class ForwardView extends FrameLayout {

    private TextView mTVLeft;
    private TextView mTVRight;

    private ImageView ivForward;

    public ForwardView(Context context) {
        super(context);
        initView(context);
    }

    public ForwardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ForwardView);

        CharSequence leftText = array.getText(R.styleable.ForwardView_algorithmLeftText);

        if (!TextUtils.isEmpty(leftText)) {
            mTVLeft.setText(leftText);
        }

        CharSequence rightText = array.getText(R.styleable.ForwardView_algorithmRightText);
        if (!TextUtils.isEmpty(leftText)) {
            mTVRight.setHint(rightText);
        }

        boolean visible = array.getBoolean(R.styleable.ForwardView_forwardVisible, true);
        if (visible) {
            ivForward.setVisibility(VISIBLE);
        } else {
            ivForward.setVisibility(GONE);
        }

        boolean isHint = array.getBoolean(R.styleable.ForwardView_isHint,false);
        if(isHint){
            mTVLeft.setText("");
            mTVLeft.setHint(leftText);
            mTVRight.setText("");
            mTVRight.setHint(rightText);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView(Context context) {

        View view = View.inflate(context, R.layout.view_forward, this);
        mTVLeft = (TextView) view.findViewById(R.id.tv_left);
        mTVRight = (TextView) view.findViewById(R.id.tv_right);
        ivForward = (ImageView) view.findViewById(R.id.forward);
    }

    public void setLeftText(CharSequence text) {
        mTVLeft.setText(text);
    }

    public void setRightText(CharSequence text) {
        mTVRight.setText(text);
    }

    public void setRightHintText(CharSequence hintText) {
        mTVRight.setHint(hintText);
    }

    public void setLeftHintText(CharSequence hintText) {
        mTVLeft.setHint(hintText);
    }

    public void setRightTextColor(int color){
        if(color != 0) {
            mTVRight.setTextColor(color);
        }
    }

    public String getLeftText() {
        return mTVLeft.getText().toString();
    }

    public String getRightText() {
        return mTVRight.getText().toString();
    }

    public void setRightForwardVisiable(boolean visible) {

        if (visible) {
            ivForward.setVisibility(VISIBLE);
        } else {
            ivForward.setVisibility(GONE);
        }
    }

    public TextView getLeftTextView(){
        return mTVLeft;
    }

    public TextView getRightTextView(){
        return mTVRight;
    }
}
