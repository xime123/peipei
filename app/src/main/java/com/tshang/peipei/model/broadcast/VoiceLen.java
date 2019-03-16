package com.tshang.peipei.model.broadcast;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @Title: VoiceLen.java 
 *
 * @Description: 获取音频文件的时长
 *
 * @author Jeff  
 *
 * @date 2014年8月14日 下午2:45:53 
 *
 * @version V1.0   
 */
public class VoiceLen {
	public static int GetVoiceInfo(Activity activyt, String mpFullname) {
		Cursor cursor = null;
		try {
			String selection = MediaStore.Audio.Media.DATA + " = ?"; // like
			//String path="/mnt/sdcard/music";
			String[] selectionArgs = { mpFullname };
			//String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
			//			String[] projection = {
			//			//MediaStore.Audio.Media._ID,
			//			MediaStore.Audio.Media.DURATION
			//			//MediaStore.Audio.Media.DATA, // --> Location
			//			//MediaStore.Audio.Media.DISPLAY_NAME,	
			//			};
			cursor = activyt.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			//			cursor = activyt.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
			//		 Cursor cursor2 = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, 
			//		 null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
			int duration = 0;
			if (cursor != null) {
				cursor.moveToFirst();
				duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
			}
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			return duration;
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			return 0;
		}
	} // 
}
