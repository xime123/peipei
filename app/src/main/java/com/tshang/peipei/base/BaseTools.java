package com.tshang.peipei.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import com.tshang.peipei.base.babase.BAConstants;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class BaseTools {

	/**
	 * 获取当前应用的版本号
	 */
	public static int getAppVersionCode(Context context) {
		// 获取packagemanager的实例
		try {
			PackageManager packageManager = context.getPackageManager();

			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (packInfo != null)
				return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	 
	/**
	 * 获取版本上一次升级时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getAppUpdateTime(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (packInfo != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
				return packInfo.lastUpdateTime;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 获取当前应用的版本名
	 */
	public static String getAppVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (packInfo != null)
				return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取版本号
	 * @author Jeff
	 *
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String version = "0.0.0";

		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	public static String getAppPackageName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			if (packInfo != null)
				return packInfo.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Get the memory class of this device (approx. per-app memory limit)
	 * 
	 * @param context
	 * @return
	 */
	public static int getMemoryClass(Context context) {
		return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	/**
	 * 用来判断服务是否后台运行
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean IsRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				IsRunning = true;
				break;
			}
		}
		return IsRunning;
	}

	// 安装apk文件
	public static void installApk(String filename, Context mContext) {
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type); // 设置数据类型
		mContext.startActivity(intent);
	}

	/**
	 * 保存音频
	 * 
	 * @param data
	 * @param id
	 * @param uid
	 * @return
	 */
	public static String[] saveAmr(byte[] data, int id, int uid, Context mContext) {

		String[] s = new String[2];
		File directory = BaseFile.getStoregeDirectory(BAConstants.PEIPEI_AUDIO_FILE, mContext);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		String file_name = "aduio_" + id + "_" + uid + ".amr";
		File fileAmr = new File(directory, file_name);

		FileOutputStream fos = null;

		MediaPlayer mMediaPlayer = null;
		try {
			fos = new FileOutputStream(fileAmr);
			fos.write(data);
			fos.flush();
			fos.close();

			s[0] = fileAmr.getAbsolutePath();

			FileInputStream fis = new FileInputStream(fileAmr);

			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(fis.getFD());
			mMediaPlayer.prepare();
			// mMediaPlayer.start();
			s[1] = (mMediaPlayer.getDuration() / 1000) + "";

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mMediaPlayer != null) {
				mMediaPlayer.release();
			}
			mMediaPlayer = null;
		}

		return s;
	}

	public static byte[] getMergeArray(byte[] al, byte[] bl) {
		byte[] a = al;
		byte[] b = bl;
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static byte[] getMD5Str(byte[] bs) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(bs);
		} catch (NoSuchAlgorithmException e) {
		}

		return messageDigest.digest();
	}

	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}

		// 16位加密，从第9位到25位
		return md5StrBuff.toString();

	}

	public static byte[] getMD5Str_8(byte[] bs) {
		MessageDigest messageDigest = null;
		byte[] b = new byte[8];

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(bs);
			byte[] bb = messageDigest.digest();
			System.arraycopy(bb, 8, b, 0, 8);
		} catch (NoSuchAlgorithmException e) {
		}

		return b;
	}

	/**
	 * 获取根目录下所有文件夹
	 * @param path
	 * @return
	 */
	public static List<File> listFileDirctory(File path) {
		List<File> list = new ArrayList<File>();
		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				list.add(file);
			}
		}
		return list;
	}

	/**
	 * 获取根目录该层下的图片,注意,该方法只获取根目录下一层文件夹的图片
	 * @param files
	 * @return
	 */
	public static List<String> getImageFromFile(File[] files) {
		List<String> list = new ArrayList<String>();
		if (files != null) {// 先判断目录是否为空，否则会报空指针   
			for (File file : files) {
				if (!file.isDirectory()) {
					String fileName = file.getName().toLowerCase();
					if (fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")) {
						String path = file.getAbsolutePath();
						list.add(path);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 循环获取目录下所有图片路径
	 * @param files
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> getFileName(File[] files) {
		List<String> list = new ArrayList<String>();
		if (files != null) {// 先判断目录是否为空，否则会报空指针   
			for (File file : files) {
				if (file.isDirectory()) {
					List listTemp = getFileName(file.listFiles());
					list.addAll(listTemp);
				} else {
					String fileName = file.getName().toLowerCase();
					if (fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")) {
						String path = file.getAbsolutePath();
						list.add(path);
					}
				}
			}
		}
		return list;
	}

	/**
	 * String[] 转 list
	 * @author vactor
	 *
	 * @param str
	 * @return
	 */
	public static List<String> getList(String[] str) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; null != str && i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	/**
	 * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节，这样就可以避免由于占位导致的排版混乱问题了。 半角转为全角的代码如下，只需调用即可。
	 * @author Administrator
	 *
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}
