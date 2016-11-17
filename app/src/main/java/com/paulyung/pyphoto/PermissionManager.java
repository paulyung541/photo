package com.paulyung.pyphoto;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by yang on 2016/9/12.
 * paulyung@outlook.com
 * <p/>
 * example :
 * <p/>
 * PermissionManager.getInstants()
 * .addInitCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE, new InitOptions() {
 *
 * @Override doInit() {
 * //init op
 * }
 * })
 * ...
 */
public class PermissionManager {
    private static final String TAG = "_PermissionManager_";

    public static int REQUEST_CODE = 0x01;

    private static PermissionManager _INSTANTS;

    private boolean enableDialog = false;

    //权限对应的初始化操作
    private Map<String, InitOptions> mPermissionMap;

    private PermissionManager() {
        mPermissionMap = new HashMap<>();
    }

    public static PermissionManager getInstants() {
        if (_INSTANTS == null) {
            synchronized (PermissionManager.class) {
                if (_INSTANTS == null) {
                    _INSTANTS = new PermissionManager();
                }
            }
        }
        return _INSTANTS;
    }

    /**
     * 检查或者添加权限，如果已经有该权限，则执行使用该权限的具体初始化
     *
     * @param isSingle 是否是单个权限
     */
    public void cheackAndRequest(Activity activity, String[] permissions, boolean isSingle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isSingle) {
                for (String p : permissions) {
                    requestPermission(activity, p);
                }
            } else {
                requestMultiplePermission(activity, permissions);
            }
        } else {//6.0以下
            Set<String> keySet = mPermissionMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                InitOptions value = mPermissionMap.get(key);
                if (value != null) {
                    value.doInit();
                }
            }
        }
    }

    //请求单个权限
    private void requestPermission(Activity activity, String permission) {
        if (!(activity.checkSelfPermission(permission)
                == PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
        } else {
            if (mPermissionMap.containsKey(permission)) {
                if (mPermissionMap.get(permission) != null) {
                    mPermissionMap.get(permission).doInit();
                }
            }
        }
    }

    //请求多个权限
    private void requestMultiplePermission(Activity activity, String[] permissions) {
        ArrayList<String> permissionList = new ArrayList<>(Arrays.asList(permissions));
        for (String s : permissions) {
            if ((activity.checkSelfPermission(s)
                    == PackageManager.PERMISSION_GRANTED)) {
                permissionList.remove(s);
                if (mPermissionMap.containsKey(s)) {
                    mPermissionMap.get(s).doInit();
                }
            }
        }
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE);
        }
    }

    /**
     * 兼容性初始化
     * 若为6.0以下，则直接执行初始化操作
     */
    public PermissionManager addInitCompat(String permission, InitOptions initOptions) {
        mPermissionMap.put(permission, initOptions);
        return this;
    }

    public PermissionManager enableDialog(boolean enable) {
        enableDialog = enable;
        return this;
    }

    /**
     * 此函数放在 onRequestPermissionsResult 中执行
     */
    public void handleRequest(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (requestCode == REQUEST_CODE) {
                if (permissions.length > 0) {
                    for (int j = 0; j < permissions.length; ++j) {
                        if (grantResults[j] == PackageManager.PERMISSION_GRANTED) {//允许
                            if (mPermissionMap.containsKey(permissions[j])) {
                                mPermissionMap.get(permissions[j]).doInit();
                                mPermissionMap.remove(permissions[j]);
                            }
                        } else {
                            //用户选择了不再提醒
                            if (!activity.shouldShowRequestPermissionRationale(permissions[j])) {
                                //弹出自定义授权提示窗口
                                if (enableDialog) {
                                    showDialog(activity, permissions[j]);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据权限显示Dialog内容
     *
     * @param permission 权限
     */
    private void showDialog(final Activity activity, final String permission) {
        DialogInterface.OnClickListener comfirm = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getAppDetailSettingIntent(activity);
            }
        };

//        switch (permission) {
//            case Manifest.permission.WRITE_EXTERNAL_STORAGE://SD卡
//                PermissionDialog.makeDialog(activity,
//                        PermissionDialog.PermissionMsg.WRITE_EXTERNAL_STORAGE, comfirm).show();
//                break;
//            case Manifest.permission.ACCESS_FINE_LOCATION://位置
//                PermissionDialog.makeDialog(activity,
//                        PermissionDialog.PermissionMsg.ACCESS_FINE_LOCATION, comfirm).show();
//                break;
//            case Manifest.permission.CALL_PHONE://电话
//                PermissionDialog.makeDialog(activity,
//                        PermissionDialog.PermissionMsg.CALL_PHONE, comfirm).show();
//                break;
//            case Manifest.permission.CAMERA://相机
//                PermissionDialog.makeDialog(activity,
//                        PermissionDialog.PermissionMsg.CAMERA, comfirm).show();
//                break;
//        }
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 需要某个权限的初始化操作
     */
    public interface InitOptions {
        void doInit();
    }
}
