package com.tongan.learn.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * 2019-5-23
 */

class DialogUtil {

    public static void showPermissionDeniedDialog(final Activity context, String str) {
        new AlertDialog.Builder(context).setTitle("获取" + str + "权限被禁用")
                .setMessage("请在 设置-应用管理-权限管理 (将" + str + "权限打开)")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public static void showPermissionRemindDiaog(final Activity context, String str, final String[] deniedPermissions, final int requestCode) {
        new AlertDialog.Builder(context).setTitle("温馨提示")
                .setMessage("我们需要" + str + "才能正常使用该功能")
                .setNegativeButton("取消", null)
                .setPositiveButton("验证权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XPermissionUtils.requestPermissionsAgain(context, deniedPermissions,
                                requestCode);
                    }
                })
                .show();
    }


}