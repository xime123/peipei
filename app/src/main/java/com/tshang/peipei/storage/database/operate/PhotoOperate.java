package com.tshang.peipei.storage.database.operate;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.PhotoDatabaseEntity;
import com.tshang.peipei.storage.database.table.Phototable;

/**
 * @Title: PhotoOperate.java 
 *
 * @Description: 上传相片时,照片信息都会存入该表
 *
 * @author vactor
 *
 * @date 2014-4-4 上午11:21:15 
 *
 * @version V1.0   
 */
public class PhotoOperate extends PeiPeiDatabaseOperate {

	private static PhotoOperate mPhotoOperate;

	public static void closePhoto(Context context) {
		mPhotoOperate = null;
	}

	public PhotoOperate(Context mContext) {
		super(mContext);
	}

	public static PhotoOperate getInstance(Context context, String db_name) {
		if (mPhotoOperate == null) {
			mPhotoOperate = new PhotoOperate(context);
		} else {
			if (!String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()).equals(db_name)) {
				mPhotoOperate = new PhotoOperate(context);
			}
		}
		return mPhotoOperate;
	}

	public void insertPhoto(PhotoDatabaseEntity photo) {
		Phototable phototable = new Phototable();
		String sql = "INSERT INTO " + Phototable.TABLE_NAME + " ( " + phototable.getTableVer() + "," + phototable.getUserId() + ","
				+ phototable.getNickName() + "," + phototable.getGender() + "," + phototable.getTitle() + "," + phototable.getDesc() + ","
				+ phototable.getProvince() + "," + phototable.getCity() + "," + phototable.getDetailAddress() + "," + phototable.getImageKeys() + ","
				+ phototable.getStatus() + "," + phototable.getAlbumId() + "," + phototable.getErrorcode() + "," + phototable.getTotal() + ","
				+ phototable.getCreatetime() + " )" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		BaseLog.d("sql_log", "phototable insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, photo.getUserId(), photo.getNickName(), photo.getGender(), photo.getTitle(),
				photo.getDesc(), photo.getProvince(), photo.getCity(), photo.getDetailAddress(), photo.getImageKeys(), photo.getStatus(),
				photo.getAlbumId(), photo.getErrorCode(), photo.getTotal(), photo.getCreatetime() };
		this.execSql(sql, objs);
	}

	/**
	 * 更新状态,以及图片key,(上传的过程中,时时更新imagekeys为剩下的图片路径,多张情况下以";"为分隔符)
	 * @author vactor
	 *
	 * @param status
	 * @param imagekyes
	 * @param albumId
	 * @param time
	 */
	public void updatePhotoStatus(int status, String imagekyes, int albumId, String time) {
		Phototable phototable = new Phototable();
		String sql = "UPDATE " + Phototable.TABLE_NAME + " SET " + phototable.getStatus() + " = ?, " + phototable.getImageKeys() + " = ? "
				+ " WHERE " + phototable.getAlbumId() + " = ? AND " + phototable.getCreatetime() + " =?";
		Object[] objs = new Object[] { status, imagekyes, albumId, time };
		this.execSql(sql, objs);
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param time
	 * @param albumId
	 */
	public void deletePhoto(String time, int albumId) {
		Phototable phototable = new Phototable();
		String sql = " DELETE FROM " + Phototable.TABLE_NAME + " WHERE " + phototable.getCreatetime() + " =? AND " + phototable.getAlbumId() + " = ?";
		this.execSql(sql, new String[] { time, albumId + "" });
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param status
	 * @param albumId
	 */
	public void deletePhoto() {
		String sql = " DELETE FROM " + Phototable.TABLE_NAME;
		this.execSql(sql);
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param status
	 * @param albumId
	 */
	public void deletePhoto(int albumId) {
		Phototable phototable = new Phototable();
		String sql = " DELETE FROM " + Phototable.TABLE_NAME + " WHERE " + phototable.getAlbumId() + " = ?";
		this.execSql(sql, new String[] { albumId + "" });
	}

	/**
	 * 根据 status 查询图片信息
	 * @author vactor
	 * @param status
	 * @return
	 */
	public PhotoDatabaseEntity getPhotoList(int status, int albumId) {
		Phototable phototable = new Phototable();
		String sql = "SELECT " + phototable.getUserId() + "," + phototable.getNickName() + "," + phototable.getGender() + "," + phototable.getTitle()
				+ "," + phototable.getDesc() + "," + phototable.getProvince() + "," + phototable.getCity() + "," + phototable.getDetailAddress()
				+ "," + phototable.getImageKeys() + "," + phototable.getStatus() + "," + phototable.getAlbumId() + "," + phototable.getErrorcode()
				+ "," + phototable.getCreatetime() + "," + phototable.getTotal() + " FROM " + Phototable.TABLE_NAME + " WHERE "
				+ phototable.getStatus() + " = ? AND " + phototable.getAlbumId() + " =? ";
		String[] objs = new String[] { status + "", albumId + "" };
		Cursor cursor = this.rawQuery(sql, objs);
		PhotoDatabaseEntity photo = null;
		try {
			if (cursor != null)
				if (cursor.moveToNext()) {
					photo = new PhotoDatabaseEntity();
					photo.setUserId(cursor.getInt(0));
					photo.setNickName(cursor.getString(1));
					photo.setGender(cursor.getInt(2));
					photo.setTitle(cursor.getString(3));
					photo.setDesc(cursor.getString(4));
					photo.setProvince(cursor.getString(5));
					photo.setCity(cursor.getString(6));
					photo.setDetailAddress(cursor.getString(7));
					photo.setImageKeys(cursor.getString(8));
					photo.setStatus(cursor.getInt(9));
					photo.setAlbumId(cursor.getInt(10));
					photo.setErrorCode(cursor.getInt(11));
					photo.setCreatetime(cursor.getString(12));
					photo.setTotal(cursor.getInt(13));
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return photo;
	}

	/**
	 * 根据 status 查询 记录是否存在
	 * @author vactor
	 * @param status
	 * @return
	 */
	public boolean isHaveUploading(int status1, int status2) {
		Phototable phototable = new Phototable();
		String sql = " SELECT COUNT(*) FROM " + Phototable.TABLE_NAME + " WHERE " + phototable.getStatus() + " =? OR " + phototable.getStatus()
				+ " =? ";
		String[] objs = new String[] { status1 + "", status2 + "" };
		Cursor cursor = this.rawQuery(sql, objs);
		int count = 0;
		try {
			cursor = this.rawQuery(sql, null);

			if (cursor != null && cursor.moveToNext()) {
				count = cursor.getInt(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return count > 0 ? true : false;
	}

	/**
	 * 根据 status 查询 记录是否存在
	 * @author vactor
	 * @param status
	 * @param albumId
	 * @return
	 */
	public boolean getPhotoStatus(int status1, int albumId) {
		Phototable phototable = new Phototable();
		String sql = " SELECT COUNT(*) FROM " + Phototable.TABLE_NAME + " WHERE " + phototable.getStatus() + " =? AND " + phototable.getAlbumId()
				+ " =?";
		String[] objs = new String[] { status1 + "", albumId + "" };
		Cursor cursor = this.rawQuery(sql, objs);
		int count = 0;
		try {
			cursor = this.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				count = cursor.getInt(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return count > 0 ? true : false;
	}

	/**
	 * 根据 status 查询 记录是否存在
	 * @author vactor
	 * @param status
	 * @return
	 */
	public boolean getPhotoStatus(int status1) {
		Phototable phototable = new Phototable();
		String sql = " SELECT COUNT(*) FROM " + Phototable.TABLE_NAME + " WHERE " + phototable.getStatus() + " =?";
		String[] objs = new String[] { status1 + "" };
		Cursor cursor = this.rawQuery(sql, objs);
		int count = 0;
		try {
			cursor = this.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				count = cursor.getInt(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return count > 0 ? true : false;
	}

}
