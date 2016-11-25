package com.paulyung.pyphoto.callback;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 * 照片操作
 */

public interface PhotoOperate {
    /**
     * @param imgFile 选中相片的绝对路径
     * @return 如果选中的照片已经选过了，则返回false，否则返回true
     */
    boolean addPhoto(String imgFile);//添加选中的照片
}
