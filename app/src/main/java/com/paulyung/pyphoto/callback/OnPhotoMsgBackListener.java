package com.paulyung.pyphoto.callback;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public interface OnPhotoMsgBackListener {
    /**
     * 获取所有照片的路径（绝对路径）之后的回调，主要用在第一个Fragment里面
     * 回调之后就可以使用照片的map信息了
     */
    void onAllPhotoGet();
}
