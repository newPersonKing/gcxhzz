package com.whoami.gcxhzz.home1;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.algorithm.android.widget.dialog.AlActionSheetDialog;
import com.algorithm.android.widget.item.ForwardView;
import com.algorithm.android.widget.item.TextEditItemView;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CommonTextActivity;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.base.activity.BaseUploadActivity;
import com.whoami.gcxhzz.entity.FileEntity;
import com.whoami.gcxhzz.entity.FileModuleEntity;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.picSelector.FullyGridLayoutManager;
import com.whoami.gcxhzz.picSelector.GridImageAdapter;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.until.Untils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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
                if(selectMedia.size()>0){
                    tvRecyclerView.setVisibility(View.GONE);
                }else{
                    tvRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onDeletePicClick() {
                if(selectMedia.size()>0){
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
        getHDData();
    }

    @Override
    protected void uploadSuccess(List<FileModuleEntity> fileModuleEntities) {
        mProgressDialog.dismiss();
        List<Integer> fileId=new ArrayList<>();
        for (FileModuleEntity fileModuleEntity:fileModuleEntities){
            fileId.add(fileModuleEntity.getId());
        }
        map.put("fileId",fileId);
        addEventReport();
    }

    @Override
    protected void uploadFailure() {

    }

    @OnClick({
            R.id.fv_event_urgency,
            R.id.fv_event_source,
            R.id.fv_event_content,
            R.id.fv_event_range,
            R.id.btn_commit,
            R.id.fv_event_name
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
                        if (selectMedia.size()>0){
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
                intent.putExtra(CommonTextActivity.MAX_NUM, 45);
                startActivityForResult(intent, REQUEST_CODE_NAME);
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
        }
    }
}
