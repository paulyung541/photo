package com.paulyung.pyphoto.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;

import com.paulyung.pyphoto.AppManager;
import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.CoverOperate;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.utils.DialogHelp;
import com.paulyung.pyphoto.utils.FileHelper;
import com.paulyung.pyphoto.utils.MultiMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 * 业务层
 */
public class MainActivity extends MainViewActivity implements CoverOperate {
    //被选中的相册
    private List<String> mSelectCovers = new ArrayList<>();

    //被选中的照片
    private List<String> mSelectPhotos = new ArrayList<>();

    private Handler handler = new Handler();
    private int DELAY_MS = 100;//删除一张照片延时100ms，主要为了好看

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().AppExit();//exit
    }

    ///////////////////CoverOperate//////////////////////
    @Override
    public boolean addCover(String imgFiles) {
        boolean res = true;
        if (mSelectCovers.contains(imgFiles)) {
            mSelectCovers.remove(imgFiles);
            res = false;
        } else {
            mSelectCovers.add(imgFiles);
        }
        return res;
    }
    ///////////////////CoverOperate//////////////////////

    //点击了删除按钮
    @Override
    protected void onDeletePressed() {
        final ProgressDialog dialog = DialogHelp.getWaitDialog(this, getResources().getString(R.string.do_delete));

        new AsyncTask<Void, Void, Void>() {
            private List<String> deleteList;//已删除的文件列表

            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                deleteList = FileHelper.deletePictures(mSelectCovers);
                //ACTION_MEDIA_SCANNER_SCAN_FILE 发这个广播只会扫描单个文件，不能是dir
                DBHelper.updateFileByPath(MainActivity.this, deleteList);
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        setSelectState(false);
                        MultiMap<String, PhotoMsg> map = BaseApplication.getInstance().getPhotoMsg();
                        List<PhotoCover> coverList = BaseApplication.getInstance().getCovers();
                        //找到删除了哪些，同步全局的map
                        Set<String> set = map.keySet();
                        for (int j = 0; j < deleteList.size(); ++j) {
                            Iterator<String> it = set.iterator();
                            while (it.hasNext()) {
                                String key = it.next();
                                List<PhotoMsg> list = map.get(key);
                                for (int i = 0; i < list.size(); ++i) {
                                    if (list.get(i).getAbsolutePath().equals(deleteList.get(j))) {
                                        list.remove(i);
                                        break;
                                    }
                                }
                                if (list.size() == 0) {//防止key对应一个空的List
                                    it.remove();
                                }
                            }
                        }
                        mSelectCovers.clear();

                        //把相册信息提取出来
                        coverList.clear();
                        Set<String> coverSet = map.keySet();
                        for (String parent : coverSet) {
                            int index = map.get(parent).size() - 1;
                            PhotoMsg msg = map.get(parent).get(index);
                            File file = new File(msg.getAbsolutePath()).getParentFile();
                            PhotoCover cover = new PhotoCover(file.getAbsolutePath(), msg.getAbsolutePath(), file.getName(), map.get(parent).size());
                            if (cover.getCoverName().equals("Camera")) {//Camera放在最前面
                                cover.setCoverName("相机");
                                coverList.add(cover);
                                if (!coverList.isEmpty() && coverList.indexOf(cover) != 0) {
                                    PhotoCover tmp = coverList.get(0);
                                    int tmpIndex = coverList.indexOf(cover);
                                    coverList.set(0, cover);
                                    coverList.set(tmpIndex, tmp);
                                }
                            } else {
                                coverList.add(cover);
                            }
                        }
                        getCurrentFragment().onFileDelete();
                    }
                }, deleteList.size() * DELAY_MS);
            }
        }.execute();
    }
}
