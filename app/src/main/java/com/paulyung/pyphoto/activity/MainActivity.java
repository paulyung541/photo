package com.paulyung.pyphoto.activity;

import android.support.annotation.NonNull;

import com.paulyung.pyphoto.AppManager;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.callback.FileOperate;
import com.paulyung.pyphoto.task.CoverDeleteTask;
import com.paulyung.pyphoto.task.TimeDeleteTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 * 业务层
 */
public class MainActivity extends MainViewActivity implements FileOperate {
    //被选中的目录或者文件
    private List<String> mSelectFilesOrDirs = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().AppExit();//exit
    }

    ///////////////////FileOperate//////////////////////
    @Override
    public boolean addFile(String imgFiles) {
        boolean res = true;
        if (mSelectFilesOrDirs.contains(imgFiles)) {
            mSelectFilesOrDirs.remove(imgFiles);
            res = false;
        } else {
            mSelectFilesOrDirs.add(imgFiles);
        }
        return res;
    }
    ///////////////////FileOperate//////////////////////

    //点击了删除按钮
    @Override
    protected void onDeletePressed(String tag) {
        int tagi = Integer.valueOf(tag);
        switch (tagi) {
            case R.id.action_photo_state://普通目录相册
                new CoverDeleteTask(this, getCurrentFragment(), mSelectFilesOrDirs).execute();
                break;
            case R.id.action_time_state://时间照片列表
                new TimeDeleteTask(this, getCurrentFragment(), mSelectFilesOrDirs).execute();
                break;
            case R.id.action_position_state://地址相册
                //// TODO: 2016/12/2 AsyncTask
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getCurrentFragment().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
