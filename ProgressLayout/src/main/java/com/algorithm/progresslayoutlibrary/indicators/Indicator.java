package com.algorithm.progresslayoutlibrary.indicators;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by algorithm on 2017/6/16.
 */

public abstract class Indicator extends Drawable implements Animatable {

    private HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners
            = new HashMap<>();

    private boolean mHasAnimators;// ?判断是否有动画
    private ArrayList<ValueAnimator> mAnimators;
    private int mAlpha = 255;

    /**
     * 在构造函数中进行初始化
     * draw(Canvas canvas)
     * abstract draw(Canvas canvas, Paint paint)
     */
    private Paint mPaint = new Paint();

    private static final Rect ZERO_BOUNDS_RECT = new Rect();
    protected Rect mDrawBounds = ZERO_BOUNDS_RECT;

    /**
     * Indicator Color
     */
    public Indicator() {
        mPaint.setColor(Color.parseColor("#38adff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        draw(canvas, mPaint);
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mAlpha = alpha;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    /**
     * 子类实现父类的onCreateAnimators
     */
    @Override
    public void start() {

        if (!mHasAnimators) {
            mAnimators = onCreateAnimators();
            mHasAnimators = true;
        }

        if (mAnimators == null) {
            return;
        }

        if (isStarted()) {
            return;
        }

        startAnimators();
        invalidateSelf();
    }

    /**
     * 具体由子类进行实现
     *
     * @return
     */
    protected abstract ArrayList<ValueAnimator> onCreateAnimators();

    private boolean isStarted() {

        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }
        return false;
    }

    private void startAnimators() {

        for (int i = 0; i < mAnimators.size(); i++) {

            ValueAnimator animator = mAnimators.get(i);
            ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            animator.start();
        }
    }

    @Override
    public void stop() {
        stopAnimators();
    }

    private void stopAnimators() {
        if (mAnimators != null) {
            for (ValueAnimator animator : mAnimators) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isRunning();
        }
        return false;
    }

    /*********************************************************/

    protected int getWidth() {
        return mDrawBounds.width();
    }

    protected int getHeight() {
        return mDrawBounds.height();
    }

    /**
     * Your should use this to add AnimatorUpdateListener when
     * create animator , otherwise , animator doesn't work when
     * the animation restart .
     *
     * @param updateListener
     */
    public void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener) {
        mUpdateListeners.put(animator, updateListener);
    }

    public void postInvalidate() {
        invalidateSelf();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setDrawBounds(bounds);
    }

    public void setDrawBounds(Rect drawBounds) {
        setDrawBounds(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom);
    }

    public void setDrawBounds(int left, int top, int right, int bottom) {
        this.mDrawBounds = new Rect(left, top, right, bottom);
    }

}
