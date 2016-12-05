package com.paulyung.pyphoto.bean;

import java.io.Serializable;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public class PhotoMsg implements Serializable {
    private String absolutePath;
    private String simpleName;
    private String parentName;//目录名（只是名字）
    private String time;
    private float lon;//经度
    private float lat;//纬度
    private String location;//详细地址信息 省+市+区+乡镇+街道+门牌号
    private String city;//省 市

    private boolean isCheck;//是否选中状态(PS：此为界面信息，放在这里方便OnBindView里根据position进行设置viewholder)
    private boolean isShowCheckBox;//是否显示CheckBox

    public PhotoMsg(String absolutePath, String simpleName,String parentName, String time, float lon, float lat, String city) {
        this.absolutePath = absolutePath;
        this.simpleName = simpleName;
        this.parentName = parentName;
        this.time = time;
        this.lon = lon;
        this.lat = lat;
        this.city = city;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
