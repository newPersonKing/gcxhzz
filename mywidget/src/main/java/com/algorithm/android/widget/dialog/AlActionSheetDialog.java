package com.algorithm.android.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.algorithm.android.utils.DisplayUtils;
import com.algorithm.android.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by algorithm on 2017/11/2.
 */

public class AlActionSheetDialog {

    private Context context;
    private Dialog dialog;
    private TextView tvTitle;
    private TextView tvCancel;
    private LinearLayout lLayoutContent;
    private ScrollView sLayoutContent;
    private List<SheetItem> sheetItemList;
    private Display display;

    private boolean isExecute = false; //是否执行


    public AlActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_action_sheet, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        // 获取自定义Dialog布局中的控件
        sLayoutContent = (ScrollView) view.findViewById(R.id.sl_alert_sheet_ayout_content);
        lLayoutContent = (LinearLayout) view.findViewById(R.id.ll_alert_sheet_ayout_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_alert_sheet_title);
        tvCancel = (TextView) view.findViewById(R.id.tv_alert_sheet_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public AlActionSheetDialog setTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        return this;
    }

    public AlActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认黑色
     * @param listener
     * @return
     */
    public AlActionSheetDialog addSheetItem(String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }
        int size = sheetItemList.size();
        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayoutContent.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayoutContent.setLayoutParams(params);
        }
        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;
            TextView textView = new TextView(context);
            textView.setBackgroundResource(R.drawable.click_background);
            textView.setText(strItem);
            textView.setTextSize(16);// 设置字体的大小
            textView.setGravity(Gravity.CENTER);
            textView.setMaxLines(2);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            // 背景颜色
            //textView.setBackgroundResource(R.color.color_white_a1);
            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Black.getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }
            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            // 点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    dialog.dismiss();
                }
            });
            lLayoutContent.addView(textView);
            // 添加一个细线
            if (i < size) {
                TextView line = new TextView(context);
                line.setBackgroundColor(Color.parseColor("#f5f5f5"));
                line.setHeight(DisplayUtils.dip2px(context, 1));
                lLayoutContent.addView(line);
            }
        }
    }

    public void show() {
        if (!isExecute) {
            isExecute = true;
            setSheetItems();
        }
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color, OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        Black("#222222"), Blue("#0097e0");
        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 提供app查看图片保存市显示UI
     *
     * @return
     */
    public AlActionSheetDialog setImageShowItem() {
        // acti_line.setVisibility(View.GONE);
        tvCancel.setTextColor(Color.parseColor(SheetItemColor.Black.getName()));
        return this;
    }
}
