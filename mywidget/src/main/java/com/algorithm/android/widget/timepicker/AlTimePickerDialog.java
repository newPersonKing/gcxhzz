package com.algorithm.android.widget.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.algorithm.android.widget.dialog.AlActionSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by algorithm on 2017/11/3.
 */

public class AlTimePickerDialog implements DatePickerView.OnSelectMoveListener {

    public static int DOOR_STATE_NOW  = 1;//1:马上
    public static int DOOR_STATE_APPOI  = 2;//马上

    private Context mContext;

    private Dialog mDialog;
    private LinearLayout mLLContainer;
    private CheckBox cbRightNow;
    private TextView tvHint;

    private DatePickerView mDatePickerView;
    private DatePickerView mTimePickerView;

    private OnPositiveClick mOnPositiveClick;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public AlTimePickerDialog(Context context) {
        mContext = context;
    }

    public AlTimePickerDialog Builder() {

        View view = View.inflate(mContext, R.layout.view_dialog_time_picker2, null);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        //立即上门
        cbRightNow = (CheckBox)view.findViewById(R.id.rb_right_now);
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
        //取消
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        //确定
        view.findViewById(R.id.tv_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dStr="";
                if(null != mDatePickerView.getText()){
                    dStr = mDatePickerView.getText().substring(3,mDatePickerView.getText().length());
                }
                Date d = new Date();
                try {
                    d = sdf.parse(dStr+" "+mTimePickerView.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(cbRightNow.isChecked()){//立即上门
                    mOnPositiveClick.click("立即上门",DOOR_STATE_NOW,System.currentTimeMillis());
                }else {//选择时间
                    mOnPositiveClick.click(mDatePickerView.getText() + " " + mTimePickerView.getText(),DOOR_STATE_APPOI,d.getTime());
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
        lp.height =  (int) (dm.heightPixels / 2.2);
        window.setAttributes(lp);

        addSheetItem();// 不暴露在外部，直接固定数据与控件

        return this;
    }

    public AlTimePickerDialog setTitleShow(boolean isShow){
        if(isShow){
            cbRightNow.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
        }else{
            cbRightNow.setVisibility(View.INVISIBLE);
            tvHint.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    private AlTimePickerDialog addSheetItem() {

        mDatePickerView = new DatePickerView(mContext);
        mTimePickerView = new DatePickerView(mContext);

        mDatePickerView.setmSelectMoveListener(this);
        mTimePickerView.setmSelectMoveListener(this);

        final Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i ++) {
            times.add(i + ":00");
            times.add(i + ":30");
        }

        List<String> dates = new ArrayList<>();

        // 控制datas 的顺序，今天的日期位于中间位置，可以循环进行滚动
        dates.add("今天 " + dateFormat.format(new Date()));
        dates.add("明天 " + dateFormat.format(DateUtils.getNextDay(new Date(), 1)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 2)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 2)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 3)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 3)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 4)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 4)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 5)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 5)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 6)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 6)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 7)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 7)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 8)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 8)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 9)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 9)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 10)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 10)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 11)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 11)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 12)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 12)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 13)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 13)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 14)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 14)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 15)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 15)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 16)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 16)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 17)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 17)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 18)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 18)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 19)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 19)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 20)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 20)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 21)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 21)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 22)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 22)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 23)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 23)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 24)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 24)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 25)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 25)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 26)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 26)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 27)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 27)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 28)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 28)));
        dates.add(DateUtils.getWeekSomeDay(DateUtils.getNextDay(new Date(), 29)) + " " + dateFormat.format(DateUtils.getNextDay(new Date(), 29)));

        mDatePickerView.setData(dates);
        mDatePickerView.setSelected(0);

        LinearLayout.LayoutParams dateLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        dateLP.weight = 3;
        dateLP.gravity = Gravity.CENTER_VERTICAL;

        mDatePickerView.setLayoutParams(dateLP);

        // TODO: 2017/11/15 第一次默认选择的回调
        mDatePickerView.setOnSelectListener(new DatePickerView.OnSelectListener() {
            @Override
            public void onSelect(String text) {

                if (text.substring(0, 2).equals("今天")) {

                    times.clear();

                    // 判断手机格式 时间格式 24消失或者12小时
                    if (android.text.format.DateFormat.is24HourFormat(mContext)) {
                        if (calendar.get(Calendar.MINUTE) < 5) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":05");
                        }

                        if (calendar.get(Calendar.MINUTE) < 10) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":10");
                        }

                        if (calendar.get(Calendar.MINUTE) < 15) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":15");
                        }

                        if (calendar.get(Calendar.MINUTE) < 20) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":20");
                        }

                        if (calendar.get(Calendar.MINUTE) < 25) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":25");
                        }

                        if (calendar.get(Calendar.MINUTE) < 30) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":30");
                        }

                        if (calendar.get(Calendar.MINUTE) < 35) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":35");
                        }

                        if (calendar.get(Calendar.MINUTE) < 40) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":40");
                        }

                        if (calendar.get(Calendar.MINUTE) < 45) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":45");
                        }

                        if (calendar.get(Calendar.MINUTE) < 50) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":50");
                        }

                        if (calendar.get(Calendar.MINUTE) < 55) {
                            times.add((calendar.get(Calendar.HOUR) + 12) + ":55");
                        }

                        for (int i = calendar.get(Calendar.HOUR) + 13; i < 24; i ++) {
                            times.add(i + ":00");
                            times.add(i + ":05");
                            times.add(i + ":10");
                            times.add(i + ":15");
                            times.add(i + ":20");
                            times.add(i + ":25");
                            times.add(i + ":30");
                            times.add(i + ":35");
                            times.add(i + ":40");
                            times.add(i + ":45");
                            times.add(i + ":50");
                            times.add(i + ":55");
                        }
                    } else {// 12小时格式 模拟器12小时制度

                        int am_pm = calendar.get(Calendar.AM_PM);// AM 0 PM 1
                        // AM & PM
                        if (am_pm == Calendar.AM) {
                            if (calendar.get(Calendar.MINUTE) < 5) {
                                times.add(calendar.get(Calendar.HOUR) + ":05");
                            }

                            if (calendar.get(Calendar.MINUTE) < 10) {
                                times.add(calendar.get(Calendar.HOUR) + ":10");
                            }

                            if (calendar.get(Calendar.MINUTE) < 15) {
                                times.add(calendar.get(Calendar.HOUR) + ":15");
                            }

                            if (calendar.get(Calendar.MINUTE) < 20) {
                                times.add(calendar.get(Calendar.HOUR) + ":20");
                            }

                            if (calendar.get(Calendar.MINUTE) < 25) {
                                times.add(calendar.get(Calendar.HOUR) + ":25");
                            }

                            if (calendar.get(Calendar.MINUTE) < 30) {
                                times.add(calendar.get(Calendar.HOUR) + ":30");
                            }

                            if (calendar.get(Calendar.MINUTE) < 35) {
                                times.add(calendar.get(Calendar.HOUR) + ":35");
                            }

                            if (calendar.get(Calendar.MINUTE) < 40) {
                                times.add(calendar.get(Calendar.HOUR) + ":40");
                            }

                            if (calendar.get(Calendar.MINUTE) < 45) {
                                times.add(calendar.get(Calendar.HOUR) + ":45");
                            }

                            if (calendar.get(Calendar.MINUTE) < 50) {
                                times.add(calendar.get(Calendar.HOUR) + ":50");
                            }

                            if (calendar.get(Calendar.MINUTE) < 55) {
                                times.add(calendar.get(Calendar.HOUR) + ":55");
                            }

                            for (int i = calendar.get(Calendar.HOUR) + 1; i < 24; i ++) {
                                times.add(i + ":00");
                                times.add(i + ":05");
                                times.add(i + ":10");
                                times.add(i + ":15");
                                times.add(i + ":20");
                                times.add(i + ":25");
                                times.add(i + ":30");
                                times.add(i + ":35");
                                times.add(i + ":40");
                                times.add(i + ":45");
                                times.add(i + ":50");
                                times.add(i + ":55");
                            }
                        } else {// PM 上午与下午判断

                            if (calendar.get(Calendar.MINUTE) < 5) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":05");
                            }

                            if (calendar.get(Calendar.MINUTE) < 10) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":10");
                            }

                            if (calendar.get(Calendar.MINUTE) < 15) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":15");
                            }

                            if (calendar.get(Calendar.MINUTE) < 20) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":20");
                            }

                            if (calendar.get(Calendar.MINUTE) < 25) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":25");
                            }

                            if (calendar.get(Calendar.MINUTE) < 30) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":30");
                            }

                            if (calendar.get(Calendar.MINUTE) < 35) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":35");
                            }

                            if (calendar.get(Calendar.MINUTE) < 40) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":40");
                            }

                            if (calendar.get(Calendar.MINUTE) < 45) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":45");
                            }

                            if (calendar.get(Calendar.MINUTE) < 50) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":50");
                            }

                            if (calendar.get(Calendar.MINUTE) < 55) {
                                times.add((calendar.get(Calendar.HOUR) + 12) + ":55");
                            }

                            for (int i = calendar.get(Calendar.HOUR) + 12 + 1; i < 24; i ++) {
                                times.add(i + ":00");
                                times.add(i + ":05");
                                times.add(i + ":10");
                                times.add(i + ":15");
                                times.add(i + ":20");
                                times.add(i + ":25");
                                times.add(i + ":30");
                                times.add(i + ":35");
                                times.add(i + ":40");
                                times.add(i + ":45");
                                times.add(i + ":50");
                                times.add(i + ":55");
                            }
                        }
                    }

                    mTimePickerView.setSelected(0);

                } else {

                    times.clear();

                    for (int i = 0; i < 24; i ++) {
                        times.add(i + ":00");
                        times.add(i + ":05");
                        times.add(i + ":10");
                        times.add(i + ":15");
                        times.add(i + ":20");
                        times.add(i + ":25");
                        times.add(i + ":30");
                        times.add(i + ":35");
                        times.add(i + ":40");
                        times.add(i + ":45");
                        times.add(i + ":50");
                        times.add(i + ":55");
                    }

                    mTimePickerView.setSelected(times.size() / 2);
                }
            }
        });


        mLLContainer.addView(mDatePickerView);

        LinearLayout.LayoutParams timeLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeLP.gravity = Gravity.CENTER_VERTICAL;
        timeLP.weight = 1;

        mTimePickerView.setData(times);

        mTimePickerView.setLayoutParams(timeLP);

        mTimePickerView.setOnSelectListener(new DatePickerView.OnSelectListener() {
            @Override
            public void onSelect(String text) {

            }
        });

        mLLContainer.addView(mTimePickerView);

        return this;
    }

    public void show() {
        mDatePickerView.doUpInit();
        mDialog.show();
    }

    @Override
    public void onSelectMove() {
        if(null != cbRightNow && cbRightNow.isChecked()){
            cbRightNow.setChecked(false);
        }
    }

    /**
     * 点击确定的回调接口 返回 所选取的时间点
     */
    public interface OnPositiveClick {
        void click(String text,int clickType,long timeStamp);
    }

    public AlTimePickerDialog setPositiveClick(OnPositiveClick onPositiveClick) {
        mOnPositiveClick = onPositiveClick;
        return this;
    }
}
