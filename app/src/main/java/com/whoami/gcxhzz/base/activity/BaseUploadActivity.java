package com.whoami.gcxhzz.base.activity;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.ResponseCallback;

import com.whoami.gcxhzz.entity.FileModule;
import com.whoami.gcxhzz.entity.FileModuleEntity;
import com.whoami.gcxhzz.http.HttpRequestUtils;
import com.whoami.gcxhzz.http.HttpService;
import com.whoami.gcxhzz.picSelector.GridImageAdapter;
import com.whoami.gcxhzz.picturepreview.ImagePreviewActivity;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.ResponseBody;

public abstract class BaseUploadActivity extends BaseTitleActivity {


    protected GridImageAdapter adapter;

    //-----------------------------------------------图片选择------------------------------------------------------------
    protected int maxSelectNum = 3;// 图片最大可选数量
    private boolean isPreviewVideo = true;
    protected boolean enableCrop = false;
    protected List<LocalMedia> selectList = new ArrayList<>();

    protected GridImageAdapter.OnAddPicHintShowListener onAddPicHintShowListener;

    public String voice_path;/*录音路径*/
    public LocalMedia videoLocalMedia ;/*录制视频返回的文件*/

    protected RxPermissions rxPermissions;
    @Override
    protected void init() {
        super.init();
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!ObjectUtils.isNull(onAddPicHintShowListener)){
            onAddPicHintShowListener.onResume();
        }
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
//                    // 进入相册
//                    /**
//                     * type --> 1图片 or 2视频
//                     * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
//                     * maxSelectNum --> 可选择图片的数量
//                     * selectMode         --> 单选 or 多选
//                     * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
//                     * isPreview    --> 是否打开预览选项
//                     * isCrop       --> 是否打开剪切选项
//                     * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
//                     * ThemeStyle -->主题颜色
//                     * CheckedBoxDrawable -->图片勾选样式
//                     * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//                     * cropH-->裁剪高度 值不能小于100
//                     * isCompress -->是否压缩图片
//                     * setEnablePixelCompress 是否启用像素压缩
//                     * setEnableQualityCompress 是否启用质量压缩
//                     * setRecordVideoSecond 录视频的秒数，默认不限制
//                     * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
//                     * setImageSpanCount -->每行显示个数
//                     * setCheckNumMode 是否显示QQ选择风格(带数字效果)
//                     * setPreviewColor 预览文字颜色
//                     * setCompleteColor 完成文字颜色
//                     * setPreviewBottomBgColor 预览界面底部背景色
//                     * setBottomBgColor 选择图片页面底部背景色
//                     * setCompressQuality 设置裁剪质量，默认无损裁剪
//                     * setSelectMedia 已选择的图片
//                     * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
//                     * 注意-->type为2时 设置isPreview or isCrop 无效
//                     * 注意：Options可以为空，默认标准模式
//                     */
//                    int selector = R.drawable.select_cb;
//                    FunctionConfig config = new FunctionConfig();
//                    config.setType(selectType);
//                    config.setCopyMode(copyMode);
//                    config.setCompress(isCompress);
//                    config.setEnablePixelCompress(true);
//                    config.setEnableQualityCompress(true);
//                    config.setMaxSelectNum(maxSelectNum);
//                    config.setSelectMode(selectMode);
//                    config.setShowCamera(isShow);
//                    config.setEnablePreview(enablePreview);
//                    config.setEnableCrop(enableCrop);
//                    config.setPreviewVideo(isPreviewVideo);
//                    config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
//                    config.setRecordVideoSecond(60);// 视频秒数
//                    config.setCropW(cropW);
//                    config.setCropH(cropH);
//                    config.setCheckNumMode(isCheckNumMode);
//                    config.setCompressQuality(100);
//                    config.setImageSpanCount(4);
//                    config.setSelectMedia(selectMedia);
//                    config.setCompressFlag(compressFlag);
//                    config.setCompressW(compressW);
//                    config.setCompressH(compressH);
//                    if (theme) {
//                        config.setThemeStyle(ContextCompat.getColor(mContext, R.color.blue));
//                        // 可以自定义底部 预览 完成 文字的颜色和背景色
//                        if (!isCheckNumMode) {
//                            // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
//
//                        }config.setPreviewColor(ContextCompat.getColor(mContext, R.color.white));
//                        config.setCompleteColor(ContextCompat.getColor(mContext, R.color.white));
//                        config.setPreviewBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//                        config.setBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//                    }
//                    if (selectImageType) {
//                        config.setCheckedBoxDrawable(selector);
//                    }
//
//                    // 先初始化参数配置，在启动相册
//                    PictureConfig.init(config);
//                    PictureConfig.getPictureConfig().openPhoto(mContext, resultCallback);

