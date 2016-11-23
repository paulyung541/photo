package com.paulyung.pyphoto.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/21.
 * paulyung@outlook.com
 */

public class FileHelper {
    /**
     * 删除文件
     *
     * @param absolutePath 文件绝对路径
     * @return 返回删除了的文件路径
     */
    public static String deleteFile(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists() && file.isFile())
            file.delete();
        else
            return null;
        return absolutePath;
    }

    /**
     * 删除多个文件
     *
     * @param files 多个目录List
     * @return 返回删除了的文件路径集合
     */
    public static List<String> deleteFiles(List<String> files) {
        List<String> hadDeleteList = new ArrayList<>();
        for (int i = 0; i < files.size(); ++i) {
            String str = deleteFile(files.get(i));
            if (str != null) {
                hadDeleteList.add(str);
            }
        }
        return hadDeleteList;
    }

    /**
     * 删除图片，如果里面还包含目录，则不删除本目录以及里面的目录，仅删除里面的文件
     *
     * @param path 图片目录
     * @return 返回删除了的文件路径集合
     */
    public static List<String> deletePicture(String path) {
        boolean hasDir = false;
        List<String> hadDeleteList = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    String str = deleteFile(files[i].getAbsolutePath());
                    if (str != null) {
                        hadDeleteList.add(str);
                    }
                } else if (files[i].isDirectory()) {
                    hasDir = true;
                }
            }
            if (!hasDir)
                dir.delete();
        }
        return hadDeleteList;
    }

    /**
     * 删除多个目录的图片
     *
     * @param dirs 目录集合
     * @return 返回删除了的文件路径集合
     */
    public static List<String> deletePictures(List<String> dirs) {
        List<String> hadDeleteList = new ArrayList<>();
        for (int i = 0; i < dirs.size(); ++i) {
            List<String> deleteFiles = deletePicture(dirs.get(i));
            hadDeleteList.addAll(deleteFiles);
        }
        return hadDeleteList;
    }
}
