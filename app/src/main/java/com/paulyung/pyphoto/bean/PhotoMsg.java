package com.paulyung.pyphoto.bean;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public class PhotoMsg {
    private String absolutePath;
    private String simpleName;
    private String parentName;//目录名（只是名字）
    private String time;
    private String lon;//经度
    private String lat;//纬度
    private String city;

    public PhotoMsg(String absolutePath, String simpleName,String parentName, String time, String lon, String lat, String city) {
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

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
