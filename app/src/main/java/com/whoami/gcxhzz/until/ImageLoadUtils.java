package com.whoami.gcxhzz.until;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.whoami.gcxhzz.R;

/**
 * Created by algorithm on 2017/11/16.
 */

public class ImageLoadUtils {

    public static <T> void loadImage(T res, final ImageView imageView, int loadImg, int errorImg) {

        RequestOptions options = new RequestOptions()
                .error(errorImg)
                .placeholder(loadImg);

        Glide.with(BaseUtils.getContext()).load(res).apply(options).into(imageView);

    }
    public static <T> void loadImageMatchParent(T res, final ImageView imageView, int loadImg, int errorImg) {
        RequestOptions options = new RequestOptions()
                .error(errorImg)
                .placeholder(loadImg)
                .centerCrop();

        Glide.with(BaseUtils.getContext()).load(res).apply(options).into(imageView); //设置错误图片;
    }

    public static <T> void loadImage(T res, ImageView imageView) {
        loadImage(res, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    public <T> void loadListImageBig(Context context, T res, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher);
        Glide.with(context).load(res).into(imageView); //设置错误图片;
    }
    /**
     * 加载用户图像
     *
     * @param res
     * @param imageView
     * @param <T>
     */
    public static <T> void loadHeaderImage(T res, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop();
        Glide.with(BaseUtils.getContext()).asBitmap().load(res).into(imageView); //设置错误图片;
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
        RequestOptions options = new RequestOptions();
        options.placeholder(null);
        options.error(errorImg);

        Glide.with(BaseUtils.getContext()).load(res).apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LoadInterface.loadingFinish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LoadInterface.loadingFinish();
                        return false;
                    }
                }).into(imageView); //设置错误图片;
    }
    public interface LoadInterface{
        void loadingFinish();
    }
}
