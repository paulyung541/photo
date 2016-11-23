package com.paulyung.pyphoto.bean;

/**
 * Created by yang on 2016/11/18.
 * paulyung@outlook.com
 */

public class PhotoCover {
    private String cover;//相册路径(目录)
    private String coverAbsolutePath;//相册封面图片路径
    private String coverName;//相册名字
    private int size;//包含的照片数量
    private boolean isCheck;//是否选中状态(PS：此为界面信息，放在这里方便OnBindView里根据position进行设置viewholder)
    private boolean isShowCheckBox;//是否显示CheckBox

    public PhotoCover(String cover, String coverAbsolutePath, String coverName, int size) {
        this.cover = cover;
        this.coverAbsolutePath = coverAbsolutePath;
        this.coverName = coverName;
        this.size = size;
        this.isCheck = isCheck;
        this.isShowCheckBox = isShowCheckBox;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverAbsolutePath() {
        return coverAbsolutePath;
    }

    public void setCoverAbsolutePath(String coverAbsolutePath) {
        this.coverAbsolutePath = coverAbsolutePath;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }
}