                    // 进入相册 以下是例子：不需要的api可以不写
                    PictureSelector.create(BaseUploadActivity.this)
                            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .maxSelectNum(maxSelectNum)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                            .previewImage(true)// 是否可预览图片
                            .isCamera(true)// 是否显示拍照按钮
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .enableCrop(enableCrop)// 是否裁剪
                            .previewVideo(isPreviewVideo)
                            .compress(true)// 是否压缩
                            .cropWH(0,0)
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .selectionMedia(selectList)// 是否传入已选图片
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

                    break;
                case 1:
                    // 删除图片
                    selectList.remove(position);
                    adapter.notifyItemRemoved(position);

                    if(!ObjectUtils.isNull(onAddPicHintShowListener)){
                        onAddPicHintShowListener.onDeletePicClick();
                    }
                    break;
            }
        }

        @Override
        public void setOnClickListener(View v, int position) {
            String[] imgs = new String[selectList.size()];
            for (int i = 0; i < selectList.size(); i++) {
                imgs[i] = selectList.get(i).getCompressPath();
            }
            ImagePreviewActivity.showImagePrivew(mContext, position, imgs, true);
        }
    };

    /**
     * 图片回调方法
     */
//    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
//        @Override
//        public void onSelectSuccess(List<LocalMedia> resultList) {
//            selectMedia = resultList;
//            Log.i("callBack_result", selectMedia.size() + "");
//            if (selectMedia != null) {
//                adapter.setList(selectMedia);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };
    //----------------------单张图片选择--------------------------------
//    protected View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // 进入相册
//            /**
//             * type --> 1图片 or 2视频
//             * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
//             * maxSelectNum --> 可选择图片的数量
//             * selectMode         --> 单选 or 多选
//             * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
//             * isPreview    --> 是否打开预览选项
//             * isCrop       --> 是否打开剪切选项
//             * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
//             * ThemeStyle -->主题颜色
//             * CheckedBoxDrawable -->图片勾选样式
//             * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//             * cropH-->裁剪高度 值不能小于100
//             * isCompress -->是否压缩图片
//             * setEnablePixelCompress 是否启用像素压缩
//             * setEnableQualityCompress 是否启用质量压缩
//             * setRecordVideoSecond 录视频的秒数，默认不限制
//             * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
//             * setImageSpanCount -->每行显示个数
//             * setCheckNumMode 是否显示QQ选择风格(带数字效果)
//             * setPreviewColor 预览文字颜色
//             * setCompleteColor 完成文字颜色
//             * setPreviewBottomBgColor 预览界面底部背景色
//             * setBottomBgColor 选择图片页面底部背景色
//             * setCompressQuality 设置裁剪质量，默认无损裁剪
//             * setSelectMedia 已选择的图片
//             * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
//             * 注意-->type为2时 设置isPreview or isCrop 无效
//             * 注意：Options可以为空，默认标准模式
//             */
//            int selector = R.drawable.select_cb;
//            FunctionConfig config = new FunctionConfig();
//            config.setType(selectType);
//            config.setCopyMode(copyMode);
//            config.setCompress(isCompress);
//            config.setEnablePixelCompress(true);
//            config.setEnableQualityCompress(true);
//            config.setMaxSelectNum(maxSelectNum);
//            config.setSelectMode(selectMode);
//            config.setShowCamera(isShow);
//            config.setEnablePreview(enablePreview);
//            config.setEnableCrop(enableCrop);
//            config.setPreviewVideo(isPreviewVideo);
//            config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
//            config.setRecordVideoSecond(60);// 视频秒数
//            config.setCropW(cropW);
//            config.setCropH(cropH);
//            config.setCheckNumMode(isCheckNumMode);
//            config.setCompressQuality(100);
//            config.setImageSpanCount(4);
//            config.setSelectMedia(selectMedia);
//            config.setCompressFlag(compressFlag);
//            config.setCompressW(compressW);
//            config.setCompressH(compressH);
//            if (theme) {
//                config.setThemeStyle(ContextCompat.getColor(mContext, R.color.blue));
//                // 可以自定义底部 预览 完成 文字的颜色和背景色
//                if (!isCheckNumMode) {
//                    // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
//                    config.setPreviewColor(ContextCompat.getColor(mContext, R.color.white));
//                    config.setCompleteColor(ContextCompat.getColor(mContext, R.color.white));
//                    config.setPreviewBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//                    config.setBottomBgColor(ContextCompat.getColor(mContext, R.color.blue));
//                }
//            }
//            if (selectImageType) {
//                config.setCheckedBoxDrawable(selector);
//            }
//
//            // 先初始化参数配置，在启动相册
//            PictureConfig.init(config);
//            PictureConfig.getPictureConfig().openPhoto(mContext, solaResultCallback);
//        }
//    };

    /**
     * 图片回调方法
     */
