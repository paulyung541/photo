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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by yang on 2016/12/2.
 * paulyung@outlook.com
 */

public class TimeDeleteTask extends AsyncTask<Void, Void, Void> {
    private static final int DELAY_MS = 100;//删除一张照片延时100ms，主要为了好看
    private List<String> deleteList;//已删除的文件列表
    private Handler handler = new Handler();
    private SelectStateCheck mCheck;
    private List<String> mFiles;
    private BaseFragment mFragment;
    private ProgressDialog dialog;
    private BaseApplication appContext;

    public TimeDeleteTask(SelectStateCheck check, BaseFragment fragment, List<String> files) {
        appContext = BaseApplication.getInstance();
        mFiles = files;
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
        deleteList = FileHelper.deleteFiles(mFiles);
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
                //// TODO: 2016/12/2 数据同步 
                List<PhotoMsg> timeList = appContext.getTimeList();
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
                    //清除掉删除了的时间照片列表信息
                    for (int i = 0; i < timeList.size(); i++) {
                        if (timeList.get(i).getAbsolutePath() != null &&
                                timeList.get(i).getAbsolutePath().equals(deleteList.get(j))) {
                            timeList.remove(i);
                            break;
                        }
                    }
                }
                for (int i = 0; i < timeList.size(); i++) {
                    if (timeList.get(i).getAbsolutePath() == null && (i + 1) < timeList.size() &&
                            timeList.get(i + 1).getAbsolutePath() == null) {
                        //( 第i个Path为null && i不是最后一个 && i+1的path也为null )
                        timeList.remove(i);
                        i--;
                    }
                }
                //若最后一个是时间信息，表示该时间已无照片，应删掉时间标志
                if (timeList.get(timeList.size() - 1).getAbsolutePath() == null) {
                    timeList.remove(timeList.size() - 1);
                }

                mFiles.clear();
                mFragment.onFileDelete();
            }
        }, deleteList.size() * DELAY_MS);
    }
}
