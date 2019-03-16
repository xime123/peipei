package com.tshang.peipei.base;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class BasePhone {
	/**
	 * 
	 * function: 得到屏幕宽度; designDate:2014-2-17 下午4:49:34; modifyDate: ;
	 * modifyAuthor: ; modifyContent: ;
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		return width;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels; // 屏幕高度（像素）
		return height;
	}

	/**
	 * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return "" if device ID is not
	 * available.
	 */
	public static String getMobileImei(Context context) {
		String imeiStr = BaseFile.getImeiByFile();
		if (TextUtils.isEmpty(imeiStr)) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (null != tm) {
				imeiStr = tm.getDeviceId();
			}
			if (TextUtils.isEmpty(imeiStr)) {
				imeiStr = "";
			}
			if (!TextUtils.isEmpty(imeiStr)) {
				BaseFile.saveImeiByFile(imeiStr);
			}
		}

		return imeiStr;
	}

	/**
	 * Check if OS version has a http URLConnection bug. See here for more
	 * information:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 * 
	 * @return
	 */
	public static boolean hasHttpConnectionBug() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
	}

	/**
	 * Check if OS version has built-in external cache dir method.
	 * 
	 * @return
	 */
	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 是否有sd卡
	 * @return
	 */
	public static boolean isStoregeDirectory() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;// 得到SD卡得路径
		} else {
			return false;
		}
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 */
	public static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (hasHttpConnectionBug()) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Check how much usable space is available at a given path.
	 * 
	 * @param path
	 *            The path to check
	 * @return The space available in bytes
	 */
	@SuppressWarnings("deprecation")
	public static long getUsableSpace(File path) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	/**
	 * 获取密度
	 * @param mContext
	 * @return
	 */
	public static float getScale(Context mContext) {
		float scale = mContext.getResources().getDisplayMetrics().density;
		return scale;
	}
}
