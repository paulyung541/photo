package com.paulyung.pyphoto.callback;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 * 照片操作
 */

public interface PhotoOperate {
    void deletePhotos(String[] imgFiles);//删除照片

    void addPhoto(String imgFile);//添加选中的照片

    void removePhoto(String imgFile);//移除选中的照片
}
