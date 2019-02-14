package com.algorithm.android.widget.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.android.utils.DateUtils;
import com.algorithm.android.widget.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by algorithm on 2018/1/17.
 * <p>
 * 数据内容 ：明天 2017-1-17
 */

public class ALDateWeekPickDialog implements DatePickerView.OnSelectMoveListener {

    private Context mContext;

    private Dialog mDialog;
    private LinearLayout mLLContainer;
    private CheckBox cbRightNow;
    private TextView tvHint;

    private DatePickerView mDatePickerView;

    private OnPositiveClick mOnPositiveClick;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ALDateWeekPickDialog(Context context) {
        mContext = context;
    }

    public ALDateWeekPickDialog Builder() {

        View view = View.inflate(mContext, R.layout.view_dialog_time_picker2, null);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);

        // 立即上门
        cbRightNow = (CheckBox) view.findViewById(R.id.rb_right_now);
        cbRightNow.setVisibility(View.GONE);
        tvHint = view.findViewById(R.id.tv_hint);
        tvHint.setVisibility(View.GONE);
        // 取消
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // 确定 点击
        view.findViewById(R.id.tv_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dStr = "";
                if (null != mDatePickerView.getText()) {
                    dStr = mDatePickerView.getText().substring(3, mDatePickerView.getText().length());
                }

                Date date = new Date();
                try {
                    date = sdf.parse(dStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mOnPositiveClick != null)
                    mOnPositiveClick.click(mDatePickerView.getText(), date.getTime());
                mDialog.dismiss();
            }
        });

        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        mDialog.setContentView(view);

//        // 布局的大小应该是这里进行了设置
//        Window dialogWindow = mDialog.getWindow();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
//        dialogWindow.setAttributes(lp);

        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        lp.height = (int) (dm.heightPixels / 2.2);
        window.setAttributes(lp);

        addSheetItem();// 不暴露在外部，直接固定数据与控件

        return this;
    }

    private ALDateWeekPickDialog addSheetItem() {

        mDatePickerView = new DatePickerView(mContext);

        mDatePickerView.setmSelectMoveListener(this);

        final Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> dates = new ArrayList<>();

        // 控制datas 的顺序，今天的日期位于中间位置，可以循环进行滚动
        for (int i = -61; i < 0; i ++) {
            dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), i)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), i)));
        }

        dates.add("今天 " + dateFormat.format(DateUtils.getNextDay(new Date(), 0)));
        dates.add("明天 " + dateFormat.format(DateUtils.getNextDay(new Date(), 1)));

        for (int i = 2; i < 61; i ++) {
            dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), i)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), i)));
        }

        mDatePickerView.setData(dates);
        mDatePickerView.setSelected(62);

        LinearLayout.LayoutParams dateLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        dateLP.weight = 3;
        dateLP.gravity = Gravity.CENTER_VERTICAL;

        mDatePickerView.setLayoutParams(dateLP);

        // TODO: 2017/11/15 第一次默认选择的回调
        mDatePickerView.setOnSelectListener(new DatePickerView.OnSelectListener() {
            @Override
            public void onSelect(String text) {

            }
        });


        mLLContainer.addView(mDatePickerView);

        LinearLayout.LayoutParams timeLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeLP.gravity = Gravity.CENTER_VERTICAL;
        timeLP.weight = 1;

        return this;
    }

    public void show() {
        mDatePickerView.doUpInit();
        mDialog.show();
    }

    @Override
    public void onSelectMove() {
        if (null != cbRightNow && cbRightNow.isChecked()) {
            cbRightNow.setChecked(false);
        }
    }

    /**
     * 点击确定的回调接口 返回 所选取的时间点
     */
    public interface OnPositiveClick {
        void click(String text, long timeStamp);
    }

    public ALDateWeekPickDialog setPositiveClick(OnPositiveClick onPositiveClick) {
        mOnPositiveClick = onPositiveClick;
        return this;
    }

}
