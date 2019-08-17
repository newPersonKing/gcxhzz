package com.whoami.gcxhzz.home1;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CommonTextActivity;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.base.activity.BaseUploadActivity;
import com.whoami.gcxhzz.base.app.MyApplication;
import com.whoami.gcxhzz.entity.CustomLocationMessageEntity;
import com.whoami.gcxhzz.entity.FileEntity;
import com.whoami.gcxhzz.entity.FileModuleEntity;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.picSelector.FullyGridLayoutManager;
import com.whoami.gcxhzz.picSelector.GridImageAdapter;
import com.whoami.gcxhzz.recorder.RecordAudioDialogFragment;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.DateUtil;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.until.SPUtils;
import com.whoami.gcxhzz.until.SoundPoolUtil;
import com.whoami.gcxhzz.until.Untils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MyRecordReportActivity extends BaseUploadActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;// 三张图片显示预览
    @BindView(R.id.tv_recycler_view)
    TextView tvRecyclerView;
    @BindView(R.id.fv_task_name)
    ForwardView fv_task_name;
    @BindView(R.id.fv_task_range)
    ForwardView fv_task_range;
    @BindView(R.id.fv_task_quesstion)
    ForwardView fv_task_quesstion;
    @BindView(R.id.fv_task_path)
    ForwardView fv_task_path;
    @BindView(R.id.fv_task_content)
    ForwardView fv_task_content;
    @BindView(R.id.fv_task_time_range)
    ForwardView fv_task_time_range;
    @BindView(R.id.fv_task_time)
    ForwardView fv_task_time;
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

    private final int REQUEST_CODE_CONTENT = 0x01;
    private final int REQUEST_CODE_SIGNATURE = 0x02;
    private final int REQUEST_CODE_XHCONTENT = 0x03;

    private AlActionSheetDialog task_rangeSheetDialog;

    private String riverCode;
    private String Rivers;
    private int state;/*0 未处理 10 处理完成*/
    private int TaskCode;

    private Map<String,Object> map=new HashMap<>();
    private String name;/*todo 巡河任务名称 只是显示 没有使用这个字段 todo*/
    private String TAG;
    @Override
    protected void beforeSetView() {
        Rivers= getIntent().getStringExtra("Rivers");
        riverCode=getIntent().getStringExtra("riverCode");
        state=getIntent().getIntExtra("State",-1);
        TaskCode=getIntent().getIntExtra("TaskCode",-1);
        name=getIntent().getStringExtra("name");
        TAG=getIntent().getStringExtra("TAG");
    }

    @Override
    protected int onSetContentView() {
        return R.layout.layout_record_report;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"巡河日志上传",0);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        //设置图片的最大数
        maxSelectNum = 3;
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);

        if ("Home1MapFragment".equals(TAG)){
            fv_task_name.setVisibility(View.GONE);
        }

        if (name!=null){
            fv_task_name.setRightText(name);
        }
        if ("Home1MapFragment".equals(TAG)){

        }else if (!ObjectUtils.isNull(Rivers)){
            fv_task_range.setRightText(Rivers);
        }


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
        task_rangeSheetDialog = new AlActionSheetDialog(mContext).builder();

        iv_voice_playDrawable = (AnimationDrawable) iv_voice_play.getDrawable();

        getHDData();
        setLocationMessage();
    }

    private void setLocationMessage(){
        List<CustomLocationMessageEntity> customLocationMessageEntities = ((MyApplication)getApplicationContext()).customLocationMessageEntities;
        String startTime = customLocationMessageEntities.get(0).getTime();
        String endTime = "";
        if(customLocationMessageEntities.size() == 1){
            /*如果只有一个点 代表没有移动*/
            endTime = DateUtil.getCurDateTime();
        }else{
             endTime = customLocationMessageEntities.get(customLocationMessageEntities.size()-1).getTime();
        }
        String startAdd = customLocationMessageEntities.get(0).getAddress();
        String endAdd = customLocationMessageEntities.get(customLocationMessageEntities.size()-1).getAddress();
        startTime = startTime.split(" ")[1];
        endTime = endTime.split(" ")[1];
        fv_task_time_range.setRightText(startTime+"-"+endTime);
        fv_task_path.setRightText(startAdd+"-"+endAdd);
        fv_task_time.setRightText(DateUtil.getCurDateTime());
        fv_task_range.setRightText(Rivers);
    }


    private boolean checkData(){

        if (ObjectUtils.isNull(fv_task_quesstion.getRightText())){
            Toast.makeText(this,"发现问题不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ObjectUtils.isNull(fv_task_path.getRightText())){
            Toast.makeText(this,"巡河轨迹不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ObjectUtils.isNull(fv_task_range.getRightText())){
            Toast.makeText(this,"巡河范围不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ObjectUtils.isNull(fv_task_content.getRightText())){
            Toast.makeText(this,"巡河内容不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void addRecord(){

        if (TaskCode!=-1){
            map.put("TaskCode",TaskCode);
        }

        map.put("Rivers", riverCode);

        String date=DateUtil.dateToDateString(new Date(System.currentTimeMillis()), DateUtil.yyyy_MM_dd_EN);
        map.put("PatrolTime",date);


        String patrolTime_quantum = fv_task_time_range.getRightText();
        map.put("PatrolTimeQuantum",patrolTime_quantum);

        map.put("PatrolTrail",getPointXY());

        map.put("problem",fv_task_quesstion.getRightText());

        if (fv_task_content.getRightText()!=null){
            map.put("content",fv_task_content.getRightText());
        }

        map.put("Trail",fv_task_path.getRightText());

        if (state!=-1){
            map.put("State",state);
        }

        HttpRequestUtils.getInstance().getNovate().executeBody(HttpService.API_PATROLLOG_ADD, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {}

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
                SoundPoolUtil.getInstance().play(MyRecordReportActivity.this,R.raw.end);
                setResult(300);
                finish();
            }
        });

    }

    private  List<String> getPointXY(){
        List<String> xys = new ArrayList<>();
        List<CustomLocationMessageEntity> customLocationMessageEntities = ((MyApplication)getApplicationContext()).customLocationMessageEntities;
        for(CustomLocationMessageEntity customLocationMessageEntity:customLocationMessageEntities){
            xys.add(customLocationMessageEntity.getX()+","+customLocationMessageEntity.getY());
        }
        return xys;
    }

    @Override
    protected void uploadSuccess(List<FileModuleEntity> files) {
        mProgressDialog.dismiss();
        List<Integer> ids=new ArrayList<>();
        for (FileModuleEntity fileModuleEntity:files){
            ids.add(fileModuleEntity.getId());
        }
        map.put("FileId",ids);
        addRecord();
    }

    @Override
    protected void uploadFailure() {
        mProgressDialog.dismiss();
        Toast.makeText(this,"上传文件失败",Toast.LENGTH_SHORT).show();
    }
    @OnClick({
            R.id.fv_task_quesstion,
            R.id.fv_task_path,
            R.id.fv_task_range,
            R.id.fv_task_content,
            R.id.btn_commit,
            R.id.btn_voice_click,
            R.id.btn_video_click,
            R.id.btn_voice_delete,
            R.id.btn_video_delete,
            R.id.ll_voice_play,
            R.id.iv_video_play
    })
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this, CommonTextActivity.class);
        super.onClick(v);
        switch (v.getId()){
            case R.id.fv_task_quesstion:

                intent.setClass(mContext, CommonTextActivity.class);
                intent.putExtra(CommonTextActivity.TITLE, "发现问题");
                intent.putExtra(CommonTextActivity.CONTENT, fv_task_quesstion.getRightText());
                intent.putExtra(CommonTextActivity.HINT, "请输入问题内容...");
                intent.putExtra(CommonTextActivity.MAX_NUM, 200);
                startActivityForResult(intent, REQUEST_CODE_CONTENT);
                break;
            case R.id.fv_task_path:
                /*这里设置的值从前一个页面 带过来*/
//                intent.setClass(mContext, CommonTextActivity.class);
//                intent.putExtra(CommonTextActivity.TITLE, "巡河轨迹");
//                intent.putExtra(CommonTextActivity.CONTENT, fv_task_path.getRightText());
//                intent.putExtra(CommonTextActivity.HINT, "请输入巡河轨迹...");
//                intent.putExtra(CommonTextActivity.MAX_NUM, 200);
//                startActivityForResult(intent, REQUEST_CODE_SIGNATURE);
                break;
            case R.id.fv_task_range:
                /*这里设置的值从前一个页面 带过来*/
//                task_rangeSheetDialog.show();
                break;
            case R.id.fv_task_content:
                intent.setClass(mContext, CommonTextActivity.class);
                intent.putExtra(CommonTextActivity.TITLE, "巡河内容");
                intent.putExtra(CommonTextActivity.CONTENT, fv_task_content.getRightText());
                intent.putExtra(CommonTextActivity.HINT, "请输入巡河内容...");
                intent.putExtra(CommonTextActivity.MAX_NUM, 200);
                startActivityForResult(intent, REQUEST_CODE_XHCONTENT);
                break;
            case R.id.btn_commit:
                if (!Untils.isFastClick()){
                    if (checkData()){
                        if (selectList.size()>0||!ObjectUtils.isNull(voice_path)||videoLocalMedia!=null){
                            mProgressDialog.setMessage("上传文件中..");
                            mProgressDialog.show();
                            postFiles();
                        }else {
                            mProgressDialog.setMessage("提交中...");
                            mProgressDialog.show();
                            addRecord();
                        }
                    }
                }
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CONTENT:
                if (RESULT_OK == resultCode) {
                    String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                    if (!ObjectUtils.isNull(res)) {
                        fv_task_quesstion.setRightText(res);
                    }
                }
                break;
            case REQUEST_CODE_SIGNATURE:
                if (RESULT_OK == resultCode) {
                    String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                    if (!ObjectUtils.isNull(res)) {
                        fv_task_path.setRightText(res);
                    }
                }
                break;
            case REQUEST_CODE_XHCONTENT:
                if (RESULT_OK == resultCode) {
                    String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                    if (!ObjectUtils.isNull(res)) {
                        fv_task_content.setRightText(res);
                    }
                }
                break;
            case PictureConfig.CHOOSE_REQUEST:
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
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
                    fv_task_range.setRightText(contentBean.getName());
                }
            });
        }
    }


    RecordAudioDialogFragment audioDialogFragment;
    private Long voice_duration;
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


    private final int audioResultCode=1111,videoResultCode=1112;
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
                    PictureSelector.create(MyRecordReportActivity.this).openCamera(PictureMimeType.ofVideo()).forResult(videoResultCode);
                }
            }
        });
    }
}
