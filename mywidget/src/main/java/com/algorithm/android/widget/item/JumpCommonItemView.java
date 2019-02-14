package com.algorithm.android.widget.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.android.widget.R;


/**
 * 左边图标文字 右边箭头
 * Created by 高炎 on 2016/7/19.
 */
public class JumpCommonItemView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    public JumpCommonItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.view_jump_common_item_layout, this);

        textView = (TextView) view.findViewById(R.id.tv_layout_me_item);
        imageView = (ImageView) view.findViewById(R.id.iv_layout_me_item);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JumpCommonItemView);

        int leftImageId = typedArray.getResourceId(R.styleable.JumpCommonItemView_jumpCommonItemViewSrc, 0);
        if (leftImageId != 0)
            imageView.setImageResource(leftImageId);

        CharSequence text = typedArray.getText(R.styleable.JumpCommonItemView_jumpCommonItemViewTitle);
        if (text != null)
            textView.setText(text);
        typedArray.recycle();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }

    }
}
