package com.tshang.peipei.base;

import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tshang.peipei.R;
import com.tshang.peipei.activity.dialog.DialogFactory;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.xutils.DownloadManager;
import com.tshang.peipei.model.xutils.DownloadService;

/**
 * APK工具类
 * 
 * @author wuhezhi
 * 
 */
public class IAPKInfoUtil {
	/**
	 * 判断APK是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstallApk(Context context, String packageName) {
		boolean isGo = true;
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception e) {
			e.printStackTrace();
			packageInfo = null;
		}
		if (packageInfo == null) {
			isGo = false;
		} else {
			isGo = true;
		}
		return isGo;
	}

	/**
	 * 获取版本号
	 * @author Aaron
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static int getVersionCode(Context context, String packageName) {
		int code = -1;
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			code = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 启动第三方运用[如果系统未安装启动会报错]
	 * 
	 * @param context
	 * @param packageName
	 *            入口名
	 */
	public static boolean startAPK(Context context, String packageName, Bundle bundle) {
		boolean isGo = true;
		try {
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
			//			context.getPackageManager().get
			if (intent != null && bundle != null) {
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			isGo = false;
			Toast.makeText(context, "应用未安装", 1).show();
		}
		return isGo;
	}

	/**
	 * 启动APK
	 * @author Aaron
	 *
	 * @param context
	 * @param componentName
	 * @param bundle
	 * @return
	 */
	public static boolean startApk(Context context, ComponentName componentName, Bundle bundle) {
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(componentName);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "启动有误", 1).show();
			return false;
		}
	}

	/**
	 * 代码安装APK
	 * 
	 * @param context
	 * @param path
	 */
	public static void InstallAPk(Context context, String path) {
		// chmod("777", path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 获取权限
	 * 
	 * @param permission
	 *            权限
	 * @param path
	 *            路径
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * APK下载
	 * @author Aaron
	 *
	 * @param context
	 * @param path
	 * @param file
	 */
	public static void APkDownLoad(final Context context, String path, String fileName) {
		View view = LayoutInflater.from(context).inflate(R.layout.progress_bar_layout, null);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		final Dialog dialog = DialogFactory.showProgressViewDialog(context, view);
		DialogFactory.showDialog(dialog);
		DownloadManager manager = DownloadService.getDownloadManager(context.getApplicationContext());
		try {
			manager.addNewDownload(path, "game", fileName, true, false, new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					DialogFactory.dimissDialog(dialog);
					IAPKInfoUtil.InstallAPk(context, IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH));
					DialogFactory.dimissDialog(dialog);
					Toast.makeText(context, "下载失败", 1).show();
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					Log.d("Aaron", "下载===" + (current * 100 / total) + "%");
					progressBar.setProgress((int) (current * 100 / total));
				}

			});
		} catch (DbException e) {
			BaseFile.delete(new File(IFileUtils.getSDCardRootDirectory() + BAConstants.PEIPEI_GAME_NIUNIUT_PATH));
			e.printStackTrace();
			DialogFactory.dimissDialog(dialog);
			Toast.makeText(context, "下载失败", 1).show();
		}
	}
}
