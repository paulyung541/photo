package com.paulyung.pyphoto.callback;

/**
 * Created by yang on 2016/11/21.
 * paulyung@outlook.com
 * 相册操作
 */

public interface FileOperate {
    /**
     * @param file 文件的绝对路径
     * @return 如果选中的照片已经选过了，则返回false，否则返回true
     */
    boolean addFile(String file);//添加选中的文件
}
