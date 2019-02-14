package com.algorithm.progressdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.algorithm.progresslayoutlibrary.R;
import com.algorithm.progresslayoutlibrary.indicators.LoadingIndicatorView;

/**
 * Created by algorithm on 2017/7/14.
 */

public class ProgressDialog extends AlertDialog {

    /**
     * 圆形进度模式 在代码中通过方法setStyle进行设置
     */
    public static final int STYLE_SPINNER = 0x01;
    /**
     * 水平进度模式
     */
    public static final int STYLE_HORIZONTAL = 0x02;

    private LoadingIndicatorView mIndicatorView;
    private CharSequence mMessage;
    private TextView mMessageView;

    private int mProgressStyle = STYLE_SPINNER;

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProgressDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (mProgressStyle == STYLE_SPINNER) {
            View view = inflater.inflate(R.layout.view_algorithm_progress_dialog_spinner, null);
            mIndicatorView = (LoadingIndicatorView) view.findViewById(R.id.indicator);
            mMessageView = (TextView) view.findViewById(R.id.message);
            Log.e("", "");
            setView(view);
        } else {
        }

        /**
         * 没有这个方法 setMessage 在 onCreate之前调用
         */
        if (mMessage != null) {
            setMessage(mMessage);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mIndicatorView != null) {
            if (mProgressStyle == STYLE_HORIZONTAL) {
                super.setMessage(message);
            } else {
                mMessageView.setText(message);
            }
        } else {
            mMessage = message;
        }
    }
}
