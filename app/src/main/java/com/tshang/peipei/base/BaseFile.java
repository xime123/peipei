package com.tshang.peipei.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Scanner;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;

import com.tshang.peipei.base.babase.BAConstants;

public class BaseFile {
	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	public static File getExternalCacheDir(Context context) {
		if (BasePhone.hasExternalCacheDir()) {
			File f = context.getExternalCacheDir();
			if (f != null)
				return f;
		}
		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory() + cacheDir);
	}

	/**
	 * 返回sd卡路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppAbsolutePath(Context context) {
		return context.getApplicationContext().getFilesDir().getAbsolutePath();
	}

	/**
	 * 返回指定文件，文件名为空返回软件基础文件夹
	 * 
	 * @param dir
	 * @param c
	 * @return
	 */
	public static File getStoregeDirectory(String dir, Context c) {
		String pathStr = "";
		if (TextUtils.isEmpty(dir)) {
			pathStr = File.separator + BAConstants.PEIPEI_FILE;
		} else {
			pathStr = File.separator + BAConstants.PEIPEI_FILE + File.separator + dir;
		}

		if (BasePhone.isStoregeDirectory()) {
			return new File(Environment.getExternalStorageDirectory() + pathStr);
		} else {
			return new File(getAppAbsolutePath(c) + pathStr);
		}
	}

	/**
	 * 将byte数组保存到指定文件
	 * 
	 * @param bb
	 * @param _file
	 * @return
	 */
	public static String saveByteToFile(byte[] bb, String _file) {
		File bitmapFile = null;
		FileOutputStream fos = null;
		try {
			bitmapFile = new File(_file);

			if (!bitmapFile.exists()) {
				bitmapFile.createNewFile();
			}

			fos = new FileOutputStream(bitmapFile);
			fos.write(bb);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmapFile.getAbsolutePath();
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytesByFilePath(File file) {
		byte[] buffer = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new WeakReference<byte[]>(new byte[1024]).get();
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();

			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytesByFilePath(String filePath) {
		byte[] buffer = null;

		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 4]).get();
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();

			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;
	}

	/**
	 * 获取指定文件的图片
	 * 
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromFile(String fileName) {
		Bitmap image = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565; // 默认是Bitmap.Config.ARGB_8888
			/* 下面两个字段需要组合使用 */
			options.inPurgeable = true;
			options.inInputShareable = true;
			image = BitmapFactory.decodeFile(fileName, options);
			//image = new WeakReference<Bitmap>(image).get();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return image;
	}

	public static String getSaveTempPath() {//获取历史存储文件目录
		return new StringBuilder().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator).append("peipei")
				.append(File.separator).append("imagetemp").toString();

	}

	public static String filenameTemp = "/temp.jpg";

	/** 将图片存入文件缓存 **/
	public static File getTempFile() {
		String dir = getSaveTempPath();
		File dirFile = new File(dir);
		if (!dirFile.exists())
			dirFile.mkdirs();
		File file = new File(dir + filenameTemp);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	/** 
	* Gets the corresponding path to a file from the given content:// URI 
	* @param selectedVideoUri The content:// URI to find the file path from 
	* @param contentResolver The content resolver to use to perform the query. 
	* @return the file path as a string 
	*/
	public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
		String filePath = "";
		String[] filePathColumn = { MediaColumns.DATA };

		Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
		//      也可用下面的方法拿到cursor  
		//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);  
		if (cursor != null) {
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		}
		return filePath;
	}

	public static byte[] getByteFromVocieFile(File file) {
		FileInputStream fis = null;
		byte[] buffer = null;
		try {
			fis = new FileInputStream(file);
			int length = fis.available();
			buffer = new WeakReference<byte[]>(new byte[length]).get();
			fis.read(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return buffer;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	public static void saveImeiByFile(String message) {
		PrintWriter pw = null;
		try {
			File files = new File(Environment.getExternalStorageDirectory() + "/peipei/.a");
			if (!files.exists()) {
				files.mkdirs();
			}
			File f = new File(files, ".a");

			if (!f.exists()) {//如果文件不存在,则新建.  
				f.createNewFile();
			}

			pw = new PrintWriter(new FileWriter(f), true);
			pw.println(message);//写入新日志.  
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getImeiByFile() {
		Scanner sc = null;
		try {
			File files = new File(Environment.getExternalStorageDirectory() + "/peipei/.a");
			if (!files.exists()) {
				files.mkdirs();
			}
			File f = new File(files, ".a");

			if (!f.exists()) {//如果文件不存在,则新建.  
				f.createNewFile();
			}

			sc = new Scanner(f);
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine())//先读出旧文件内容,并暂存sb中;  
			{
				sb.append(sc.nextLine());
			}
			sc.close();

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	* 递归删除目录下的所有文件及子目录下所有文件
	* @param dir 将要删除的文件目录
	*/
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
