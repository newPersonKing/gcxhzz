package com.whoami.gcxhzz.home1;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorithm.android.widget.dialog.AlActionSheetDialog;
import com.algorithm.android.widget.item.ForwardView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.FileUtil;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CommonTextActivity;
import com.whoami.gcxhzz.base.activity.BaseUploadActivity;
import com.whoami.gcxhzz.entity.FileModuleEntity;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.home3.MyEventActivity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.picSelector.FullyGridLayoutManager;
import com.whoami.gcxhzz.picSelector.GridImageAdapter;
import com.whoami.gcxhzz.recorder.RecordAudioDialogFragment;
import com.whoami.gcxhzz.recorder.RecordingService;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.until.SPUtils;
import com.whoami.gcxhzz.until.Untils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MyEventRepotActivity extends BaseUploadActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;// 三张图片显示预览

    @BindView(R.id.tv_recycler_view)
    TextView tvRecyclerView;
    @BindView(R.id.fv_event_name)
    ForwardView fv_event_name;
    @BindView(R.id.fv_event_urgency)
    ForwardView fv_event_urgency;
    @BindView(R.id.fv_event_source)
    ForwardView fv_event_source;
    @BindView(R.id.fv_event_content)
    ForwardView fv_event_content;
    @BindView(R.id.fv_event_range)
    ForwardView fv_event_range;
    @BindView(R.id.iv_voice_play)
    ImageView iv_voice_play;
    @BindView(R.id.tv_voice_length)
    TextView tv_voice_length;
    @BindView(R.id.ll_voice_play)
    LinearLayout ll_voice_play;
    @BindView(R.id.btn_voice_click)
    TextView btn_voice_click;
    @BindView(R.id.iv_video_play)
    ImageView iv_video_play;

    AnimationDrawable iv_voice_playDrawable;
    private AlActionSheetDialog event_urgencySheetDialog;
    private AlActionSheetDialog event_sourceSheetDialog;
    private AlActionSheetDialog task_rangeSheetDialog;

    private final int REQUEST_CODE_CONTENT = 0x01;
    private final int REQUEST_CODE_NAME = 0x02;

    private int EventSorce=-1;
    private int urgency=-1;
    private String riverCode;
    private String Rivers;
    Map<String,Object> map=new HashMap<>();

    private final int audioResultCode=1111,videoResultCode=1112;

    private Long voice_duration;
    @Override
    protected int onSetContentView() {
        return R.layout.layout_event_report;
    }

    @Override
    protected void onInitData() {
        setTitle(R.drawable.back,"事件上报",0);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        //设置图片的最大数
        maxSelectNum = 3;
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);


        super.onAddPicHintShowListener = new GridImageAdapter.OnAddPicHintShowListener() {
            @Override
            public void onResume() {
                if(selectList.size()>0){
                    tvRecyclerView.setVisibility(View.GONE);
                }else{
                    tvRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onDeletePicClick() {
                if(selectList.size()>0){
                    tvRecyclerView.setVisibility(View.GONE);
                }else{
                    tvRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        };

        event_urgencySheetDialog = new AlActionSheetDialog(mContext).builder()
                .addSheetItem("一般", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_urgency.setRightText("一般");
                        urgency=1;

                    }
                }).addSheetItem("紧急", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_urgency.setRightText("紧急");
                        urgency=2;
                    }
                });
        event_sourceSheetDialog = new AlActionSheetDialog(mContext).builder()
                .addSheetItem("巡查河湖", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_source.setRightText("巡查河湖");
                        EventSorce=10;

                    }
                }).addSheetItem("督导检查", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_source.setRightText("督导检查");
                        EventSorce=20;

                    }
                }).addSheetItem("遥感监测", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_source.setRightText("遥感监测");
                        EventSorce=30;

                    }
                }).addSheetItem("社会监督", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_event_source.setRightText("社会监督");
                        EventSorce=40;
                    }
                });


        task_rangeSheetDialog = new AlActionSheetDialog(mContext).builder();

        iv_voice_playDrawable = (AnimationDrawable) iv_voice_play.getDrawable();


        getHDData();
    }

    @Override
    protected void uploadSuccess(List<FileModuleEntity> fileModuleEntities) {
        mProgressDialog.dismiss();
        List<Integer> fileId=new ArrayList<>();
        for (FileModuleEntity fileModuleEntity:fileModuleEntities){
            fileId.add(fileModuleEntity.getId());
        }
        Log.i("ccccccccc","fileId=="+fileId);
        map.put("fileId",fileId);
        addEventReport();
    }

    @Override
    protected void uploadFailure() {

    }
    RecordAudioDialogFragment audioDialogFragment;
    @OnClick({
            R.id.fv_event_urgency,
            R.id.fv_event_source,
            R.id.fv_event_content,
            R.id.fv_event_range,
            R.id.btn_commit,
            R.id.fv_event_name,
            R.id.btn_voice_click,
            R.id.btn_video_click,
            R.id.btn_voice_delete,
            R.id.btn_video_delete,
            R.id.ll_voice_play,
            R.id.iv_video_play
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);

        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.fv_event_urgency:
                event_urgencySheetDialog.show();
                break;
            case R.id.fv_event_source:
                event_sourceSheetDialog.show();
                break;
            case R.id.fv_event_content:
                intent.setClass(mContext, CommonTextActivity.class);
                intent.putExtra(CommonTextActivity.TITLE, "事件内容");
                intent.putExtra(CommonTextActivity.CONTENT, fv_event_content.getRightText());
                intent.putExtra(CommonTextActivity.HINT, "请输入事件内容...");
                intent.putExtra(CommonTextActivity.MAX_NUM, 200);
                startActivityForResult(intent, REQUEST_CODE_CONTENT);
                break;
            case R.id.fv_event_range:
                task_rangeSheetDialog.show();
                break;
            case R.id.btn_commit:
                if (!Untils.isFastClick()){
                    if (checkData()){
                        if (selectList.size()>0||!ObjectUtils.isNull(voice_path)||videoLocalMedia!=null){
                            mProgressDialog.setMessage("上传文件中...");
                            mProgressDialog.show();
                            postFiles();
                        }else{
                            mProgressDialog.setMessage("提交中...");
                            mProgressDialog.show();
                            addEventReport();
                        }
                    }
                }
                break;
            case R.id.fv_event_name:
                intent.setClass(mContext, CommonTextActivity.class);
                intent.putExtra(CommonTextActivity.TITLE, "事件名称");
                intent.putExtra(CommonTextActivity.CONTENT, fv_event_content.getRightText());
                intent.putExtra(CommonTextActivity.HINT, "请输入事件名称...");
                intent.putExtra(CommonTextActivity.MAX_NUM, 200);
                startActivityForResult(intent, REQUEST_CODE_NAME);
                break;
            case R.id.btn_voice_click:
                requestVoicePermissions();
                break;
            case R.id.btn_video_click:
                requestVideoPermissions();
                break;
            case R.id.btn_video_delete:
                File voideo_file = new File(videoLocalMedia.getPath());
                if(voideo_file.exists()){
                    boolean result = voideo_file.delete();
                    videoLocalMedia = null;
                    iv_video_play.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_video_play:
                if(videoLocalMedia!=null){
                    PictureSelector.create(this).externalPictureVideo(videoLocalMedia.getPath());
                }
                break;
            case R.id.ll_voice_play:
                iv_voice_playDrawable.start();
                if(!ObjectUtils.isNull(voice_path)){
                    Play(voice_path);
                }
                break;
            case R.id.btn_voice_delete:
                File voice_file = new File(voice_path);
                if(voice_file.exists()){
                    boolean result = voice_file.delete();
                    ll_voice_play.setVisibility(View.GONE);
                    voice_path = "";
                }
                break;
        }
    }

    private void addEventReport(){

        map.put("eventTitle",fv_event_name.getRightText());
        map.put("source",fv_event_source.getRightText());
        map.put("urgency",urgency);
        map.put("content",fv_event_content.getRightText());
        /*todo 这两个参数暂时不知道哪来的*/
        map.put("riverCode",riverCode);
//        map.put("logId",1);

        HttpRequestUtils.getInstance().getNovate().executeBody(HttpService.API_EVENTMANAGE_ADD, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private boolean checkData(){
        if (EventSorce==-1){
            Toast.makeText(this,"事件来源不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (urgency==-1){
            Toast.makeText(this,"紧急程度不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ObjectUtils.isNull(fv_event_name.getRightText())){
            Toast.makeText(this,"事件名称不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ObjectUtils.isNull(fv_event_content.getRightText())){
            Toast.makeText(this,"事件名称不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*获取河段信息*/
    private void getHDData(){
        Map<String,Object> map=new HashMap<>();
        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_RIVERBASEINFO_GET, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {}

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                RiverEntity taskEntity= BaseUtils.getGson().fromJson(originalResponse,RiverEntity.class);
                createDialog(taskEntity.getContent());

            }
        });
    }

    private void createDialog(List<RiverEntity.ContentBean> datas){

        for (final RiverEntity.ContentBean contentBean:datas){

            task_rangeSheetDialog.addSheetItem(contentBean.getName(), AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    riverCode=contentBean.getCode();
                    fv_event_range.setRightText(contentBean.getName());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CONTENT:
                if (RESULT_OK == resultCode) {
                    String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                    if (!ObjectUtils.isNull(res)) {
                        fv_event_content.setRightText(res);
                    }
                }
                break;
            case REQUEST_CODE_NAME:
                String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                if (!ObjectUtils.isNull(res)) {
                    fv_event_name.setRightText(res);
                }
                break;
            case PictureConfig.CHOOSE_REQUEST:
                selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null) {
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                }
                break;
            case audioResultCode:/*调用系统录音*/
//                List<LocalMedia> voiceSelectList = PictureSelector.obtainMultipleResult(data);
//                if(voiceSelectList.size()>0){
//                    voiceLocalMedia = voiceSelectList.get(0);
//                    ll_voice_play.setVisibility(View.VISIBLE);
//                    tv_voice_length.setText(voiceLocalMedia.getDuration()%1000+"秒");
//                }
                break;
            case videoResultCode:
                List<LocalMedia> videosSelectList = PictureSelector.obtainMultipleResult(data);
                if(videosSelectList.size()>0){
                    videoLocalMedia = videosSelectList.get(0);
                    iv_video_play.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(videoLocalMedia.getPath()).into(iv_video_play);
                }
                break;
        }
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
                    iv_voice_playDrawable.stop();
                    mediaPlayer.reset();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestVoicePermissions(){
        if(rxPermissions == null){
            rxPermissions = new RxPermissions(this);
        }

        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                    if(aBoolean){
                        audioDialogFragment = RecordAudioDialogFragment.newInstance();
                        audioDialogFragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                        audioDialogFragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                            @Override
                            public void onCancel() {
                                audioDialogFragment.dismiss();
                                voice_path = (String) SPUtils.getInstance().get("audio_path","");
                                voice_duration = (Long) SPUtils.getInstance().get("elpased",0L);
                                Long minRes = voice_duration/1000;
                                tv_voice_length.setText(minRes+"秒");
                                if(ObjectUtils.isNotNull(voice_path)){
                                    ll_voice_play.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
            }
        });
    }


    private void requestVideoPermissions(){
        if(rxPermissions == null){
            rxPermissions = new RxPermissions(this);
        }

        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean){
                    PictureSelector.create(MyEventRepotActivity.this).openCamera(PictureMimeType.ofVideo()).forResult(videoResultCode);
                }
            }
        });
    }
}
