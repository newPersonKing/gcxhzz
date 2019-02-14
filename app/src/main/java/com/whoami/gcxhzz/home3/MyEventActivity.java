package com.whoami.gcxhzz.home3;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.whoami.gcxhzz.entity.EventEntity;
import com.whoami.gcxhzz.entity.EventEntityData;
import com.whoami.gcxhzz.entity.TaskEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/*事件列表*/
public class MyEventActivity extends BaseTitleActivity{

    @BindView(R.id.event_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    private BaseQuickAdapter mAdapter;

    private List<EventEntityData> entities = new ArrayList<>();
    @Override
    protected int onSetContentView() {
        return R.layout.layout_event;
    }

    private int page=1;
    private int size=10;
    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"我的事件上报",0);

        mAdapter = new BaseQuickAdapter<EventEntityData, BaseViewHolder>(R.layout.item_event, entities) {
            @Override
            protected void convert(BaseViewHolder helper, final EventEntityData item) {
                helper.getView(R.id.iv_event_urgency).setVisibility(View.VISIBLE);
                if (item.getUrgency()==1){
                    helper.setImageResource(R.id.iv_event_urgency,R.mipmap.general);
                }else if (item.getUrgency()==2){
                    helper.setImageResource(R.id.iv_event_urgency,R.mipmap.state_urgency);
                }

                helper.setText(R.id.tv_event_scope,item.getEventTitle());

                helper.getView(R.id.iv_event_state).setVisibility(View.VISIBLE);
                if (item.getState()==0){
                    helper.setImageResource(R.id.iv_event_state,R.mipmap.ic_activity_state_jx);
                }else if (item.getState()==1){
                    helper.setImageResource(R.id.iv_event_state,R.mipmap.state_completed);
                }

                helper.getView(R.id.tv_laiyuan).setVisibility(View.VISIBLE);
                String source="";
                if (item.getSource()==10){
                    source="巡查河湖";
                }else if (item.getSource()==20){
                    source="督导检查";
                }else if (item.getSource()==30){
                    source="遥感监测";
                }else {
                    source="社会监督";
                }
                helper.setText(R.id.tv_laiyuan,"事件来源："+source);


                helper.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_time,"上报时间："+item.getReportTime()+"");

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext,MyEventDetailsActivity.class);
                        intent.putExtra("EVENT_ID",item.getEventId());
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

    private void loadDatas(){

        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executeGet(HttpService.API_EVENT_MANAGE_MY+"?page="+page+"&&size="+size, map, new Novate.ResponseCallBack<Object>() {

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
                EventEntity eventEntity= BaseUtils.getGson().fromJson(originalResponse,EventEntity.class);
                EventEntity.ContentBean data=eventEntity.getContent();
                if (data.getTotalCount()>entities.size()){
                    entities.addAll(eventEntity.getContent().getData());
                    mAdapter.notifyDataSetChanged();
                    page++;
                    refreshLayout.finishLoadmore();
                }else if (data.getTotalCount()==0){
                    entities.clear();
                    mAdapter.notifyDataSetChanged();
                }else {
                    refreshLayout.finishLoadmoreWithNoMoreData();
                }
                refreshLayout.finishRefresh();
                progressLayout.showContent();
            }
        });

    }

}
