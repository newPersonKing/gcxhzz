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
import com.whoami.gcxhzz.entity.RecordEntity;
import com.whoami.gcxhzz.entity.RecordEntityData;
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

/*我的日志列表*/
public class MyRecordActivity extends BaseTitleActivity{

    private BaseQuickAdapter mAdapter;

    @BindView(R.id.record_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;

    private int page=1;
    private int size=15;

    private List<RecordEntityData> entities = new ArrayList<>();
    @Override
    protected int onSetContentView() {
        return R.layout.layout_record;
    }

    @Override
    protected void onInitData() {

        setTitle(R.drawable.back,"我的巡河日志",0);


        mAdapter = new BaseQuickAdapter<RecordEntityData, BaseViewHolder>(R.layout.item_event_list, entities) {
            @Override
            protected void convert(BaseViewHolder helper, final RecordEntityData item) {

                helper.getView(R.id.tv_laiyuan).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_laiyuan,"巡河范围："+item.getRivers());

                helper.getView(R.id.tv_leixing).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_leixing,"巡河轨迹："+item.getTrail());

                helper.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_time,"巡河时间："+item.getPatrolTime()+"");

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext,MyRecordDetailsActivity.class);
                        intent.putExtra("RECORD_ID",item.getCode());
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
               entities.clear();
               page=1;
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

    /*加载我的巡河日志*/
    private void loadDatas(){

        Map<String,Object> map=new HashMap<>();

        HttpRequestUtils.getInstance().getNovate().executePost(HttpService.API_PATROLLOG_LIST+"?page="+page+"&&size="+size, map, new Novate.ResponseCallBack<Object>() {

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
                RecordEntity recordEntity= BaseUtils.getGson().fromJson(originalResponse,RecordEntity.class);
                RecordEntity.ContentBean data=recordEntity.getContent();
                if (data.getTotalCount()>entities.size()){
                    entities.addAll(recordEntity.getContent().getData());
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
