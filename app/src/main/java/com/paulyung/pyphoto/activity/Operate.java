package com.paulyung.pyphoto.activity;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public interface Operate {
    void delete(String[] imgFiles);//删除照片

    void select(String[] imgFiles);//选择照片
}
