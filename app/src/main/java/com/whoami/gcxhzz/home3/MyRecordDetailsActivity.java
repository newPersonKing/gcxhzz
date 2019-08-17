package com.whoami.gcxhzz.home3;

import android.content.Intent;
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
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.download.DownLoadCallBack;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CustomPictureVideoPlayActivity;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.EventDetailsEntity;
import com.whoami.gcxhzz.entity.RecordDetailsEntity;
import com.whoami.gcxhzz.entity.RecordEntityData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyRecordDetailsActivity extends BaseTitleActivity {

    @BindView(R.id.tv_xh_rw_mc)
    TextView tv_xh_rw_mc;
    @BindView(R.id.tv_xh_fw)
    TextView tv_xh_fw;
    @BindView(R.id.tv_xh_gj)
    TextView tv_xh_gj;
    @BindView(R.id.tv_xh_time)
    TextView tv_xh_time;
    @BindView(R.id.tv_fx_wt)
    TextView tv_fx_wt;
    @BindView(R.id.content)
    WebView content;
    @BindView(R.id.ll_picture)
    LinearLayout ll_picture;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    @BindView(R.id.btn_video_play)
    Button btn_video_play;
    @BindView(R.id.btn_voice_download)
    Button btn_voice_download;
    @BindView(R.id.iv_voice_play)
    Button iv_voice_play;

    private int id;

    @Override
    protected int onSetContentView() {
        return R.layout.layout_record_details;
    }

    @Override
    protected void onInitData() {
        setTitle(R.drawable.back,"详情页",0);

        id=getIntent().getIntExtra("RECORD_ID",0);

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

    /*日志详情*/
    private void loadDetails(){
        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executePost(HttpService.API_PATROLLOG_DETAIL+"?id="+id, map, new Novate.ResponseCallBack<Object>() {

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
                Log.i("cccccccccccc","res==="+originalResponse);
                RecordDetailsEntity recordDetailsEntity= BaseUtils.getGson().fromJson(originalResponse,RecordDetailsEntity.class);

                setData(recordDetailsEntity.getContent());
                progressLayout.showContent();
            }
        });
    }
    private String videoUrl,audioUrl;
    private void setData(RecordEntityData recordEntityData){

        if(ObjectUtils.isNotNull(recordEntityData.getVideoUrl())){
            btn_video_play.setVisibility(View.VISIBLE);
            videoUrl = recordEntityData.getVideoUrl();
        }

        if(ObjectUtils.isNotNull(recordEntityData.getAudioUrl())){
            audioUrl = recordEntityData.getAudioUrl();
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

        tv_xh_rw_mc.setText(recordEntityData.getTaskName());
        tv_xh_fw.setText(recordEntityData.getRivers());
        tv_xh_gj.setText(recordEntityData.getTrail());
        tv_xh_time.setText(recordEntityData.getPatrolTime());
        tv_fx_wt.setText(recordEntityData.getProblem());
        Document doc = Jsoup.parse(recordEntityData.getContent());
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }
        String html = doc.toString();

        content.loadData(html, "text/html; charset=UTF-8", null);
        addImg(recordEntityData.getFiles());
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

    /**
     * 添加图片
     *
     * @param imgs
     */
    private void addImg(List<RecordEntityData.FileContent> imgs) {
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
                ImageLoadUtils.loadImage(imgs.get(i).getFullName(), newImg);
            }
        }
    }

    private String getFileName(String path){
        String[] strs = path.split("/");
        String name = strs[strs.length-1];
        return name;
    }
}
