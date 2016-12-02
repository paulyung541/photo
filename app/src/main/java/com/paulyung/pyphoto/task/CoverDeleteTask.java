package com.paulyung.pyphoto.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.fragment.BaseFragment;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.utils.DialogHelp;
import com.paulyung.pyphoto.utils.FileHelper;
import com.paulyung.pyphoto.utils.MultiMap;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by yang on 2016/12/2.
 * paulyung@outlook.com
 */

public class CoverDeleteTask extends AsyncTask<Void, Void, Void> {
    private static final int DELAY_MS = 100;//删除一张照片延时100ms，主要为了好看
    private List<String> deleteList;//已删除的文件列表
    private Handler handler = new Handler();
    private SelectStateCheck mCheck;
    private List<String> mSelectCovers;
    private BaseFragment mFragment;
    private ProgressDialog dialog;
    private BaseApplication appContext;

    public CoverDeleteTask(SelectStateCheck check, BaseFragment fragment, List<String> covers) {
        appContext = BaseApplication.getInstance();
        mSelectCovers = covers;
        mFragment = fragment;
        mCheck = check;
    }

    @Override
    protected void onPreExecute() {
        dialog = DialogHelp.getWaitDialog(mFragment.getActivity(), appContext.getString(R.string.do_delete));
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        deleteList = FileHelper.deletePictures(mSelectCovers);
        //ACTION_MEDIA_SCANNER_SCAN_FILE 发这个广播只会扫描单个文件，不能是dir
        DBHelper.updateFileByPath(deleteList);
        return null;
    }

    @Override
    protected void onPostExecute(Void Void) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                mCheck.setSelectState(false);
                MultiMap<String, PhotoMsg> map = appContext.getPhotoMsg();
                List<PhotoCover> coverList = appContext.getCovers();
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
                            it.remove();//delete key
                            for (int i = 0; i < coverList.size(); i++) {//如果相册已无照片，则清除该相册信息
                                if (coverList.get(i).getCoverName().equals(key)) {
                                    coverList.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }

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
                mSelectCovers.clear();
                mFragment.onFileDelete();
            }
        }, deleteList.size() * DELAY_MS);
    }
}
