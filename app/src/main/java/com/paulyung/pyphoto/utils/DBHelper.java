package com.paulyung.pyphoto.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.bean.PositionCover;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;
import com.paulyung.pyphoto.task.PositionScanTask;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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
                List<PhotoMsg> timeList = BaseApplication.getInstance().getTimeList();
                LinkedList<PhotoMsg> tmpTimeList = new LinkedList<>();
                if (!map.isEmpty())//每次清空
                    map.clear();

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = BaseApplication.getInstance().getContentResolver();
                //查询jpeg，png和gif的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null)
                    return null;

                //查询Media数据库取出Photo信息
                while (cursor.moveToNext()) {
                    String absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File photoFile = new File(absolutePath);
                    if (!photoFile.exists()) {
                        //如果从数据库查到，却发现无此文件，就应当从数据库中删除该记录以及缩略图
                        deleteImageFromDatabase(absolutePath);
                        continue;
                    }

                    String time = null;
                    float lon = 0.0f;
                    float lat = 0.0f;
                    String city = null;
                    try {
                        ExifInterface exif = new ExifInterface(absolutePath);
                        float[] output = new float[2];
                        exif.getLatLong(output);
                        lon = output[1];
                        lat = output[0];
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    time = StringUtils.getTimeByMilliseconds(photoFile.lastModified());//文件修改时间

                    PhotoMsg msg = new PhotoMsg(absolutePath, photoFile.getName(),
                            photoFile.getParentFile().getName(), time, lon, lat, city);
                    if (time != null)//没有时间信息的就不显示出来了
                        tmpTimeList.addFirst(msg);
                    map.put(photoFile.getParentFile().getName(), msg);
                }
                cursor.close();

                //插入时间头
                String preTime = "";
                for (PhotoMsg msg : tmpTimeList) {
                    if (!StringUtils.getSubString(0, 10, msg.getTime()).equals(preTime)) {
                        timeList.add(new PhotoMsg(null, null, null, StringUtils.getSubString(0, 10,
                                StringUtils.getSubString(0, 10, msg.getTime())), 0.0f, 0.0f, null));
                    }
                    timeList.add(msg);
                    preTime = StringUtils.getSubString(0, 10, msg.getTime());
                }

                List<PhotoCover> list = BaseApplication.getInstance().getCovers();
                if (!list.isEmpty())
                    list.clear();
                //遍历，保存相册封面
                Set<String> coverSet = map.keySet();
                for (String parent : coverSet) {
                    PhotoMsg msg = map.get(parent).get(map.get(parent).size() - 1);
                    File file = new File(msg.getAbsolutePath()).getParentFile();
                    PhotoCover cover = new PhotoCover(file.getAbsolutePath(), msg.getAbsolutePath(), file.getName(), map.get(parent).size());
                    if (cover.getCoverName().equalsIgnoreCase("Camera")) {//Camera放在最前面
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
                new PositionScanTask().execute();
            }
        }.execute();
    }

    public static void savePositionPhoto() {
        MultiMap<String, PhotoMsg> map = BaseApplication.getInstance().getPhotoMsg();
        if (map.get("Camera") != null) {
            List<PhotoMsg> positionList = new LinkedList<>(map.get("Camera"));
            NetUtil.getCitys(positionList);
            List<PhotoMsg> cameraList = map.get("Camera");

            MultiMap<String, PhotoMsg> posMap = BaseApplication.getInstance().getPositionMap();
            if (!posMap.isEmpty())
                return;//// TODO: 2016/12/7 如果第一次没有获取全，则会有bug
            for (int i = 0; i < cameraList.size(); ++i) {
                PhotoMsg msg = cameraList.get(i);
                posMap.put(msg.getCity(), msg);
            }
            List<PositionCover> positionCovers = BaseApplication.getInstance().getPositionCoverList();
            if (!positionCovers.isEmpty())
                positionCovers.clear();
            Set<String> keys = posMap.keySet();
            for (String key : keys) {
                List<PhotoMsg> msgs = posMap.get(key);
                PositionCover pCover = new PositionCover(key, msgs.get(msgs.size() - 1).getAbsolutePath(), msgs.size());
                positionCovers.add(pCover);
            }
        }
    }

    public static void updateFileByPath(List<String> filePathList) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        for (int i = 0; i < filePathList.size(); ++i) {
            Uri data = Uri.fromFile(new File(filePathList.get(i)));
            intent.setData(data);
            BaseApplication.getInstance().sendBroadcast(intent);
        }
    }

    public static void deleteImageFromDatabase(String imgPath) {
        ContentResolver resolver = BaseApplication.getInstance().getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            BaseApplication.getInstance().getContentResolver().delete(uri, null, null);
        }
    }
}
