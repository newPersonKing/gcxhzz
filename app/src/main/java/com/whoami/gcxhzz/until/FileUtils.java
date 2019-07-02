package com.whoami.gcxhzz.until;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by algorithm on 2017/9/4.
 */

public class FileUtils {

    /**
     * 默认APP根目录.
     */
    private static String downloadRootDir;

    // /storage/emulated/0/com.emcc.android.splendid/images
    private static String jollyImagesDir;

    // 文件的下载路径
    // /storage/emulated/0/com.emcc.android.splendid/download
    private static String sJollyDownloadDir;


    /**
     * 图片选择裁剪 然后生成新的文件的路径
     *
     * @return
     */
    public static String getJollyImages() {
        if (jollyImagesDir == null) {
            initFileDir(BaseUtils.getContext());
        }
        return jollyImagesDir;
    }

    public static String getJollyDownloadDir() {
        if (sJollyDownloadDir == null) {
            initFileDir(BaseUtils.getContext());
        }
        return sJollyDownloadDir;
    }

    /**
     * 描述：初始化存储目录.
     *
     * @param context the context
     */
    public static void initFileDir(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // /com.emcc.android.jollyapplication/
        String packageName = File.separator + context.getPackageName() + File.separator;

        // Jolly图片处理之后的文件夹
        String imagePath = packageName + "images" + File.separator;
        // com.emcc.android.jollyapplication/download
        String downloadPath = packageName + "download" + File.separator;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            // /storage/emulated/0
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File imageDownloadDirFile = new File(root + imagePath);
            if (imageDownloadDirFile.exists()) {
            } else {
                imageDownloadDirFile.mkdirs();
            }
            jollyImagesDir = imageDownloadDirFile.getPath();

            File downloadDirFile = new File(root + downloadPath);
            if (downloadDirFile.exists()) {
            } else {
                downloadDirFile.mkdirs();
            }
            sJollyDownloadDir = downloadDirFile.getPath();

        } else {
            return;
        }
    }

    /*通过ImageView背景生成bitmap*/
    public static void saveBitmap(ImageView view, String filePath) {
        filePath=Environment.getExternalStorageDirectory()+"/DCIM/"+filePath;
        Drawable drawable = view.getDrawable();
        if (drawable == null) {
            return;
        }
        FileOutputStream outStream = null;
        File file = new File(filePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            return;
        }
        try {
            outStream = new FileOutputStream(file);

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBitmapGlide(final Context context, String url, final String fileNmae){

        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                saveImageToGallery(resource,fileNmae,context);
                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void saveImageToGallery(Bitmap bitmap,String filePath,Context context){

        String savefilePath=Environment.getExternalStorageDirectory()+"/DCIM/"+filePath;
        File dir=new File(Environment.getExternalStorageDirectory()+"/DCIM/");
        if(!dir.exists()){
            dir.mkdirs();
        }

        FileOutputStream outStream = null;
        File file = new File(savefilePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            return;
        }

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }

                    /*最后刷新图库 不然图片在相册不显示*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    context.sendBroadcast(mediaScanIntent);
                } else {
                    context.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.fromFile(file)));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

