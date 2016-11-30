package com.paulyung.pyphoto.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.PhotoOperate;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.fragment.BaseFragment;
import com.paulyung.pyphoto.fragment.PhotoListFragment;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.utils.DialogHelp;
import com.paulyung.pyphoto.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/18.
 * paulyung@outlook.com
 * 点击相册后进入的照片列表
 */

public class PhotoListActivity extends BaseActivity implements SelectStateCheck, PhotoOperate {
    private FragmentManager mFragmentManager;
    private boolean isCheckState;
    private BaseFragment mFragment;
    //被选中的照片
    private List<String> mSelectPhotos = new ArrayList<>();
    private String title;//标题
    private Handler handler = new Handler();
    private int DELAY_MS = 100;//删除一张照片延时100ms，主要为了好看

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected void beforeSetView() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        bundle.putString(BundleTag.COVER_NAME, title);
        mFragment = PhotoListFragment.getInstance(bundle);
        mFragmentManager.beginTransaction()
                .add(R.id.lay_container, mFragment, getClass().getSimpleName()).commit();
    }

    @Override
    protected Toolbar getInitToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getIntent().getStringExtra(BundleTag.COVER_NAME);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray1));
        return toolbar;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        if (isCheckState) {
            menu.findItem(R.id.delete).setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isCheckState) {
            //执行删除
            onDeletePressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectState(boolean selectState) {
        isCheckState = selectState;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean getSelectState() {
        return isCheckState;
    }

    @Override
    public void onBackPressed() {
        if (isCheckState) {//如果为选照片状态则调用Fragment方法，
            if (!mFragment.onBackPressed())
                super.onBackPressed();//如果当前Fragment没有重写 onBackPressed() 方法，则任然使用默认返回
            setSelectState(false);
        } else {
            super.onBackPressed();
        }
    }

    //点击删除
    private void onDeletePressed() {
        final ProgressDialog dialog = DialogHelp.getWaitDialog(this, getResources().getString(R.string.do_delete));
        new AsyncTask<Void, Void, Void>() {
            private List<String> deleteList;//已删除的文件列表

            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                deleteList = FileHelper.deleteFiles(mSelectPhotos);
                //ACTION_MEDIA_SCANNER_SCAN_FILE 发这个广播只会扫描单个文件，不能是dir
                DBHelper.updateFileByPath(PhotoListActivity.this, deleteList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        setSelectState(false);
                        String tmpStr = title;
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
        }.execute();
    }

    @Override
    public boolean addPhoto(String imgFile) {
        boolean res = true;
        if (mSelectPhotos.contains(imgFile)) {
            mSelectPhotos.remove(imgFile);
            res = false;
        } else {
            mSelectPhotos.add(imgFile);
        }
        return res;
    }
}
