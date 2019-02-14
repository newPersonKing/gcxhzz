/*
 * Copyright (c) EMCC 2015 All Rights Reserved
 */
package com.whoami.gcxhzz.picturepreview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorithm.android.widget.dialog.AlActionSheetDialog;
import com.whoami.gcxhzz.R;
import com.whoami.gcxhzz.until.FileUtils;


/**
 * <p>
 * 。
 * <p>
 *
 * 创建日期 2015年8月24日 下午1:12:23<br>
 * @author  高炎<p>
 * @since  1.0.0
 */
public class ImagePreviewActivity extends FragmentActivity implements OnPageChangeListener, OnPagerImageClickListener {
    public static final String TAG = ImagePreviewActivity.class.getSimpleName();
    public static final String BUNDLE_KEY_IMAGES = "bundle_key_images";
    public static final String BUNDLE_KEY_INDEX = "bundle_key_index";
    @SuppressWarnings("unused")
    private static final String IMAGE_PREVIEW_SCREEN = "image_preview_screen";
    private HackyViewPager mViewPager;
    private RelativeLayout image_title_view;
    private SamplePagerAdapter mAdapter;
    private int mCurrentPostion = 0;
    private String[] mImageUrls;
    private ImageView back;
    private TextView image_num;
    // 是否显示头部标题
    public static boolean isShowTitle = true;
    
    private ImageShowAdapter adapter;

    AlActionSheetDialog saveBitmapDialog;

    private String saveImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        image_title_view = (RelativeLayout) findViewById(R.id.image_title_view);
        back = (ImageView) findViewById(R.id.back);
        image_num = (TextView) findViewById(R.id.image_num);
        mImageUrls = getIntent().getStringArrayExtra(BUNDLE_KEY_IMAGES);
        int index = getIntent().getIntExtra(BUNDLE_KEY_INDEX, 0);
        mAdapter = new SamplePagerAdapter(this, mImageUrls, this);//
        
        adapter = new ImageShowAdapter(getSupportFragmentManager(), mImageUrls);
        
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(index);
        onPageSelected(index);
        if (isShowTitle) {
            image_title_view.setVisibility(View.VISIBLE);
        } else {
            image_title_view.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

         /*2018 4 20 添加长按图片保存 GY*/
        saveBitmapDialog = new AlActionSheetDialog(this).builder()
                .addSheetItem("保存", AlActionSheetDialog.SheetItemColor.Black, new AlActionSheetDialog.OnSheetItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(int which) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {// 6.0权限拒绝

                            requestPermissions(new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 0x18);
                        }else{
                            FileUtils.saveBitmapGlide(ImagePreviewActivity.this,saveImageUrl,System.currentTimeMillis()+".jpg");
                        }
                    }
                });
       // overridePendingTransition(R.anim.ap1, R.anim.ap2);
    }

    public static void showImagePrivew(Context context, int index, String[] images, boolean isShowTitle) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(BUNDLE_KEY_IMAGES, images);
        intent.putExtra(BUNDLE_KEY_INDEX, index);
        ImagePreviewActivity.isShowTitle = isShowTitle;
        context.startActivity(intent);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int idx) {
        mCurrentPostion = idx;
        if (mImageUrls != null && mImageUrls.length >= 1) {
            image_num.setText((mCurrentPostion + 1) + "/" + mImageUrls.length);
        }
    }

    @Override
    public void onPagerClick() {
        finish();
    }

    @Override
    public void onLongClick(String imgUrl) {
        imageChooseItem(imgUrl);
    }

    /**
     * 操作选择
     * 
     * @param imgUrl
     */
    public void imageChooseItem(final String imgUrl) {
        saveImageUrl=imgUrl;
        saveBitmapDialog.show();
    }

}
