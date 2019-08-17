package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algorithm.android.utils.DisplayUtils;
import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.luck.picture.lib.PictureSelector;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.download.DownLoadCallBack;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CustomPictureVideoPlayActivity;
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

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.btn_video_play)
    Button btn_video_play;
    @BindView(R.id.btn_voice_download)
    Button btn_voice_download;
    @BindView(R.id.iv_voice_play)
    Button iv_voice_play;
    @BindView(R.id.tv_river_name)
    TextView tv_river_name;
//    @BindView(R.id.ll_voice_play)
//    LinearLayout ll_voice_play;

    private int id;
    private String videoUrl,audioUrl;
//    private AnimationDrawable iv_voice_playDrawable;
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

//        iv_voice_playDrawable = (AnimationDrawable) iv_voice_play.getDrawable();

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

        if(ObjectUtils.isNotNull(eventEntityData.getVideoUrl())){
            btn_video_play.setVisibility(View.VISIBLE);
            videoUrl = eventEntityData.getVideoUrl();
        }

        if(ObjectUtils.isNotNull(eventEntityData.getAudioUrl())){
            audioUrl = eventEntityData.getAudioUrl();
            /*[判断显示什么*/
            String name =getFileName(audioUrl);
            File voiceFile = new File(voiceSavePath+"/"+name);
            if(voiceFile.exists()){
                btn_voice_download.setVisibility(View.GONE);
//                ll_voice_play.setVisibility(View.VISIBLE);
                iv_voice_play.setVisibility(View.VISIBLE);
            }else{
                btn_voice_download.setVisibility(View.VISIBLE);
            }
        }

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
        tv_river_name.setText(eventEntityData.getRiverName());
        tv_sb_time.setText(eventEntityData.getReportTime());
        tv_event_scope.setText(eventEntityData.getEventTitle());
        iv_event_urgency.setVisibility(View.VISIBLE);
        iv_event_state.setVisibility(View.VISIBLE);
        /*紧急状态*/
        if (eventEntityData.getUrgency()==1){
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

    @OnClick({
            R.id.btn_video_play,
            R.id.btn_voice_download,
            R.id.iv_voice_play
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_video_play:
                downLoadVideo();
                if(ObjectUtils.isNotNull(videoUrl)){
                    Intent videoIntent = new Intent(this,CustomPictureVideoPlayActivity.class);
                    videoIntent.putExtra("video_path", videoUrl);
                    startActivity(videoIntent);
                }
                break;
            case R.id.btn_voice_download:
                if(ObjectUtils.isNotNull(audioUrl)){
                    downLoadVoice();
                }
                break;
            case R.id.iv_voice_play:
                Play(audioUrl);
                break;
        }
    }
    String videoSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ZHIHE/VIDEODOWNLOAD/";
    /*在线播放 实在是有点卡*/
    private void downLoadVideo(){

        File dirFile = new File(videoSavePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        String[] strs = videoUrl.split("/");
        String name = strs[strs.length-1];
        File videoFile = new File(videoSavePath+"/"+name);
        if(videoFile.exists()){
            return;
        }
        HttpRequestUtils.getInstance().getNovate().download(System.currentTimeMillis()+"video",videoUrl,videoSavePath,name,
                new DownLoadCallBack(){

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSucess(String key, String path, String name, long fileSize) {
                        Log.i("ccccc","path===="+path+"==="+name);
                    }
                });
    }
    private String    voiceSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ZHIHE/VOICEDOWNLOAD/";
    private void downLoadVoice(){
        Log.i("cccccccc","downLoadVoice");
        File dirFile = new File(voiceSavePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        Log.i("cccccccc","downLoadVoice");
        String name = getFileName(audioUrl);
        File voiceFile = new File(voiceSavePath+"/"+name);
        Log.i("cccccccc","downLoadVoice"+voiceFile.exists());
        if(voiceFile.exists()){
            return;
        }
        Log.i("cccccccc","downLoadVoice");
        HttpRequestUtils.getInstance().getNovate().download(System.currentTimeMillis()+"voice",audioUrl,voiceSavePath,name,
                new DownLoadCallBack(){

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSucess(String key, String path, String name, long fileSize) {
                        Log.i("ccccc","path===="+path+"==="+name);
//                        ll_voice_play.setVisibility(View.VISIBLE);
                        iv_voice_play.setVisibility(View.VISIBLE);
                        btn_voice_download.setVisibility(View.GONE);
                    }
                });
    }

    private String getFileName(String path){
        String[] strs = path.split("/");
        String name = strs[strs.length-1];
        return name;
    }

    MediaPlayer mediaPlayer;

    public  void Play(String path) {
        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
//                    iv_voice_playDrawable.stop();
                    mediaPlayer.reset();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
