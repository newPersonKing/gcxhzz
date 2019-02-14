package com.algorithm.progresslayoutlibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by algorithm on 2017/6/16.
 * 三种状态
 * <p>
 * 1.content
 * 2.loading
 * 3.empty
 * 4.error
 */

public class ProgressLayout extends RelativeLayout {

    /**
     * 应该是控制addView
     */
    private static final int TAG_LOADING = 0x01;
    private static final int TAG_EMPTY = 0x02;
    private static final int TAG_ERROR = 0x03;

    private OnErrorClickListener mOnErrorClickListener;

    private LayoutInflater mLayoutInflater;
    private View mRootView;

    private Drawable mBackground;

    private List<View> mContentViews = new ArrayList<>();

    private LayoutParams mLayoutParams;// RelativeLayout内部的LayoutParams

    // RelativeLayout
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;

    public ProgressLayout(Context context) {
        super(context);
        init();
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(CENTER_IN_PARENT);

        // 如果外部使用ProgressLayout的Background 目的是抵消背景
        mBackground = getBackground();
    }

    /**
     * @param child
     * @param params
     */
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);

        // mLoadingView
        // mEmptyView
        // mErrorView
        // 并不会添加到mContentViews中
        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {

            mContentViews.add(child);
        }
    }

    public void setLoadingView() {


    }

    public void showLoading2() {
        if (mLoadingView == null) {

            if (mEmptyView == null) {
            } else {
                mEmptyView.setVisibility(GONE);
            }

            if (mErrorView == null) {
            } else {
                mErrorView.setVisibility(GONE);
            }

            mRootView = mLayoutInflater.inflate(R.layout.progress_layout_loading_view, null);
            mLoadingView = mRootView.findViewById(R.id.loadingView);
            mLoadingView.setTag(TAG_LOADING);

            View loadingIndicator = mRootView.findViewById(R.id.loadingIndicator);
            // LoadingIndicator width & height
            loadingIndicator.getLayoutParams().width = (int) getResources().getDimension(R.dimen.dp_64);
            loadingIndicator.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dp_64);
            loadingIndicator.requestLayout();

            addView(mLoadingView, mLayoutParams);
            setContentVisible(false, Collections.<Integer>emptyList());

        } else {
            if (mEmptyView == null) {
            } else {
                mEmptyView.setVisibility(GONE);
            }

            if (mErrorView == null) {
            } else {
                mErrorView.setVisibility(GONE);
            }
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    public void showLoading() {
        if (mLoadingView == null) {

            if (mEmptyView == null) {
            } else {
                mEmptyView.setVisibility(GONE);
            }

            if (mErrorView == null) {
            } else {
                mErrorView.setVisibility(GONE);
            }

            mRootView = mLayoutInflater.inflate(R.layout.progress_layout_loading_shimmer, null);
            mLoadingView = mRootView.findViewById(R.id.shimmerLayout);
            mLoadingView.setTag(TAG_LOADING);

            addView(mLoadingView, mLayoutParams);
            setContentVisible(false, Collections.<Integer>emptyList());

        } else {
            if (mEmptyView == null) {
            } else {
                mEmptyView.setVisibility(GONE);
            }

            if (mErrorView == null) {
            } else {
                mErrorView.setVisibility(GONE);
            }
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置Content的内容是否可见
     *
     * @param visible
     * @param ids
     */
    private void setContentVisible(boolean visible, List<Integer> ids) {
        for (View v : mContentViews) {
            if (ids.contains(v.getId())) {
            } else {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

    }

    public void showContent() {
        hideRootView();
        setContentVisible(true, Collections.<Integer>emptyList());
    }

    private void hideRootView() {

        if (mRootView != null) {
            if (mLoadingView != null) {
                mLoadingView.setVisibility(GONE);
            }
            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
            }
            if (mErrorView != null) {
                mErrorView.setVisibility(GONE);
            }
            mRootView.setVisibility(View.GONE);
        }
    }

    public void showEmpty() {

        if (mEmptyView == null) {

            if (mLoadingView != null) {
                mLoadingView.setVisibility(GONE);
            }

            if (mErrorView != null) {
                mErrorView.setVisibility(GONE);
            }

            mRootView = mLayoutInflater.inflate(R.layout.progress_empty_view, null);
            mEmptyView = mRootView.findViewById(R.id.emptyView);
            mEmptyView.setTag(TAG_EMPTY);

            addView(mRootView, mLayoutParams);

            setContentVisible(false, Collections.<Integer>emptyList());
        } else {
            mEmptyView.setVisibility(VISIBLE);
        }

    }

    public void showError() {

        if (mErrorView == null) {

            if (mLoadingView == null) {
            } else {
                mLoadingView.setVisibility(GONE);
            }

            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
            }

            mRootView = mLayoutInflater.inflate(R.layout.progress_layout_error_view, null);
            mErrorView = mRootView.findViewById(R.id.errorView);
            mErrorView.setTag(TAG_ERROR);

            if (mOnErrorClickListener != null) {
                mErrorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnErrorClickListener.onClick();
                    }
                });
            }

            addView(mRootView, mLayoutParams);

            setContentVisible(false, Collections.<Integer>emptyList());

        } else {
            mErrorView.setVisibility(VISIBLE);
        }
    }

    public ProgressLayout showError(String msg) {
        if (mErrorView == null) {

            if (mLoadingView == null) {
            } else {
                mLoadingView.setVisibility(GONE);
            }

            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
            }

            mRootView = mLayoutInflater.inflate(R.layout.progress_layout_error_view, null);
            mErrorView = mRootView.findViewById(R.id.errorView);
            mErrorView.setTag(TAG_ERROR);

            if (mOnErrorClickListener != null) {
                mErrorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnErrorClickListener.onClick();
                    }
                });
            }

            TextView tvError = (TextView) mRootView.findViewById(R.id.tv_error);
            tvError.setText(msg);

            addView(mRootView, mLayoutParams);

            setContentVisible(false, Collections.<Integer>emptyList());

        } else {
            mErrorView.setVisibility(VISIBLE);
        }

        return this;
    }

    public void setOnErrorClickListener(OnErrorClickListener listener) {
        mOnErrorClickListener = listener;
    }

    public interface OnErrorClickListener {
        void onClick();
    }

}
