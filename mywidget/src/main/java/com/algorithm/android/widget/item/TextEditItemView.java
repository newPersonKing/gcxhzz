package com.algorithm.android.widget.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.android.widget.R;


/**
 * 左边文字 右边EditText
 * Created by Spring on 2016/7/13.
 */
public class TextEditItemView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static String TAG = TextEditItemView.class.getSimpleName();

    private TextView textView;
    private EditText editText;
    private ImageView clean;

    public TextEditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_text_edit_layout, this);
        textView = (TextView) view.findViewById(R.id.tv_layout_text_edit_item);
        editText = (EditText) view.findViewById(R.id.et_layout_text_edit_item);
        editText.addTextChangedListener(this);
        clean = (ImageView) view.findViewById(R.id.iv_clean_layout_text_edit_item);
        clean.setOnClickListener(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextEditItemView);


        int textColor = typedArray.getColor(R.styleable.TextEditItemView_textCommonItemViewTextColor, 0);
        if (textColor != 0)
            editText.setTextColor(textColor);

        CharSequence text = typedArray.getText(R.styleable.TextEditItemView_textCommonItemViewText);
        if (text != null)
            textView.setText(text);

        CharSequence hint = typedArray.getText(R.styleable.TextEditItemView_textCommonItemViewHint);
        if (hint != null)
            editText.setHint(hint);

        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_clean_layout_text_edit_item) {
            editText.setText("");
            editText.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s == editText.getText()) {
            if (s.length() == 0) {
                clean.setVisibility(View.INVISIBLE);
            } else {
                clean.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * 得到用户填写的字符
     *
     * @return
     */
    public String getText() {
        if (null != editText) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    public void setText(String string) {
        editText.setText(string);
    }

    /**
     * 设置输入类型
     *
     * @param type
     */
    public void setInputType(int type) {
        if (null != editText) {
            editText.setInputType(type);
        }
    }


    /**
     * 设置过滤器
     *
     * @param inputFilters
     */
    public void setFilters(InputFilter[] inputFilters) {
        if (null != editText && null != inputFilters && inputFilters.length > 0) {
            editText.setFilters(inputFilters);
        }
    }

    /**
     * editText 是否可编辑  此方法一定要在文本设置之后在调用
     */
    public void setEnabledNot(boolean enabled) {
        editText.setEnabled(enabled);
        clean.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置输入最大长度
     *
     * @param max
     */
    public void setMaxLength(int max) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    /**
     * 取得editText
     * @return
     */
    public EditText getEditText() {
        return editText;
    }

}
