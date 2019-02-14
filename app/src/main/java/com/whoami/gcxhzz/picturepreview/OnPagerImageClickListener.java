/*
 * Copyright (c) EMCC 2015 All Rights Reserved
 */
package com.whoami.gcxhzz.picturepreview;

/**
 * <p>
 * 图片预览事件 。
 * <p>
 *
 * 创建日期 2015年8月24日 下午1:29:07<br>
 * @author  高炎<p>
 * @since  1.0.0
 */
public interface OnPagerImageClickListener {
    //
    public void onPagerClick();

    /**
     * 长按图片进行保存
     * @param imgUrl
     */
    public void onLongClick(String imgUrl);
}