//    private PictureConfig.OnSelectResultCallback solaResultCallback = new PictureConfig.OnSelectResultCallback() {
//        @Override
//        public void onSelectSuccess(List<LocalMedia> resultList) {
//            selectMedia = resultList;
//            Log.i("callBack_result", selectMedia.size() + "");
//            solaSelectSuccess(selectMedia);
//        }
//    };

    /**
     * 单张请重写该方法
     *
     * @param selectMedia
     */
    protected void solaSelectSuccess(List<LocalMedia> selectMedia) {

    }
    //-----------------------------------------------图片选择------------------------------------------------------------

    //-----------------------------------------------图片上传------------------------------------------------------------

    /**
     * app----上传附件（文件或者图片）
     */
    protected void postFiles() {
        final List<FileModuleEntity> fileModuleEntities = new ArrayList<>();
        final List<String> paths = new ArrayList<>();
        for(LocalMedia localMedia : selectList){
            paths.add(localMedia.getCompressPath());
        }

        if(!ObjectUtils.isNull(voice_path)){
            paths.add(voice_path);
        }

        if(videoLocalMedia!=null){
            paths.add(videoLocalMedia.getPath());
        }

        if (paths.size() > 0) {
            for (String path : paths) {
                File file = new File(path);
                /*文件上传 rxUploadWithPartListByFileCustom*/
                Log.i("ccccccccc","path=="+path);
                HttpRequestUtils.getInstance().getNovate().rxUploadWithPart(HttpService.API_FILEINPUT_UPLOAD, file, new ResponseCallback<FileModule, ResponseBody>() {
                    @Override
                    public FileModule onHandleResponse(ResponseBody response) throws Exception {
                        /*返回成功直接走的这里 没有再次封装直接使用返回的json*/
                        String jstring = new String(response.bytes());
                        Log.i("cccccccccc","jsString==="+jstring);
                        return BaseUtils.getGson().fromJson(jstring,FileModule.class);
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {
                      Log.i("cccccccccccc","err==="+e.getMessage());
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {

                    }

                    @Override
                    public void onNext(Object tag, Call call, FileModule response) {
                        if (response.isSucceeded()){
                            fileModuleEntities.add(response.getContent());
                            if (fileModuleEntities.size()==paths.size()){
                                uploadSuccess(fileModuleEntities);
                            }
                        }else {
                            uploadFailure();
                        }

                    }
                });
            }
        }
    }


    /**
     * 文件上传成功 暂时注释
     *
     * @param fileModuleEntities
     */
    protected abstract void uploadSuccess(List<FileModuleEntity> fileModuleEntities);

    /**
     * 文件上传失败
     */
    protected abstract void uploadFailure();

    //-----------------------------------------------图片上传------------------------------------------------------------
}
