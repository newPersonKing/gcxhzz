package com.algorithm.android.widget.timepicker;

import android.app.Dialog;
import android.content.Context;
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
 * Created by algorithm on 2018/5/4.
 */

public class ALTimePicker3Dialog implements DatePickerView.OnSelectMoveListener {

    public static int DOOR_STATE_NOW = 1;//1:马上
    public static int DOOR_STATE_APPOI = 2;//马上

    private Context mContext;

    private Dialog mDialog;
    private LinearLayout mLLContainer;
    private CheckBox cbRightNow;
    private TextView tvHint;

    private DatePickerView mDatePickerView;
    private DatePickerView mHourView;// 时间选择器
    private DatePickerView mMinView;// 分钟选择器

    private OnPositiveClick mOnPositiveClick;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ALTimePicker3Dialog(Context context) {
        mContext = context;
    }

    public ALTimePicker3Dialog Builder() {

        View view = View.inflate(mContext, R.layout.view_dialog_time_picker2, null);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        //立即上门
        cbRightNow = (CheckBox) view.findViewById(R.id.rb_right_now);
        tvHint = view.findViewById(R.id.tv_hint);
//        cbRightNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(rbRightNow.isChecked()){
//                    rbRightNow.setChecked(false);
//                }else{
//                    rbRightNow.setChecked(true);
//                }
//            }
//        });
        // 取消
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // 确定按钮点击
        view.findViewById(R.id.tv_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dStr = "";

                if (null != mDatePickerView.getText()) {
                    dStr = mDatePickerView.getText().substring(3, mDatePickerView.getText().length());
                }
                Date d = new Date();
                try {
                    d = sdf.parse(dStr + " " + mHourView.getText() + ":" + mMinView.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 回调返回数据
                if (cbRightNow.isChecked()) { //立即上门
                    mOnPositiveClick.click("立即上门", DOOR_STATE_NOW, System.currentTimeMillis());
                } else {//选择时间
                    mOnPositiveClick.click(mDatePickerView.getText() + " " + mHourView.getText() + ":" + mMinView.getText(), DOOR_STATE_APPOI, d.getTime());
                }
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

    public ALTimePicker3Dialog setTitleShow(boolean isShow) {
        if (isShow) {
            cbRightNow.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
        } else {
            cbRightNow.setVisibility(View.INVISIBLE);
            tvHint.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    private ALTimePicker3Dialog addSheetItem() {

        mDatePickerView = new DatePickerView(mContext);
        mHourView = new DatePickerView(mContext);
        mMinView = new DatePickerView(mContext);

        mDatePickerView.setmSelectMoveListener(this);
        mHourView.setmSelectMoveListener(this);
        mMinView.setmSelectMoveListener(this);

        final Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> dates = new ArrayList<>();

        for (int h = -30; h < 0; h++) {
            dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), h)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), h)));
        }

        // 控制datas 的顺序，今天的日期位于中间位置，可以循环进行滚动
        dates.add("今天 " + dateFormat.format(new Date()));
        dates.add("明天 " + dateFormat.format(DateUtils.getNextDay(new Date(), 1)));

        for (int h = 2; h < 30; h++) {
            dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), h)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), h)));
        }

        mDatePickerView.setData(dates);
        mDatePickerView.setSelected(0);

        LinearLayout.LayoutParams dateLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        dateLP.weight = 3;
        dateLP.gravity = Gravity.CENTER_VERTICAL;

        mDatePickerView.setLayoutParams(dateLP);

        mLLContainer.addView(mDatePickerView);

        List<String> hours = new ArrayList<>();
        hours.add("01");hours.add("02");hours.add("03");hours.add("04");hours.add("05");hours.add("06");
        hours.add("07");hours.add("08");hours.add("09");hours.add("10");hours.add("11");hours.add("12");
        hours.add("13");hours.add("14");hours.add("15");hours.add("16");hours.add("17");hours.add("18");
        hours.add("19");hours.add("20");hours.add("21");hours.add("22");hours.add("23");hours.add("00");

        mHourView.setData(hours);
        mHourView.setSelected(hours.size() / 2 - 1);

        LinearLayout.LayoutParams hourLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        hourLP.gravity = Gravity.CENTER_VERTICAL;
        hourLP.weight = 1;

        mHourView.setLayoutParams(hourLP);

        mLLContainer.addView(mHourView);

        List<String> minutes = new ArrayList<>();
        minutes.add("01");minutes.add("02");minutes.add("03");minutes.add("04");minutes.add("5");
        minutes.add("06");minutes.add("07");minutes.add("08");minutes.add("09");minutes.add("10");
        minutes.add("11");minutes.add("12");minutes.add("13");minutes.add("14");minutes.add("15");
        minutes.add("16");minutes.add("17");minutes.add("18");minutes.add("19");minutes.add("20");
        minutes.add("21");minutes.add("22");minutes.add("23");minutes.add("24");minutes.add("25");
        minutes.add("26");minutes.add("27");minutes.add("28");minutes.add("29");minutes.add("30");
        minutes.add("31");minutes.add("32");minutes.add("33");minutes.add("34");minutes.add("35");
        minutes.add("36");minutes.add("37");minutes.add("38");minutes.add("39");minutes.add("40");
        minutes.add("41");minutes.add("42");minutes.add("43");minutes.add("44");minutes.add("45");
        minutes.add("46");minutes.add("47");minutes.add("48");minutes.add("49");minutes.add("50");
        minutes.add("51");minutes.add("52");minutes.add("53");minutes.add("54");minutes.add("55");
        minutes.add("56");minutes.add("57");minutes.add("58");minutes.add("59");minutes.add("00");
        mMinView.setData(minutes);
        mMinView.setSelected(minutes.size() / 2 - 1);

        mMinView.setLayoutParams(hourLP);

        mLLContainer.addView(mMinView);

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
        void click(String text, int clickType, long timeStamp);
    }

    public ALTimePicker3Dialog setPositiveClick(ALTimePicker3Dialog.OnPositiveClick onPositiveClick) {
        mOnPositiveClick = onPositiveClick;
        return this;
    }
}
