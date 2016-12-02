package com.paulyung.pyphoto;

import android.app.Application;
import android.widget.Toast;

import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.MultiMap;
import com.paulyung.pyphoto.utils.PYLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class BaseApplication extends Application {
    private static BaseApplication _INSTANCE;

    //// TODO: 2016/12/2 暂时假设文件名不会重复，后续需要改为绝对路径作Key
    private MultiMap<String, PhotoMsg> mPhotoMsg;
    private List<PhotoCover> mCovers;
    private List<PhotoMsg> mTimeList;//按照时间排列的照片,会插入时间头，时间头是只带时间信息的PhotoMsg

    public synchronized static BaseApplication getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _INSTANCE = this;

        PYLog.setDebug(BuildConfig.DEBUG);
        mPhotoMsg = new MultiMap<>();
        mCovers = new ArrayList<>();
        mTimeList = new ArrayList<>();
    }

    public void showToast(String msg) {
        Toast.makeText(_INSTANCE, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取所有照片
     *
     * @return MutilMap < 目录,照片 >
     */
    public MultiMap<String, PhotoMsg> getPhotoMsg() {
        return mPhotoMsg;
    }

    /**
     * 根据父路径获取该路径的照片列表
     *
     * @param parent 照片的父路径，就是照片所在的目录（要传绝对路径）
     * @return 该相册下所有照片信息
     */
    List<PhotoMsg> getPhotoByCoverName(String parent) {
        return mPhotoMsg.get(parent);
    }

    /**
     * 获取相册信息
     *
     * @return 相册列表
     */
    public List<PhotoCover> getCovers() {
        return mCovers;
    }

    public List<PhotoMsg> getTimeList() {
        return mTimeList;
    }
}
