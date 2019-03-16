package com.tshang.peipei.base;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;

/**
 * 
 * @ClassName: IFileUtils
 * 
 * @Description: 文件工具类
 * 
 * @Author Aaron
 * 
 * @CreateDate 2015-4-3 上午10:53:41
 * 
 * @Version V1.0
 */
public class IFileUtils {
	/**
	 * 
	 * @Title: isExistSDCard
	 * 
	 * @Description: 判断SD是否存在
	 * 
	 * @return
	 */
	public static boolean isExistSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @Title: getSDCardRootDirectory
	 * 
	 * @Description: 获取SD卡根目录
	 * 
	 * @return
	 */
	public static String getSDCardRootDirectory() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 
	 * @Title: getMIMEType
	 * 
	 * @Description: 获取文件类型
	 * 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getMIMEType(File file) {
		String type = "*/*";
		String fileName = file.getName();

		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
		if (end == "") {
			return end;
		}

		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0])) {
				type = MIME_MapTable[i][1];
			}
		}
		return type;
	}

	/**
	 * 清除本应用所有的数据 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
//		for (String filePath : filepath) {
//			cleanCustomCache(filePath);
//		}
	}

	/**
	 * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}
	
	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}
	
	/**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	/**
	 * 清除指定目录文件(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param directory
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * 	清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) 
	 *  @param context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	
	
	/**
	 * 文件类型适配
	 * */
	public static String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" },
			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" },
			{ ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" },
			{ ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" },
			{ ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" },
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/x-zip-compressed" },
			{ "", "*/*" } };

}
