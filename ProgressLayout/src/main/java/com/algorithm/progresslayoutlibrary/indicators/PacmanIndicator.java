package com.algorithm.progresslayoutlibrary.indicators;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by algorithm on 2017/6/16.
 */

public class PacmanIndicator extends Indicator {

    private float mTranslateX;// 球体的左侧移动
    private int mAlpha;
    private float degrees1, degrees2;

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        drawPacMan(canvas, paint);
        drawCircle(canvas, paint);
    }

    private void drawPacMan(Canvas canvas, Paint paint) {

        /**
         * 父类方法 获取默认的矩形边界的高度与宽度
         */
        float x = getWidth() / 2;
        float y = getHeight() / 2;

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(degrees1);
        paint.setAlpha(255);
        RectF rectF1 = new RectF(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f);
        canvas.drawArc(rectF1, 0, 270, true, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(degrees2);
        paint.setAlpha(255);
        RectF rectF2 = new RectF(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f);
        canvas.drawArc(rectF2, 90, 270, true, paint);
        canvas.restore();

    }

    private void drawCircle(Canvas canvas, Paint paint) {
        float radius = getWidth() / 11;
        paint.setAlpha(mAlpha);
        canvas.drawCircle(mTranslateX, getHeight() / 2, radius, paint);
    }

    @Override
    protected ArrayList<ValueAnimator> onCreateAnimators() {

        ArrayList<ValueAnimator> animators = new ArrayList<>();
        float startT = getWidth() / 11;
        ValueAnimator translationAnim = ValueAnimator.ofFloat(getWidth() - startT, getWidth() / 2);
        translationAnim.setDuration(650);
        translationAnim.setInterpolator(new LinearInterpolator());
        translationAnim.setRepeatCount(-1);
        addUpdateListener(translationAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 122);
        alphaAnim.setDuration(650);
        alphaAnim.setRepeatCount(-1);
        addUpdateListener(alphaAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAlpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnim1 = ValueAnimator.ofFloat(0, 45, 0);
        rotateAnim1.setDuration(650);
        rotateAnim1.setRepeatCount(-1);
        addUpdateListener(rotateAnim1, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees1 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnim2 = ValueAnimator.ofFloat(0, -45, 0);
        rotateAnim2.setDuration(650);
        rotateAnim2.setRepeatCount(-1);
        addUpdateListener(rotateAnim2, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees2 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animators.add(translationAnim);
        animators.add(alphaAnim);
        animators.add(rotateAnim1);
        animators.add(rotateAnim2);
        return animators;
    }
}
