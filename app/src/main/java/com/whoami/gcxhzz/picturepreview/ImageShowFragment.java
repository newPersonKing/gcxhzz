package com.whoami.gcxhzz.picturepreview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.luck.picture.lib.photoview.OnPhotoTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.until.ImageLoadUtils;

public class ImageShowFragment extends Fragment {
    private OnPagerImageClickListener listener;// 长按保存图片会调接口
    private String imageUri;
    private PhotoView photoView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 网络图片地址
        imageUri = getArguments().getString("uri");
        listener = (ImagePreviewActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_imageshow, null);
        photoView = (PhotoView) view
                .findViewById(R.id.photoView_fragment_imageShow);
        linearLayout = (LinearLayout) view.findViewById(R.id.fuck_you);
        progressBar=view.findViewById(R.id.pb_loading);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(imageUri);
                return false;
            }
        });

        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                listener.onPagerClick();
            }
        });

//        ImageLoadUtils.loadImage(imageUri, photoView);
          ImageLoadUtils.loadImageWithListerner(imageUri, photoView, new ImageLoadUtils.LoadInterface() {
              @Override
              public void loadingFinish() {
                  progressBar.setVisibility(View.GONE);
              }
          });
        return view;
    }

}
