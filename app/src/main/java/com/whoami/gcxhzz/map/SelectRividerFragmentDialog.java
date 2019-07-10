package com.whoami.gcxhzz.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.entity.RiverEntity;
import com.whoami.gcxhzz.entity.SelectRiverEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.until.BaseUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectRividerFragmentDialog extends DialogFragment {

    private CallBackInterFace  callBackInterFace;
    private List<RiverEntity.ContentBean> contentBeans = new ArrayList<>();/*用来保存原始数据*/
    private RiverEntity.ContentBean contentBean;
    public static SelectRividerFragmentDialog getInstance(){
        SelectRividerFragmentDialog selectRividerFragmentDialog = new SelectRividerFragmentDialog();
        return selectRividerFragmentDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialong_select_river, null);
        initView(view);
        builder.setCancelable(false);
        builder.setView(view);
        setRetainInstance(true);
        return builder.create();
    }



    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;

    private String riverName;
    private void initView(View view){
        Button btn_sure = view.findViewById(R.id.btn_sure);
        Button btn_cancle = view.findViewById(R.id.btn_cancle);
        EditText et_search_text = view.findViewById(R.id.et_search_text);
        et_search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  refreshData(s.toString());
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackInterFace!=null){
                    callBackInterFace.onClickCancle();
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackInterFace!=null){
                    callBackInterFace.onClickSure(contentBean);
                }
            }
        });

        recyclerView = view.findViewById(R.id.rv_river_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<RiverEntity.ContentBean,BaseViewHolder>(R.layout.item_select_river) {

            @Override
            protected void convert(final BaseViewHolder helper, RiverEntity.ContentBean item) {
                final int position = helper.getAdapterPosition();
                final List<RiverEntity.ContentBean> data = adapter.getData();
                RiverEntity.ContentBean contentBean = data.get(position);

                helper.setText(R.id.tv_river_name,contentBean.getName());

                CheckBox checkBox = helper.getView(R.id.cb_select);
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(contentBean.isSelect());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            refreshState(position);
                        }else {
                            data.get(position).setSelect(false);
                        }
                    }
                });
            }
        };


        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void refreshState(int position){
        List<RiverEntity.ContentBean> data = adapter.getData();
        for(int i=0;i<data.size();i++){
            if(i==position){
                data.get(i).setSelect(true);
                riverName = data.get(i).getName();
                contentBean = data.get(i);
            }else {
                data.get(i).setSelect(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshData(String searchText){
        List<RiverEntity.ContentBean> nData = new ArrayList<>();
        for(RiverEntity.ContentBean contentBean:contentBeans){
            if(contentBean.getName().contains(searchText)){
                nData.add(contentBean);
            }
        }
        adapter.setNewData(nData);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(contentBeans.size()==0){
            getHDData();
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
                contentBeans.clear();
                contentBeans.addAll(taskEntity.getContent());
                adapter.setNewData(contentBeans);
            }
        });
    }

    public void setCallBackInterFace(CallBackInterFace callBackInterFace){
        this.callBackInterFace = callBackInterFace;
    }

    public interface CallBackInterFace{
        void onClickSure(RiverEntity.ContentBean contentBean);
        void onClickCancle();
    }
}
