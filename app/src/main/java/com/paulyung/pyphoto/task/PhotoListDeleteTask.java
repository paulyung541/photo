package com.paulyung.pyphoto.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.fragment.BaseFragment;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.utils.DialogHelp;
import com.paulyung.pyphoto.utils.FileHelper;

import java.util.List;

/**
 * Created by yang on 2016/12/6.
 * paulyung@outlook.com
 */

public class PhotoListDeleteTask extends AsyncTask<Void, Void, Void> {
    private static final int DELAY_MS = 100;//删除一张照片延时100ms，主要为了好看
    private List<String> deleteList;//已删除的文件列表
    private Handler handler = new Handler();
    private SelectStateCheck mCheck;
    private List<String> mSelectPhotos;
    private BaseFragment mFragment;
    private ProgressDialog dialog;
    private BaseApplication appContext;
    private String mTitle;

    public PhotoListDeleteTask(SelectStateCheck check, BaseFragment fragment, List<String> photos, String title) {
        appContext = BaseApplication.getInstance();
        mSelectPhotos = photos;
        mFragment = fragment;
        mCheck = check;
        mTitle = title;
    }

    @Override
    protected void onPreExecute() {
        dialog = DialogHelp.getWaitDialog(mFragment.getActivity(), appContext.getString(R.string.do_delete));
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        deleteList = FileHelper.deleteFiles(mSelectPhotos);
        //ACTION_MEDIA_SCANNER_SCAN_FILE 发这个广播只会扫描单个文件，不能是dir
        DBHelper.updateFileByPath(deleteList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                mCheck.setSelectState(false);
                String tmpStr = mTitle;
                if (tmpStr.equals("相机"))
                    tmpStr = "Camera";
                List<PhotoMsg> list = BaseApplication.getInstance().getPhotoMsg().get(tmpStr);
                for (int i = 0; i < list.size(); ++i) {
                    for (int j = 0; j < deleteList.size(); ++j) {
                        if (list.get(i).getAbsolutePath().equals(deleteList.get(j))) {
                            list.remove(i);
                            i -= 1;
                            break;
                        }
                    }
                }
                mFragment.onFileDelete();
            }
        }, deleteList.size() * DELAY_MS);
    }
}
