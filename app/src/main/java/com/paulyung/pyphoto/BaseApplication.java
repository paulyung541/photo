package com.paulyung.pyphoto;

import android.app.Application;
import android.widget.Toast;

import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.MultiMap;
import com.paulyung.pyphoto.utils.PYLog;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class BaseApplication extends Application {
    private static BaseApplication _INSTANCE;

    private MultiMap<String, PhotoMsg> mPhotoMsg;
    private List<PhotoCover> mCovers;

    public synchronized static BaseApplication getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _INSTANCE = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        PYLog.setDebug(BuildConfig.DEBUG);
        mPhotoMsg = new MultiMap<>();
        mCovers = new ArrayList<>();
    }

    public void showToast(String msg) {
        Toast.makeText(_INSTANCE, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取所有照片
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
}
