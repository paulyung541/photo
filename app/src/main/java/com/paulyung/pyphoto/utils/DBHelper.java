package com.paulyung.pyphoto.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.activity.MainActivity;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public class DBHelper {
    /**
     * 扫描SDcard中的照片
     *
     * @param scanListener 照片搜索完成的回调
     */
    public static void scanPhoto(final OnPhotoMsgBackListener scanListener) {
        //执行异步扫描
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MultiMap<String, PhotoMsg> map = BaseApplication.getInstance().getPhotoMsg();
                if (!map.isEmpty())//每次清空
                    map.clear();

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = BaseApplication.getInstance().getContentResolver();
                //只查询jpeg和png的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null)
                    return null;
                while (cursor.moveToNext()) {
                    String absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File photoFile = new File(absolutePath);
                    String time = null;
                    String lon = null;
                    String lat = null;
                    String city = null;
                    try {
                        ExifInterface exif = new ExifInterface(absolutePath);
                        time = exif.getAttribute(ExifInterface.TAG_DATETIME);
                        lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PhotoMsg msg = new PhotoMsg(absolutePath, photoFile.getName(),
                            photoFile.getParentFile().getName(), time, lon, lat, city);

                    map.put(photoFile.getParentFile().getName(), msg);
                }
                cursor.close();

                List<PhotoCover> list = BaseApplication.getInstance().getCovers();
                if (!list.isEmpty())
                    list.clear();
                //遍历，保存相册封面
                Set<String> coverSet = map.keySet();
                for (String parent : coverSet) {
                    PhotoMsg msg = map.get(parent).get(map.get(parent).size() - 1);
                    File file = new File(msg.getAbsolutePath()).getParentFile();
                    PhotoCover cover = new PhotoCover(file.getAbsolutePath(), msg.getAbsolutePath(), file.getName(), map.get(parent).size());
                    if (cover.getCoverName().equals("Camera")) {//Camera放在最前面
                        cover.setCoverName("相机");
                        list.add(cover);
                        if (!list.isEmpty() && list.indexOf(cover) != 0) {
                            PhotoCover tmp = list.get(0);
                            int tmpIndex = list.indexOf(cover);
                            list.set(0, cover);
                            list.set(tmpIndex, tmp);
                        }
                    } else {
                        list.add(cover);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void notthing) {
                if (scanListener != null)
                    scanListener.onAllPhotoGet();
            }
        }.execute();
    }


    public static void updateFileByPath(MainActivity activity, List<String> filePathList) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        for (int i = 0; i < filePathList.size(); ++i) {
            Uri data = Uri.fromFile(new File(filePathList.get(i)));
            intent.setData(data);
            activity.sendBroadcast(intent);
        }
    }
    //// TODO: 2016/11/23 要解决的问题是：怎样在删除原图的同时，删掉数据库中的缩略图。扫描一个目录
}
