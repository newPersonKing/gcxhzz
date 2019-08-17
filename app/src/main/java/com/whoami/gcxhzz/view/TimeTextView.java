package com.whoami.gcxhzz.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TimeTextView extends AppCompatTextView {

    private long startTime;
    private long endTime;
    private long resumeTime;
    private long currentTime;
    private long pauseTime;
    private long totalTime ;
    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*设置的都是最新的开始时间*/
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void setPauseTime(long pauseTime){
        this.pauseTime = pauseTime;
        totalTime = totalTime + pauseTime - startTime;
    }


    public void setEndTime(long endTime){
        this.endTime = endTime;
    }

    public void setResumeTime(long resumeTime){
        this.resumeTime = resumeTime;
    }

    public void setCurrentTime(long currentTime){
        long timeRange = 0;/*保存 当前时间 距离上次开始的时间*/
        this.currentTime = currentTime;
        if(pauseTime == 0){/*还没有暂停过*/
            timeRange = currentTime-startTime;
        }else{
         timeRange = currentTime - startTime;
        }
        setText(formatDuring(timeRange+totalTime));
    }

    private  String formatDuring(long mss) {
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        String resut = changeText(hours+"") + " : " + changeText(minutes+"") + " : "
                + changeText(seconds+"");
        return  resut;
    }

    private String changeText(String text){
        if(text.length() ==1){
            return "0"+text;
        }else if(text.length() ==0){
            return "00";
        }
        return text;
    }
}
