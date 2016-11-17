package com.paulyung.pyphoto.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public class DBHelper {
    /**
     * 通过照片的绝对路径寻找数据库并返回 PhotoMsg 实体
     *
     * @param photoName 照片的绝对路径
     * @return 照片信息
     */
//    public static PhotoMsg findByPhotoName(String photoName) {
//        List<PhotoMsg> list = SugarRecord.find(PhotoMsg.class, "absolutePath=?", new String[]{photoName}, null, null, "1");
//        if (list.isEmpty())
//            return null;
//        return list.get(0);
//    }

    /**
     * 扫描SDcard中的照片
     *
     * @param context      Context
     * @param scanListener 照片搜索完成的回调
     */
    public static void scanPhoto(Context context, final OnPhotoMsgBackListener scanListener) {
        //执行异步扫描
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... params) {
                MultiMap<String, PhotoMsg> map = BaseApplication.getInstance().getPhotoMsg();
                if (!map.isEmpty())//每次清空
                    map.clear();

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = params[0].getContentResolver();
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
                            photoFile.getParentFile().getName(),time, lon, lat, city);

                    map.put(photoFile.getParentFile().getName(), msg);
                }
                cursor.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void notthing) {
                if (scanListener != null)
                    scanListener.onAllPhotoGet();
            }
        }.execute(context);
    }
}
