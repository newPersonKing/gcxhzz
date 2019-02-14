package com.algorithm.android.utils;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by algorithm on 2017/12/20.
 */

public class ALRecyclerViewDecorationMarginHorizontal extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final int mSize;
    private final int mMarginHorizontal;

    public ALRecyclerViewDecorationMarginHorizontal(Resources resources, @ColorRes int color,
                                                    @DimenRes int size, @DimenRes int marginLeft) {
        mDivider = new ColorDrawable(resources.getColor(color));
        mSize = resources.getDimensionPixelSize(size);
        mMarginHorizontal = resources.getDimensionPixelSize(marginLeft);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left;
        int right;
        int top;
        int bottom;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getTop() + child.getPaddingTop() + mMarginHorizontal;
            bottom = child.getBottom() - child.getPaddingBottom() - mMarginHorizontal;
            left = child.getRight() + params.rightMargin;
            right = left + mSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.set(0, 0, 0, mSize);
    }
}
