package com.whoami.gcxhzz.home3;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.android.utils.DisplayUtils;
import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.EventDetailsEntity;
import com.whoami.gcxhzz.entity.EventEntityData;
import com.whoami.gcxhzz.entity.RecordEntityData;
import com.whoami.gcxhzz.entity.TaskDetailEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ImageLoadUtils;
import com.whoami.gcxhzz.until.ObjectUtils;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*事件上报详情*/
public class MyEventDetailsActivity extends BaseTitleActivity {

    @BindView(R.id.tv_sj_ly)
    TextView tv_sj_ly;
    @BindView(R.id.tv_sb_time)
    TextView tv_sb_time;
    @BindView(R.id.content)
    WebView content;
    @BindView(R.id.ll_picture)
    LinearLayout ll_picture;
    @BindView(R.id.tv_event_scope)
    TextView tv_event_scope;
    @BindView(R.id.iv_event_urgency)
    ImageView iv_event_urgency;
    @BindView(R.id.iv_event_state)
    ImageView iv_event_state;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    private int id;
    @Override
    protected int onSetContentView() {
        return R.layout.layout_event_details;
    }

    @Override
    protected void onInitData() {
        setTitle(R.drawable.back,"详情页",0);
        id=getIntent().getIntExtra("EVENT_ID",0);

        progressLayout.showLoading();
        progressLayout.setOnErrorClickListener(new ProgressLayout.OnErrorClickListener() {
            @Override
            public void onClick() {
                progressLayout.showLoading();
                loadDetails();
            }
        });

        loadDetails();
    }


    /*事件上报详情*/
    private void loadDetails(){
        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_EVENTMANAGE_DETAIL+"?id="+id, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e.getCode() == 403) {
                    progressLayout.showError("用户没有权限,禁止访问");
                } else if (e.getCode() == 408 || e.getCode() == 504) {
                    progressLayout.showError("网络连接超时");
                } else if (e.getCode() == 302) {
                    progressLayout.showError("网络错误,查看网络是否连接");
                } else if (e.getCode() == 1006) {
                    progressLayout.showError("连接超时,请稍后再试");
                } else {
                    progressLayout.showError("网络连接异常");
                }
            }

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                EventDetailsEntity eventDetailsEntity= BaseUtils.getGson().fromJson(originalResponse,EventDetailsEntity.class);
                setData(eventDetailsEntity.getContent());
                progressLayout.showContent();
            }
        });
    }

    private void setData(EventEntityData eventEntityData){

        String source="";
        if (eventEntityData.getSource()==10){
            source="巡查河湖";
        }else if (eventEntityData.getSource()==20){
            source="督导检查";
        }else if (eventEntityData.getSource()==30){
            source="遥感监测";
        }else {
            source="社会监督";
        }

        tv_sj_ly.setText(source+"");
        tv_sb_time.setText(eventEntityData.getReportTime());
        tv_event_scope.setText(eventEntityData.getEventTitle());
        iv_event_urgency.setVisibility(View.VISIBLE);
        iv_event_state.setVisibility(View.VISIBLE);
        /*紧急状态*/
        if (eventEntityData.getUrgency()==0){
            iv_event_urgency.setImageResource(R.mipmap.general);
        }else {
            iv_event_urgency.setImageResource(R.mipmap.urgency);
        }

        if (eventEntityData.getState()==0){
            iv_event_state.setImageResource(R.mipmap.ic_activity_state_jx);
        }else {
           iv_event_state.setImageResource(R.mipmap.state_completed);
        }

        Document doc = Jsoup.parse(eventEntityData.getContent());
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }
        String html = doc.toString();
        content.loadData(html, "text/html; charset=UTF-8", null);
        addImg(eventEntityData.getFileUrl());
    }

    /**
     * 添加图片
     *
     * @param imgs
     */
    private void addImg(List<String> imgs) {
        if (!ObjectUtils.isNull(imgs)) {
            ll_picture.removeAllViews();
            for (int i = 0; i < imgs.size(); i++) {
                final ImageView newImg = new ImageView(mContext);
                newImg.setAdjustViewBounds(true);
                //newImg.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置想要的图片，相当于android:src="@drawable/image"
                //设置子控件在父容器中的位置布局，wrap_content,match_parent
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        DisplayUtils.getWindowsWidth(mContext) - 70,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = DisplayUtils.dip2px(mContext, 15);
                ll_picture.addView(newImg, params);
                ImageLoadUtils.loadImage(imgs.get(i), newImg);
            }
        }
    }
}
