package com.whoami.gcxhzz.recorder;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.whoami.gcxhzz.R;

public class FinishDialogFragment extends DialogFragment {

    private CallBack callBack;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_finish_dialog, null);
        initView(view);


        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    public void setCallBacl(CallBack callBack){
        this.callBack = callBack;
    }

    private void initView(View view){

        TextView tv_sure = view.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.onClickSure();
                }
            }
        });

        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.onClickCancle();
                }
            }
        });
    }

   public interface CallBack{

        void onClickSure();

        void onClickCancle();

   }
}
