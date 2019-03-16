package com.tshang.peipei.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.os.Environment;
import android.os.StatFs;

/**
 * @Title: SdCardUtils.java 
 *
 * @Description: Sd卡工具类
 *
 * @author Jeff 
 *
 * @date 2014年8月13日 上午11:21:42 
 *
 * @version V1.0   
 */
public class SdCardUtils {
	private static final String CACHDIR = "PeiPei/Audio/";
	private static final String MYVOICEDIR = "_myvoice";
	private static final String PUBLICVOICEDIR = "_publicvoice";
	private static final String VEDIO_DIR = "PeiPei/video";
	private static final String SMILE_VOICE = ".smile_voice/";
	private static final String LOADPIC_DIR = "/PeiPei/loadpic";

	private static final int MB = 1024 * 1024;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 20;
	private static final int SAVEFILECOUNT = 200;

	private SdCardUtils() {

	}

	private static SdCardUtils instance = null;

	public static SdCardUtils getInstance() {
		if (instance == null) {
			synchronized (SdCardUtils.class) {
				if (instance == null) {
					instance = new SdCardUtils();
				}
			}
		}
		return instance;
	}

	/** 将图片存入文件缓存 **/
	public String saveFile(byte[] b, int uid, String fileName) {
		String dir = getDirectory(uid);
		return saveFile(b, dir, fileName);
	}

	/** 将图片存入文件缓存 **/
	public String saveFile(byte[] b, String path, String fileName) {
		if (b == null) {
			return null;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return null;
		}

		File dirFile = new File(path);
		if (!dirFile.exists())
			dirFile.mkdirs();
		removeCache(path);
		File file = new File(path + "/" + fileName);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			outStream.write(b);
			outStream.flush();
			outStream.close();
			return file.getPath();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/** 将图片存入文件缓存 **/
	public boolean saveVideoFile(File file, String fileName) {
		if (file == null) {
			return false;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return false;
		}

		String dir = getVedioDirectory();
		File dirFile = new File(dir);
		if (!dirFile.exists())
			dirFile.mkdirs();
		File saveFile = new File(dir + "/" + fileName + ".mp4");
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			file.createNewFile();
			in = new java.io.FileInputStream(file);
			out = new FileOutputStream(saveFile);
			byte[] bt = new byte[1024];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}
			return true;

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件的个数超过一定量时就删除最早的那几个文件
	 */
	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}
		int fileCount = files.length;
		if (fileCount >= SAVEFILECOUNT) {
			Arrays.sort(files, new FileLastModifSort());

			for (int i = SAVEFILECOUNT - 1; i < files.length; i++) {
				files[i].delete();
			}
		}
		return true;
	}

	/** 修改文件的最后修改时间 **/
	public void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/** 计算sdcard上的剩余空间 **/
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/** 获得缓存目录 **/
	public String getDirectory(int uid) {
		String dir = getSDPath() + "/" + CACHDIR;
		if (uid > 0) {
			dir = dir + uid + MYVOICEDIR;
		} else {
			dir = dir + PUBLICVOICEDIR;
		}
		
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}

	public String getHaremVoiceDir(String dir) {
		return getSDPath() + "/" + CACHDIR + SMILE_VOICE + dir;
	}

	public String getLoadPicDir() {
		return getSDPath() + LOADPIC_DIR;
	}

	/** 获得聊天视频目录 **/
	public String getVedioDirectory() {
		return getSDPath() + "/" + VEDIO_DIR + "/chat";
	}

	/** 获得我的视频目录 **/
	public String getMyVedioDirectory() {
		File dirFile = new File(getSDPath() + "/" + VEDIO_DIR);
		if (!dirFile.exists())
			dirFile.mkdirs();
		return dirFile.getAbsolutePath();
	}

	/** 取SD卡路径 **/
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	public static boolean isExistSdCard() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 根据文件的最后修改时间进行排序
	 */
	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() < arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
