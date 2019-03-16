package com.tshang.peipei.model.biz.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import android.text.TextUtils;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

/**
 * @Title: VedioUtils.java 
 *
 * @Description: 视频转换工具类
 *
 * @author Jeff  
 *
 * @date 2014年8月29日 上午10:56:45 
 *
 * @version V1.0   
 */
public class VideoUtils {
	public static int transfer(String ffmpegPath, String command) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("chmod 700 " + ffmpegPath);
			Process proc = rt.exec(command);
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null)
				System.out.println(line);

			int exitVal = proc.waitFor();
			return exitVal;//0说明转换成功
		} catch (Throwable t) {
			t.printStackTrace();
			return -1;
		}
	}

	/**
	 * 搜索所有的视频
	 * @author Jeff
	 *
	 * @param activity
	 * @return
	 */
	public static Map<String, List<Video>> getVideoMap(Activity activity) {
		Map<String, List<Video>> maps = new HashMap<String, List<Video>>();
		List<Video> mylist = new ArrayList<Video>();
		List<Video> allList = new ArrayList<Video>();
		if (activity != null && SdCardUtils.isExistSdCard()) {
			Cursor cursor = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				String myDir = SdCardUtils.getInstance().getMyVedioDirectory();
				while (cursor.moveToNext()) {
					int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
					String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
					String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
					String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
					String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
					String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
					String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
					final BigDecimal secondLen = new BigDecimal(duration / 1000.0).setScale(0, BigDecimal.ROUND_HALF_UP);
					Uri uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
					String thumbImg = "";
					if (uri != null) {
						thumbImg = uri.toString();
					}
					if (TextUtils.isEmpty(thumbImg)) {
						thumbImg = "";
					}
					//isMyVideo
					if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(displayName)
							&& (displayName.toLowerCase().endsWith(".3gp") || displayName.toLowerCase().endsWith(".mp4"))) {//只选择3gp和mp4的视频
						Video video = new Video(id, title, album, artist, displayName, mimeType, path, size, secondLen.longValue(), thumbImg);
						if (path.startsWith(myDir)) {//处理好的视频
							if (!path.contains(myDir + "/chat")) {
								mylist.add(video);
							}
						} else {//需要处理压缩的视频
							allList.add(video);
						}
					}
				}
				cursor.close();
			}
		}
		maps.put("myvideo", mylist);
		maps.put("allVideo", allList);
		return maps;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param activity 上下文
	 * @param uri 视频uri
	 * @param handler 消息队列
	 */
	public static void operateVideo(final Activity activity, Uri uri, BAHandler handler) {
		int uid = 0;//上报用户的uid
		if (BAApplication.mLocalUserInfo != null) {
			uid = BAApplication.mLocalUserInfo.uid.intValue();
		}
		if (uri == null) {
			return;
		}
		if (activity == null) {
			return;
		}
		if (!SdCardUtils.isExistSdCard()) {
			return;
		}
		String lastName = "";//上传文件的后缀名
		Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);//查询视频信息
		if (cursor != null && cursor.moveToNext()) {//摄像机的路径===/storage/sdcard0/DCIM/Camera/VID_20140829_102143.3gp
			final String filePath = cursor.getString(cursor.getColumnIndex(VideoColumns.DATA));//获取视频文件路径
			double timeLen = cursor.getInt(cursor.getColumnIndex(VideoColumns.DURATION)) / 1000.0;//获取时长
			final BigDecimal secondLen = new BigDecimal(timeLen).setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入2.1=2，2.5=3
			long StartTime = System.currentTimeMillis();
			try {//ffmepg的路径===/data/data/com.tshang.peipei/app_peipei_bin/ffmpeg
				handler.sendEmptyMessageDelayed(HandlerValue.CHAT_VEDIO_COMPRESSION_VALUE, 600);//弹出进度条开始压缩
				final String controllerPath = new FfmpegController(activity).getBinaryPath();
				String dir = SdCardUtils.getInstance().getMyVedioDirectory();
				if (TextUtils.isEmpty(dir)) {//-ss 00:00:00 -vsync 0  -t 00 : 00 : 15.530000
					handler.sendEmptyMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE);
					return;
				}
				String outFile = dir + "/" + System.currentTimeMillis() + ".mp4";
				if (!TextUtils.isEmpty(controllerPath)) {
					String _3gptoMp4 = controllerPath + " -y -i " + filePath + " -b:v 400k -vcodec mpeg4 -acodec mp2 -s 960x640 -f mp4 " + outFile;
					String mp4toMp4 = controllerPath + " -y -i " + filePath + " -b:v 400k -vcodec mpeg4 -acodec copy -s 960x640 -f mp4 " + outFile;
					if (secondLen.intValue() > 30) {
						_3gptoMp4 = controllerPath + "  -ss 00:00:00 -t 00:00:30 -y -i " + filePath
								+ " -b:v 400k -vcodec mpeg4 -acodec mp2 -s 960x640 -f mp4 " + outFile;
						mp4toMp4 = controllerPath + "  -ss 00:00:00 -t 00:00:30 -y -i " + filePath
								+ " -b:v 400k -vcodec mpeg4 -acodec copy -s 960x640 -f mp4 " + outFile;
					}
					lastName = filePath.substring(filePath.lastIndexOf("."));//获取文件后缀名
					int value = -1;
					long startTime = System.currentTimeMillis();
					if (lastName.toLowerCase().equals(".3gp")) {
						value = VideoUtils.transfer(controllerPath, _3gptoMp4);
					} else {
						value = VideoUtils.transfer(controllerPath, mp4toMp4);

					}
					try {
//						MobclickAgent.reportError(activity, "压缩的时间差==" + (System.currentTimeMillis() - startTime));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if (value == 0) {//视频压缩成功
						File finalFile = new File(outFile);
						long fileSize = 0;
						if (finalFile != null) {
							updateGallery(activity, finalFile.getAbsolutePath());
							fileSize = finalFile.length();
							SdCardUtils.getInstance().saveVideoFile(finalFile, getMd5ByFile(finalFile));
						}
						String strSize = getDataSize(fileSize);
						strSize += "," + outFile;//拼接大小和路径

						handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_SUCCESS_VALUE, strSize));
					} else {//视频压缩失败
						File file = new File(outFile);
						if (file.exists()) {//压缩失败，删除掉刚创建的文件
							file.delete();
						}
						handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE, "真正压缩失败了--uid==" + uid + "--"
								+ lastName));
					}
				} else {
					handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE, "文件路径不对--uid==" + uid + "--"
							+ lastName));
				}
			} catch (FileNotFoundException e) {
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE, "文件未找到--uid==" + uid + "--" + lastName));
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE, "读写异常--uid==" + uid + "--" + lastName));
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_VEDIO_COMPRESSION_FAILED_VALUE, "未知异常--uid==" + uid + "--" + lastName));
			} finally {
				cursor.close();
			}
		}
	}

	/** 
	 * 返回byte的数据大小对应的文本 
	 * @param size 
	 * @return 
	 */
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "b";
		} else if (size < 1024 * 1024) {
			float kbsize = size / 1024f;
			return formater.format(kbsize) + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			float mbsize = size / 1024f / 1024f;
			return formater.format(mbsize) + "MB";
		} else if (size < 1024 * 1024 * 1024 * 1024) {
			float gbsize = size / 1024f / 1024f / 1024f;
			return formater.format(gbsize) + "GB";
		} else {
			return "size: error";
		}
	}

	/** 
	 * 查找视频文件对应于MediaStore的Uri 
	 * @param file 视频文件  
	 * @return 
	 */

	public static Uri queryUriForVideo(Activity activity, File file) {
		if (activity == null) {
			return null;
		}
		if (file == null) {
			return null;
		}
		int id = getId(activity, file);
		if (id == -1) {
			return null;
		}
		return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
	}

	/**
	**  
	* 获得 指定视频文件F在MediaStore中对应的ID  
	* @param f  视频文件  
	* @return  对应ID  
	*/

	private static int getId(Activity activity, File f) {
		int id = -1;
		// MediaStore.Video.Media.DATA：视频文件路径；  
		// MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4  
		// MediaStore.Video.Media.TITLE: 视频标题 : testVideo  
		Cursor cursor = null;
		try {
			String[] mediaColumns = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
					MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DISPLAY_NAME };

			final String where = MediaStore.Video.Media.DATA + "=" + "?";

			cursor = activity.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, where, new String[] { f.getAbsolutePath() },
					null);
			if (cursor == null) {
				return -1;
			}

			if (cursor.moveToFirst()) {
				do {
					id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
					//sysVideoList.add(info);  
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//			if (cursor != null) {
			//				cursor.close();
			//				cursor = null;
			//			}
		}
		return id;

	}

	/**
	 * 对文件MD5加密
	 * @author Jeff
	 *
	 * @param file加密的视频文件，这样就可以解决重复上传问题
	 * @return
	 */
	public static String getMd5ByFile(File file) {
		String value = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	private static void updateGallery(Activity activity, String filename)//filename是我们的文件全名，包括后缀哦  
	{
		try {
			MediaScannerConnection.scanFile(activity, new String[] { filename }, null, new MediaScannerConnection.OnScanCompletedListener() {
				public void onScanCompleted(String path, Uri uri) {
					//                  Log.i("ExternalStorage", "Scanned " + path + ":");  
					//                  Log.i("ExternalStorage", "-> uri=" + uri);  
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
