package com.paulyung.pyphoto.bean;

/**
 * Created by yang on 2016/12/6.
 * paulyung@outlook.com
 * 地址相册封面
 */

public class PositionCover {
    private String title;//地址信息 省+市+区+乡镇+街道+门牌号
    private String coverImagePath;//封面图片路径
    private int size;//拥有的图片数量

    public PositionCover(String title, String coverImagePath, int size) {
        this.title = title;
        this.coverImagePath = coverImagePath;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
