package com.whoami.gcxhzz.activity;

import android.content.Intent;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.view.EmojiEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class CommonTextActivity  extends BaseTitleActivity {

    @BindView(R.id.et_content)
    EmojiEditText mETContent;
    @BindView(R.id.tv_num)
    TextView mTVNum;// 输入文字的数量
    @BindView(R.id.tv_max_num)
    TextView tvMaxNum;

    @BindView(R.id.tv_right)
    TextView tvRight;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.ll_tv_num_container)
    View llTVNumContainer;// 200／200 父布局包裹的LinearLayout


    /**
     * TitleLayout's title
     */
    public static final String TITLE = "title";
    private String mTitle = "请输入内容";

    /**
     * 输入的内容
     */
    public static final String CONTENT = "content";
    private String mContent;

    public static final String HINT = "hint";
    private String mHint;

    public static final String MAX_NUM = "max_num";
    private int mMaxNum = 200;

    /**
     * 返回的内容
     */
    public static final String CONTENT_RESULT = "content_result";

    @Override
    protected int onSetContentView() {
        return R.layout.activity_common_text;
    }

    @Override
    protected void onInitData() {

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(TITLE);
        mContent = intent.getStringExtra(CONTENT);
        mHint = intent.getStringExtra(HINT);
        mMaxNum = intent.getIntExtra(MAX_NUM, 200);


        setTitle(R.drawable.back, mTitle, "完成");
        //setTitle(R.drawable.back, mTitle, getString(R.string.finish));
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_size_17sp));
        tvRight.setTextColor(getResources().getColor(R.color.theme_color));


        tvMaxNum.setText("/" + mMaxNum);

        mETContent.setText(mContent);
        mTVNum.setText(ObjectUtils.isNull(mContent)?"0":mContent.length() + "");
        mETContent.setHint(mHint);

        mETContent.setOnTextChange(new EmojiEditText.onTextChange() {
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTVNum.setText(s.length() + "");
                selectionStart = mETContent.getSelectionStart();
                selectionEnd = mETContent.getSelectionEnd();
                if (mContent.length() > mMaxNum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mETContent.setText(s);
                    mETContent.setSelection(tempSelection);// 设置光标在最后
                }
            }
        });
    }

    @OnClick({R.id.btn_commit,R.id.tv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
            case R.id.btn_commit:
                if (!ObjectUtils.isNull(mContent)) {
                    Intent intent = new Intent();
                    intent.putExtra(CONTENT_RESULT, mContent);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
