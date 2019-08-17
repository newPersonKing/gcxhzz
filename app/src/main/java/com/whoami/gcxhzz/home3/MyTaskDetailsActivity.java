package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.RecordEntity;
import com.whoami.gcxhzz.entity.TaskDetailEntity;
import com.whoami.gcxhzz.entity.TaskEntityData;
import com.whoami.gcxhzz.home1.MyRecordReportActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.map.LocationActivity;
import com.whoami.gcxhzz.until.BaseUtils;
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
import butterknife.OnClick;

/*巡河任务详情*/
public class MyTaskDetailsActivity extends BaseTitleActivity {

    @BindView(R.id.tv_xd_ry)
    TextView tv_xd_ry;
    @BindView(R.id.tv_xd_time)
    TextView tv_xd_time;
    @BindView(R.id.tv_rw_ly)
    TextView tv_rw_ly;
    @BindView(R.id.tv_rw_lx)
    TextView tv_rw_lx;
    @BindView(R.id.tv_rw_xcrq)
    TextView tv_rw_xcrq;
    @BindView(R.id.content)
    WebView content;
    @BindView(R.id.iv_event_urgency)
    ImageView iv_event_urgency;
    @BindView(R.id.tv_event_scope)
    TextView tv_event_scope;
    @BindView(R.id.iv_event_state)
    ImageView iv_event_state;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    private TaskDetailEntity taskDetailEntity;

    /*任务id*/
    int id;
    @Override
    protected int onSetContentView() {
        return R.layout.layout_task_details;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"详情页",0);

        id= getIntent().getIntExtra("TASK_ID",0);
        loadDetails();

        progressLayout.showLoading();
        progressLayout.setOnErrorClickListener(new ProgressLayout.OnErrorClickListener() {
            @Override
            public void onClick() {
                progressLayout.showLoading();
                loadDetails();
            }
        });
    }
    /*加载详情*/
    private void loadDetails(){

        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executePost(HttpService.API_TASK_DETAIL+"?id="+id, map, new Novate.ResponseCallBack<Object>() {

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
                taskDetailEntity= BaseUtils.getGson().fromJson(originalResponse,TaskDetailEntity.class);
                setData(taskDetailEntity.getContent());
                progressLayout.showContent();
            }
        });
    }

    private void setData(TaskEntityData taskEntityData){
        tv_xd_ry.setText(taskEntityData.getPatrolUserName());
        tv_xd_time.setText(taskEntityData.getCreateTime());
        tv_rw_ly.setText(taskEntityData.getSource());
        tv_rw_lx.setText(taskEntityData.getType());
        tv_rw_xcrq.setText(taskEntityData.getPatrolDate());
        tv_event_scope.setText(taskEntityData.getRivers());
        iv_event_urgency.setVisibility(View.VISIBLE);
        iv_event_state.setVisibility(View.VISIBLE);
        /*紧急状态*/
        if ("紧急".equals(taskEntityData.getUrgency())){
            iv_event_urgency.setImageResource(R.mipmap.state_urgency);
        }else if ("一般".equals(taskEntityData.getUrgency())){
            iv_event_urgency.setImageResource(R.mipmap.general);
        }

        /*巡河任务 state 3 已巡河  其他待巡河*/
        if (taskEntityData.getState()==3){
            iv_event_state.setImageResource(R.mipmap.state_yxh);
        }else {
            iv_event_state.setImageResource(R.mipmap.state_dxh);
        }

        Document doc = Jsoup.parse(taskEntityData.getContent());
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }
        String html = doc.toString();

        content.loadData(html, "text/html; charset=UTF-8", null);
    }

    @OnClick({
            R.id.btn_add_my_record
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_my_record:
//                Intent intent=new Intent(this, MyRecordReportActivity.class);
//                TaskEntityData data=taskDetailEntity.getContent();
//                intent.putExtra("Rivers",data.getRivers());
//                intent.putExtra("State",data.getState());
//                intent.putExtra("TaskCode",data.getCode());
//                intent.putExtra("content",data.getContent());
//                intent.putExtra("name",data.getName());
//                intent.putExtra("riverCode",data.getRiversId()+"");
//                startActivity(intent);
                TaskEntityData data=taskDetailEntity.getContent();
                Intent intent = new Intent();
                if(taskDetailEntity.getContent().getState() == 3){
                    /*这里跳转巡河日志页面*/
                    intent.putExtra("RECORD_ID",data.getPatroLogCode());
                    intent.setClass(this,MyRecordDetailsActivity.class);
                }else{
                    intent.setClass(this,LocationActivity.class);
                    intent.putExtra("Rivers",data.getRivers());
                    intent.putExtra("State",data.getState());
                    intent.putExtra("TaskCode",data.getCode());
                    intent.putExtra("content",data.getContent());
                    intent.putExtra("name",data.getName());
                    intent.putExtra("riverCode",data.getRiversId()+"");
                    intent.putExtra("TAG","TASK");
                }
                startActivity(intent);
        }
    }

    /**
     * 添加图片
     *
     * @param imgs
     */
//    private void addImg(List<FileEntity> imgs, LinearLayout ll) {
//        if (!ObjectUtils.isNull(imgs)) {
//            final String[] imgs_ = new String[imgs.size()];
//            ll.removeAllViews();
//            for (int i = 0; i < imgs.size(); i++) {
//                final ImageView newImg = new ImageView(mContext);
//                imgs_[i] = imgs.get(i).getUrl();
//
//                newImg.setAdjustViewBounds(true);
//                //newImg.setScaleType(ImageView.ScaleType.FIT_XY);
//                //设置想要的图片，相当于android:src="@drawable/image"
//                //设置子控件在父容器中的位置布局，wrap_content,match_parent
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        DisplayUtils.getWindowsWidth(mContext) - 70,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.topMargin = DisplayUtils.dip2px(mContext, 15);
//                ll.addView(newImg, params);
//                ImageLoadUtils.loadImage(imgs.get(i).getUrl(), newImg);
//            }
//        }
//    }

}
