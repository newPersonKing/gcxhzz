package com.whoami.gcxhzz.home1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.algorithm.android.widget.dialog.AlActionSheetDialog;
import com.algorithm.android.widget.item.ForwardView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.activity.CommonTextActivity;
import com.whoami.gcxhzz.base.activity.BaseActivity;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.BaseUserEntity;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.DateUtil;
import com.whoami.gcxhzz.until.ObjectUtils;
import com.whoami.gcxhzz.until.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class IssuseTaskActivity extends BaseTitleActivity {

    @BindView(R.id.et_task_name)
    EditText et_task_name;
    @BindView(R.id.fv_task_type)
    ForwardView fv_task_type;
    @BindView(R.id.fv_task_urgency)
    ForwardView fv_task_urgency;
    @BindView(R.id.fv_task_source)
    ForwardView fv_task_source;
    @BindView(R.id.fv_task_range)
    ForwardView fv_task_range;
    @BindView(R.id.fv_task_people)
    ForwardView fv_task_people;
    @BindView(R.id.fv_task_time)
    ForwardView fv_task_time;
    @BindView(R.id.fv_task_content)
    ForwardView fv_task_content;

    private AlActionSheetDialog task_typeSheetDialog;
    private AlActionSheetDialog task_urgencySheetDialog;
    private AlActionSheetDialog task_sourceSheetDialog;
    private AlActionSheetDialog task_river_sheetDialog;
    private AlActionSheetDialog task_people_sheetDialog;


    private Integer urgency;
    private Integer task_source;
    private Integer task_type;
    @Override
    protected int onSetContentView() {
        return R.layout.activity_issuse_task;
    }

    @Override
    protected void onInitData() {
        setTitle(R.drawable.back,"任务下发",0);

        task_typeSheetDialog = new AlActionSheetDialog(mContext).builder()
                .addSheetItem("常规任务", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        task_type = 1;
                        fv_task_type.setRightText("常规任务");
                    }
                })
                .addSheetItem("督导检查任务", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        task_type = 2;
                        fv_task_type.setRightText("督导检查任务");
                    }
                })
                .addSheetItem("事件处理", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        task_type = 3;
                        fv_task_type.setRightText("事件处理");
                    }
                });

        task_urgencySheetDialog = new AlActionSheetDialog(mContext).builder()
                .addSheetItem("一般", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_urgency.setRightText("一般");
                        urgency=1;

                    }
                }).addSheetItem("紧急", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_urgency.setRightText("紧急");
                        urgency=2;
                    }
                });

        /*todo task_source 对应的id 修改了 */
        task_sourceSheetDialog = new AlActionSheetDialog(mContext).builder()
                .addSheetItem("督导检查", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_source.setRightText("督导检查");
                        task_source=1;

                    }
                }).addSheetItem("遥感监测", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_source.setRightText("遥感监测");
                        task_source=2;

                    }
                }).addSheetItem("社会监督", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_source.setRightText("社会监督");
                        task_source=3;

                    }
                }).addSheetItem("相关系统推送", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog
                        .OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        fv_task_source.setRightText("相关系统推送");
                        task_source=4;
                    }
                });

        task_river_sheetDialog = new AlActionSheetDialog(mContext).builder();
        getHDData();

        task_people_sheetDialog = new AlActionSheetDialog(mContext).builder();
        getPeople();
    }

    @OnClick({
            R.id.iv_left,
            R.id.fv_task_type,
            R.id.fv_task_people,
            R.id.fv_task_range,
            R.id.fv_task_source,
            R.id.fv_task_time,
            R.id.fv_task_urgency,
            R.id.fv_task_content,
            R.id.btn_commit
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.fv_task_type:
                task_typeSheetDialog.show();
                break;
            case R.id.fv_task_urgency:
                task_urgencySheetDialog.show();
                break;
            case R.id.fv_task_range:
                task_river_sheetDialog.show();
                break;
            case R.id.fv_task_people:
                task_people_sheetDialog.show();
                break;
            case R.id.fv_task_source:
                task_sourceSheetDialog.show();
                break;
            case R.id.fv_task_time:
                showSelectTime();
                break;
            case R.id.fv_task_content:
                addContent();
                break;
            case R.id.btn_commit:
                addTask();
                break;

        }
    }

    private void addTask(){

        String name = et_task_name.getText().toString();
        if(ObjectUtils.isNull(name)){
            Toast.makeText(this,"任务名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        checkData();


        String content = fv_task_content.getRightText();
        if(ObjectUtils.isNull(content)){
            Toast.makeText(this,"巡河内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,Object> map = new HashMap<>();

        map.put("Name",name);
        map.put("Type",task_type);
        map.put("Urgency",urgency);
        map.put("Source",task_source);
        map.put("Rivers",riverCode);
        map.put("Content",content);
        map.put("PatrolUser",userId);
        map.put("PatrolDate",time);

        HttpRequestUtils.getInstance().getNovate().rxBody(HttpService.ADD_TASK, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("succeeded");
                    if(success){
                        finish();
                        Toast.makeText(IssuseTaskActivity.this,"下发成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(IssuseTaskActivity.this,"下发失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    private void checkData(){
        if(ObjectUtils.isNull(task_type)){
            Toast.makeText(this,"任务类型不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ObjectUtils.isNull(urgency)){
            Toast.makeText(this,"紧急程度不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ObjectUtils.isNull(task_source)){
            Toast.makeText(this,"任务来源不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ObjectUtils.isNull(riverCode)){
            Toast.makeText(this,"巡河范围不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ObjectUtils.isNull(userId)){
            Toast.makeText(this,"巡河人员不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ObjectUtils.isNull(time)){
            Toast.makeText(this,"巡河时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private String riverCode;
    private List<String> riverCodes=new ArrayList<>();
    /*获取河段信息*/
    private void getHDData(){


        Map<String,Object> map=new HashMap<>();
        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_RIVERBASEINFO_GET, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onSuccess(int code, String msg, Object response, String originalResponse) throws JSONException {
                RiverEntity taskEntity=BaseUtils.getGson().fromJson(originalResponse,RiverEntity.class);
                createDialog(taskEntity.getContent());
            }
        });
    }

    private void createDialog(List<RiverEntity.ContentBean> datas){

        for (final RiverEntity.ContentBean contentBean:datas){
            riverCodes.add(contentBean.getCode());
            task_river_sheetDialog.addSheetItem(contentBean.getName(), AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    riverCode=contentBean.getCode();
                    fv_task_range.setRightText(contentBean.getName());
                }
            });
        }
    }

    /*查询所有的巡河人员*/
    private void getPeople(){
        Map<String,Object> map = new HashMap<>();
        HttpRequestUtils.getInstance().getNovate().rxGet(HttpService.GET_USER_LIST, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                BaseUserEntity baseUserEntity = BaseUtils.getGson().fromJson(response,BaseUserEntity.class);
                createUserDialog(baseUserEntity);
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
    private Long userId;
    private void createUserDialog(BaseUserEntity baseUserEntity){
        List<BaseUserEntity.TaskUser> taskUsers = baseUserEntity.getContent();
        for(final BaseUserEntity.TaskUser taskUser:taskUsers){
            task_people_sheetDialog.addSheetItem(taskUser.getRealName(), AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    userId=taskUser.getUserId();
                    fv_task_people.setRightText(taskUser.getRealName());
                }
            });
        }
    }

    private String time;
    private void showSelectTime(){
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                time = DateUtil.dateToDateString(date,DateUtil.yyyy_MM_dd_EN);
                fv_task_time.setRightText(time);
            }
        }).build();
        pvTime.show();
    }
    private final int REQUEST_CODE_CONTENT = 0x01;
    private void addContent(){
        Intent intent = new Intent();
        intent.setClass(mContext, CommonTextActivity.class);
        intent.putExtra(CommonTextActivity.TITLE, "巡河内容");
        intent.putExtra(CommonTextActivity.CONTENT, fv_task_content.getRightText());
        intent.putExtra(CommonTextActivity.HINT, "请输入巡河内容...");
        intent.putExtra(CommonTextActivity.MAX_NUM, 200);
        startActivityForResult(intent, REQUEST_CODE_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CONTENT:
                if (RESULT_OK == resultCode) {
                    String res = ObjectUtils.isNull(data) ? "" : data.getExtras().getString(CommonTextActivity.CONTENT_RESULT);
                    if (!ObjectUtils.isNull(res)) {
                        fv_task_content.setRightText(res);
                    }
                }
                break;
        }
    }
}
