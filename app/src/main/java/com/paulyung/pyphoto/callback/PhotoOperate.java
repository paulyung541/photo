package com.paulyung.pyphoto.callback;

import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.MultiMap;

import java.util.List;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public interface PhotoOperate {
    void delete(String[] imgFiles);//删除照片

    void select(String[] imgFiles);//选择照片

    /**
     * 调用此方法获取所有照片信息，在初始化时不可
     * @return
     */
    MultiMap<String, PhotoMsg> getAllPhoto();

    /**
     * 根据父路径获取该路径的照片列表
     * @param parent 照片的父路径，就是照片所在的目录（要传绝对路径）
     * @return
     */
    List<PhotoMsg> getPhotoByParentPath(String parent);
}
