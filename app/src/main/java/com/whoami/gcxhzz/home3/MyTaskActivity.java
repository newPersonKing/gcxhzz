package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.algorithm.android.utils.AlRecyclerViewDecoration;
import com.algorithm.progresslayoutlibrary.ProgressLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.base.activity.BaseTitleActivity;
import com.whoami.gcxhzz.entity.TaskEntity;
import com.whoami.gcxhzz.entity.TaskEntityData;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*任务列表*/
public class MyTaskActivity extends BaseTitleActivity{

    private BaseQuickAdapter mAdapter;

    @BindView(R.id.task_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    private List<TaskEntityData> entities = new ArrayList<>();
    private int page=1;
    private int size=10;
    @Override
    protected int onSetContentView() {
        return R.layout.layout_task;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"我的巡河任务",0);

        mAdapter = new BaseQuickAdapter<TaskEntityData, BaseViewHolder>(R.layout.item_event, entities) {
            @Override
            protected void convert(BaseViewHolder helper, final TaskEntityData item) {
                helper.getView(R.id.iv_event_urgency).setVisibility(View.VISIBLE);
                if ("紧急".equals(item.getUrgency())){
                    helper.setImageResource(R.id.iv_event_urgency,R.mipmap.state_urgency);
                }else if ("一般".equals(item.getUrgency())){
                    helper.setImageResource(R.id.iv_event_urgency,R.mipmap.general);
                }
                helper.setText(R.id.tv_event_scope,item.getName());
                helper.getView(R.id.iv_event_state).setVisibility(View.VISIBLE);
                /*巡河任务 state 3 已巡河  其他待巡河*/
                if (item.getState()==3){
                    helper.setImageResource(R.id.iv_event_state,R.mipmap.state_yxh);
                }else {
                    helper.setImageResource(R.id.iv_event_state,R.mipmap.state_dxh);
                }

                helper.getView(R.id.tv_fanwei).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_fanwei,"巡河范围："+item.getRivers());

                helper.getView(R.id.tv_laiyuan).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_laiyuan,"任务来源："+item.getSource());

                helper.getView(R.id.tv_leixing).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_leixing,"任务类型："+item.getType());

                helper.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_time,"要求巡查日期："+item.getPatrolDate()+"");

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext,MyTaskDetailsActivity.class);
                        intent.putExtra("TASK_ID",item.getCode());
                        startActivity(intent);
                    }
                });
            }
        };


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new AlRecyclerViewDecoration(getResources(), R.color.window_background, R
                .dimen.dp_10, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                 loadDatas();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
               page=1;
               entities.clear();
               loadDatas();
            }
        });

        progressLayout.showLoading();

        progressLayout.setOnErrorClickListener(new ProgressLayout.OnErrorClickListener() {
            @Override
            public void onClick() {
                progressLayout.showLoading();
                loadDatas();
            }
        });

        loadDatas();
    }
    /*加载我的逊河任务*/
    private void loadDatas(){

        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executePost(HttpService.API_TASK_LIST+"?page="+page+"&&size="+size, map, new Novate.ResponseCallBack<Object>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {
                progressLayout.showContent();
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
                TaskEntity taskEntity=BaseUtils.getGson().fromJson(originalResponse,TaskEntity.class);

                TaskEntity.ContentBean data=taskEntity.getContent();

                if (data.getTotalCount()>entities.size()){
                    entities.addAll(data.getData());
                    page++;
                    mAdapter.notifyDataSetChanged();
                    refreshLayout.finishLoadmore();
                }else if (data.getTotalCount()==0){
                    entities.clear();
                    mAdapter.notifyDataSetChanged();
                }else {
                    refreshLayout.finishLoadmoreWithNoMoreData();
                }
                refreshLayout.finishRefresh();
                mAdapter.notifyDataSetChanged();
                progressLayout.showContent();
            }
        });

    }

}
