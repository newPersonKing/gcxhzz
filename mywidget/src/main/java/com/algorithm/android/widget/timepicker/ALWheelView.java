package com.algorithm.android.widget.timepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by algorithm on 2018/1/17.
 */

public class ALWheelView extends View {

    // 按下Y值
    private float mLastY;

    private Scroller mScroller;

    public ALWheelView(Context context) {
        super(context);
    }

    public ALWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ALWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScroller = new Scroller(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mLastY = event.getY();
                break;


        }


        return super.onTouchEvent(event);


    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    private void finishScroll( ) {

    }
}
