package com.whoami.gcxhzz.until;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whoami.gcxhzz.R;

/**
 * Created by algorithm on 2017/11/16.
 */

public class ImageLoadUtils {

    public static <T> void loadImage(T res, final ImageView imageView, int loadImg, int errorImg) {
        DrawableRequestBuilder<T> builder = Glide.with(BaseUtils.getContext()).load(res)
                .placeholder(loadImg) //设置占位图
                .error(errorImg); //设置错误图片;
        builder.into(imageView);

    }
    public static <T> void loadImageMatchParent(T res, final ImageView imageView, int loadImg, int errorImg) {
        DrawableRequestBuilder<T> builder = Glide.with(BaseUtils.getContext()).load(res)
                .placeholder(loadImg) //设置占位图
                .error(errorImg).centerCrop(); //设置错误图片;
        builder.into(imageView);


    }

    public static <T> void loadImage(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public <T> void loadListImageBig(Context context, T res, ImageView imageView) {
        DrawableRequestBuilder<T> builder = Glide.with(context).load(res)
                .placeholder(R.mipmap.ic_launcher) //设置占位图
                .error(R.mipmap.ic_launcher); //设置错误图片;
        builder.into(imageView);
    }
    /**
     * 加载用户图像
     *
     * @param res
     * @param imageView
     * @param <T>
     */
    public static <T> void loadHeaderImage(T res, ImageView imageView) {
        Glide.with(BaseUtils.getContext()).load(res)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.ic_header) //设置占位图
                .error(R.mipmap.ic_header).into(imageView); //设置错误图片;
    }

    public static <T> void loadImage_132_76(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public static <T> void loadImage_191_110(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public static <T> void loadImage_200_150(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }
    public static <T> void loadImage_457_457(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }
    public static <T> void loadImage_750_370(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }
    /*避免加载图片 应该全屏的 刷新后不全屏*/
    public static <T> void loadImage_Match_Parent(T res, ImageView imageView) {
        loadImageMatchParent(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public static <T> void loadNewsDefaultImage(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }


    public static <T> void loadListImageSmall(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public static <T> void loadImageWithListerner(T res, ImageView imageView,LoadInterface LoadInterface) {
        loadImageWithListener(res, imageView,0, R.mipmap.ic_launcher,LoadInterface);
    }

    public static <T> void loadImageWithListener(T res, final ImageView imageView, int loadImg, int errorImg,final LoadInterface LoadInterface) {
        DrawableRequestBuilder<T> builder = Glide.with(BaseUtils.getContext()).load(res)
                .placeholder(null) //设置占位图
                .error(errorImg)
                .listener(new RequestListener<T, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, T model, Target<GlideDrawable> target, boolean isFirstResource) {
                        LoadInterface.loadingFinish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, T model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        LoadInterface.loadingFinish();
                        return false;
                    }
                }); //设置错误图片;
        builder.into(imageView);

    }
   public interface LoadInterface{
        void loadingFinish();
    }
}
