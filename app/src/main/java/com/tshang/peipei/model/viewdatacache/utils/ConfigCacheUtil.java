package com.tshang.peipei.model.viewdatacache.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.vender.common.util.FileUtils;

/**
 * @Title: ConfigCacheUtil.java 
 *
 * @Description: TODO网络请求数据缓存
 *
 * @author Jeff  
 *
 * @date 2014年10月8日 下午2:44:56 
 *
 * @version V1.3.0   
 */
public class ConfigCacheUtil {
	private static final String TAG = ConfigCacheUtil.class.getName();
	private static final String CACHDIR = "PeiPei/datacache/";

	/** 1秒超时时间 */
	public static final int CONFIG_CACHE_SHORT_TIMEOUT = 1000 * 60 * 10; // 10 分钟  

	/** 5分钟超时时间 */
	public static final int CONFIG_CACHE_MEDIUM_TIMEOUT = 1000 * 3600 * 2; // 2小时  

	/** 中长缓存时间 */
	public static final int CONFIG_CACHE_ML_TIMEOUT = 1000 * 60 * 60 * 12 * 1; // 1天  

	/** 最大缓存时间 */
	public static final int CONFIG_CACHE_MAX_TIMEOUT = 1000 * 60 * 60 * 24 * 7; // 7天  

	/** 
	 * CONFIG_CACHE_MODEL_LONG : 长时间(7天)缓存模式 <br> 
	 * CONFIG_CACHE_MODEL_ML : 中长时间(12小时)缓存模式<br> 
	 * CONFIG_CACHE_MODEL_MEDIUM: 中等时间(2小时)缓存模式 <br> 
	 * CONFIG_CACHE_MODEL_SHORT : 短时间(5分钟)缓存模式 
	 */
	public enum ConfigCacheModel {
		CONFIG_CACHE_MODEL_SHORT, CONFIG_CACHE_MODEL_MEDIUM, CONFIG_CACHE_MODEL_ML, CONFIG_CACHE_MODEL_LONG;
	}

	public static File getUrlCacheFile(Context context, String url) {
		if (url == null) {
			return null;
		}
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist) {
			return null;
		}
		String path = context.getExternalCacheDir().getAbsolutePath() + "/" + url;
		File file = new File(path);
		return file;
	}

	/** 
	 * 获取缓存 
	 * @param url 访问网络的URL 
	 * @return 缓存数据 
	 */
	public static boolean getUrlCacheisEffective(File file, ConfigCacheModel model) {

		long expiredTime = System.currentTimeMillis() - file.lastModified();
		Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
		// 1。如果系统时间是不正确的  
		// 2。当网络是无效的,你只能读缓存  
		if (NetworkUtils.isNetworkAvailable(BAApplication.getInstance().getApplicationContext())) {
			if (expiredTime < 0) {
				return false;
			}
			if (model == ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT) {
				if (expiredTime > CONFIG_CACHE_SHORT_TIMEOUT) {
					return false;
				}
			} else if (model == ConfigCacheModel.CONFIG_CACHE_MODEL_MEDIUM) {
				if (expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT) {
					return false;
				}
			} else if (model == ConfigCacheModel.CONFIG_CACHE_MODEL_ML) {
				if (expiredTime > CONFIG_CACHE_ML_TIMEOUT) {
					return false;
				}
			} else if (model == ConfigCacheModel.CONFIG_CACHE_MODEL_LONG) {
				if (expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT) {
					return false;
				}
			} else {
				if (expiredTime > CONFIG_CACHE_MAX_TIMEOUT) {
					return false;
				}
			}
		}

		return true;
	}

	/** 取SD卡路径 **/
	private static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
			return sdDir.toString();
		}
		return null;
	}

	/** 
	 * 设置缓存 
	 * @param data 
	 * @param url 
	 */
	public static void setUrlCache(Context context, byte[] data, String url) {

		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist) {
			return;
		}
		if (data == null) {
			return;
		}
		FileUtils.getFileFromBytes(data, context.getExternalCacheDir().getAbsolutePath() + "/" + url);
		//		File file = new File(getNetworkDataDir() + "/" + url);
		//
		//		try {
		//			file.createNewFile();
		//			// 创建缓存数据到磁盘，就是创建文件  
		//			FileUtils.writeTextFile(file, data);
		//		} catch (IOException e) {
		//			Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!");
		//			e.printStackTrace();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

	/**
	 * 缓存文件
	 * @author Jeff
	 *
	 * @param activity
	 * @return
	 */
	public static File getCacheFile(Activity activity, String fileNameStart) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			File file = getUrlCacheFile(activity, fileNameStart + info.uid.intValue());
			return file;
		}
		return null;
	}

	/** 
	 * 删除历史缓存文件 
	 * @param cacheFile 
	 */
	public static void clearCache(File cacheFile) {
		if (cacheFile == null) {
			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				try {
					File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/hulutan/cache/");
					if (cacheDir.exists()) {
						clearCache(cacheDir);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (cacheFile.isFile()) {
			cacheFile.delete();
		} else if (cacheFile.isDirectory()) {
			File[] childFiles = cacheFile.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				clearCache(childFiles[i]);
			}
		}
	}
}
