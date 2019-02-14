/*
 * Copyright (c) EMCC 2015 All Rights Reserved
 */
package com.whoami.gcxhzz.picturepreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.until.ImageLoadUtils;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * <p/>
 * 。
 * <p/>
 * <p/>
 * 创建日期 2015年8月24日 下午1:19:57<br>
 *
 * @author 高炎<p>
 * @since 1.0.0
 */
public class SamplePagerAdapter extends RecyclingPagerAdapter {
    private String[] images = new String[]{};
    private OnPagerImageClickListener mListener;
    private Context context;

    public SamplePagerAdapter(Context context, String[] images, OnPagerImageClickListener lis) {
        this.context = context;
        mListener = lis;
        this.images = images;
    }

    public String getItem(int position) {
        return images[position];
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_preview, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final ProgressBar bar = vh.progress;
//        bar.setVisibility(View.GONE);
        // 图片路径
        final String imgUrl = images[position];
        //ImageLoader.getInstance().display(context, TextUtils.isEmpty(imgUrl) ? R.drawable.img_default_header : imgUrl, vh.image);
        ImageLoadUtils.loadImage(imgUrl, vh.image);

        // 图片单击事件
        vh.image.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mListener != null) {
                    mListener.onPagerClick();

                }
            }

        });
        vh.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(imgUrl);
                }
                return false;
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(imgUrl);
                }
                return false;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        PhotoView image;
        ProgressBar progress;

        ViewHolder(View view) {
            image = (PhotoView) view.findViewById(R.id.photoview);
            progress=view.findViewById(R.id.pb_loading);
        }
    }
}
