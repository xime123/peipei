package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.PublishDatabaseEntity;
import com.tshang.peipei.storage.database.table.Publishtable;

/**
 * @Title: PhotoOperate.java 
 *
 * @Description: 写贴操作类
 *
 * @author vactor
 *
 * @date 2014-4-11 
 *
 * @version V1.0   
 */
public class PublishOperate extends PeiPeiDatabaseOperate {

	private static PublishOperate mPublishOperate;

	//	private static Publishtable mPublishTable;

	public static void closePublish(Context context) {
		mPublishOperate = null;
	}

	public PublishOperate(Context mContext) {
		super(mContext);
	}

	public static PublishOperate getInstance(Context context, String db_name) {
		if (mPublishOperate == null) {
			mPublishOperate = new PublishOperate(context);
		} else {
			if (!String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()).equals(db_name)) {
				mPublishOperate = new PublishOperate(context);
			}
		}
		return mPublishOperate;
	}

	/**
	 * 插入贴子数据
	 * @author vactor
	 * @param content 根据 type不同存不同的数据,文本:存内容;语音,图片均存路径
	 * @param type 帖子类型 文本,语音,图片.
	 * @param userId
	 * @param nickName
	 * @param gender
	 * @param province
	 * @param city
	 * @param imageKeys 图片,或者语音的路径,多个时,以";"号作为分隔符
	 * @param status
	 * @param createTime
	 */
	public void insertPublish(int userId, String topicId, String nickName, int gender, String province, String city, String content,
			String imageKeys, int type, int status, String createTime) {
		Publishtable publish = new Publishtable();
		String sql = "INSERT INTO " + Publishtable.TABLE_NAME + " ( " + publish.getTableVer() + "," + publish.getTopicId() + ","
				+ publish.getUserId() + " , " + publish.getNickName() + " , " + publish.getGender() + " , " + publish.getProvince() + " , "
				+ publish.getCity() + " , " + publish.getContent() + " , " + publish.getImageKeys() + " , " + publish.getStatus() + ","
				+ publish.getCreatetime() + " , " + publish.getType() + " )" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		BaseLog.d("sql_log", "phototable insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, topicId, userId, nickName, gender, province, city, content, imageKeys, status,
				createTime, type };
		this.execSql(sql, objs);
	}

	/**
	 * 根据topicId更新状态
	 * @author vactor
	 * @param status 状态
	 * @param imageKeys 图片,或者语音的路径,多张时以";"为分隔符
	 * @param topicId
	 */
	public void updateStatusByTopicId(int status, String imageKeys, String topicId) {
		Publishtable publish = new Publishtable();
		String sql = "UPDATE " + Publishtable.TABLE_NAME + " SET " + publish.getStatus() + " =?, " + publish.getImageKeys() + " =? " + " WHERE "
				+ publish.getTopicId() + " = ?";
		Object[] objs = new Object[] { status, imageKeys, topicId };
		this.execSql(sql, objs);
	}

	/**
	 * 根据时间更新状态
	 * @author vactor
	 * @param status 状态
	 * @param imageKeys 图片,或者语音的路径,多张时以";"为分隔符
	 * @param createTime
	 */
	public void updateStatusByTime(String topicId, String createTime) {
		Publishtable publish = new Publishtable();
		String sql = "UPDATE " + Publishtable.TABLE_NAME + " SET " + publish.getTopicId() + " =?  WHERE " + publish.getCreatetime() + " = ?";
		BaseLog.w("sql_log", "publish updateStatusByTime " + sql);
		Object[] objs = new Object[] { topicId, createTime.trim() };
		this.execSql(sql, objs);
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param topicId
	 */
	public void deleteTopicId(String topicId) {
		Publishtable publish = new Publishtable();
		String sql = "DELETE  FROM " + Publishtable.TABLE_NAME + " WHERE " + publish.getTopicId() + " = ?";
		this.execSql(sql, new String[] { topicId + "" });
	}

	public void deleteCurrentTime(String time) {
		Publishtable publish = new Publishtable();
		String sql = "DELETE  FROM " + Publishtable.TABLE_NAME + " WHERE " + publish.getCreatetime() + " = ?";
		this.execSql(sql, new String[] { time + "" });
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param topicId
	 */
	public void deleteTopic() {
		//		Publishtable publish = new Publishtable();
		String sql = "DELETE  FROM " + Publishtable.TABLE_NAME;
		this.execSql(sql);
	}

	/**
	 * 根据status查询得到所有的帖子
	 * @author vactor
	 *
	 * @param status
	 * @return
	 */
	public List<PublishDatabaseEntity> getPublishList(int status) {
		List<PublishDatabaseEntity> list = new

		ArrayList<PublishDatabaseEntity>();
		Publishtable publish = new Publishtable();
		String sql = "SELECT " + publish.getUserId() + " , " + publish.getNickName() + " , " + publish.getGender() + " , " + publish.getType()
				+ " , " + publish.getTopicId() + " , " + publish.getImageKeys() + " , " + publish.getContent() + " , " + publish.getStatus() + " , "
				+ publish.getCreatetime() + " FROM " + Publishtable.TABLE_NAME + " WHERE " + publish.getStatus() + " = ?";
		String[] objs = new String[] { status + "" };
		Cursor cursor = this.rawQuery(sql, objs);

		while (cursor.moveToNext()) {
			PublishDatabaseEntity publishEntity = new PublishDatabaseEntity();
			publishEntity.setUserId(cursor.getInt(0));
			publishEntity.setNickName(cursor.getString(1));
			publishEntity.setGender(cursor.getInt(2));
			publishEntity.setType(cursor.getInt(3));
			publishEntity.setTopicId(cursor.getString(4));
			publishEntity.setImageKeys(cursor.getString(5));
			publishEntity.setContent(cursor.getString(6));
			publishEntity.setStatus(cursor.getInt(7));
			publishEntity.setCreatetime(cursor.getString(8));
			list.add(publishEntity);
		}
		if (null != cursor) {
			cursor.close();
		}
		return list;
	}

	/**
	 * 根据status查询得到所有的帖子
	 * @author vactor
	 *
	 * @param status
	 * @return
	 */
	public List<PublishDatabaseEntity> getPublishList(int status1, int status2) {
		List<PublishDatabaseEntity> list = new

		ArrayList<PublishDatabaseEntity>();
		Publishtable publish = new Publishtable();
		String sql = "SELECT " + publish.getUserId() + " , " + publish.getNickName() + " , " + publish.getGender() + " , " + publish.getType()
				+ " , " + publish.getTopicId() + " , " + publish.getImageKeys() + " , " + publish.getContent() + " , " + publish.getStatus() + " , "
				+ publish.getCreatetime() + " FROM " + Publishtable.TABLE_NAME + " WHERE " + publish.getStatus() + " = ? OR " + publish.getStatus()
				+ " = ? ORDER BY ? DESC";
		String[] objs = new String[] { status1 + "", status2 + "", publish.getCreatetime() };
		Cursor cursor = this.rawQuery(sql, objs);

		while (cursor.moveToNext()) {
			PublishDatabaseEntity publishEntity = new PublishDatabaseEntity();
			publishEntity.setUserId(cursor.getInt(0));
			publishEntity.setNickName(cursor.getString(1));
			publishEntity.setGender(cursor.getInt(2));
			publishEntity.setType(cursor.getInt(3));
			publishEntity.setTopicId(cursor.getString(4));
			publishEntity.setImageKeys(cursor.getString(5));
			publishEntity.setContent(cursor.getString(6));
			publishEntity.setStatus(cursor.getInt(7));
			publishEntity.setCreatetime(cursor.getString(8));
			list.add(publishEntity);
		}
		if (null != cursor) {
			cursor.close();
		}
		return list;
	}

}
