package com.tshang.peipei.storage.database.operate;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.table.AppreciateTable;

/**
 * @Title: PhotoOperate.java 
 *
 * @Description: 点赞操作类
 *
 * @author vactor
 *
 * @date 2014-4-22
 *
 * @version V1.0   
 */
public class AppreciateOperate extends PeiPeiDatabaseOperate {

	private static AppreciateOperate mAppreciateOperate;

	public static void closeAppreciate(Context context) {
		mAppreciateOperate = null;
	}

	public AppreciateOperate(Context mContext) {
		super(mContext);
	}

	public static AppreciateOperate getInstance(Context context, String db_name) {
		if (mAppreciateOperate == null) {
			mAppreciateOperate = new AppreciateOperate(context);
		} else {
			if (!String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()).equals(db_name)) {
				mAppreciateOperate = new AppreciateOperate(context);
			}
		}
		return mAppreciateOperate;
	}

	/**
	 * 插入点赞表
	 * @author vactor
	 *
	 * @param userId
	 * @param topicId
	 * @param topicUid
	 */
	public void insertPublish(int userId, int topicId, int topicUid) {
		AppreciateTable apprecitate = new AppreciateTable();
		String sql = "INSERT INTO " + AppreciateTable.TABLE_NAME + " ( " + apprecitate.getTableVer() + "," + apprecitate.getTopicId() + ","
				+ apprecitate.getUserId() + " , " + apprecitate.getTopicUid() + " )" + " VALUES (?,?,?,?)";
		BaseLog.d("sql_log", "appreciatetable insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, topicId, userId, topicUid };
		this.execSql(sql, objs);
	}

	/**
	 * 删除
	 * @author vactor
	 *
	 * @param topicId
	 */
	public void deleteTopicId(int topicId, int topicUid) {
		AppreciateTable apprecitate = new AppreciateTable();
		String sql = "DELETE  FROM " + AppreciateTable.TABLE_NAME + " WHERE " + apprecitate.getTopicId() + " = ? AND " + apprecitate.getTopicUid()
				+ " = ?";
		this.execSql(sql, new String[] { topicId + "", topicUid + "" });

	}

	/**
	 * 判断某条记录是否存在 
	 * @author vactor
	 *
	 * @param topicId
	 * @param topicUid
	 * @return
	 */
	public boolean isExist(int topicId, int topicUid) {
		AppreciateTable apprecitate = new AppreciateTable();
		String sql = " SELECT COUNT(*) FROM " + AppreciateTable.TABLE_NAME + " WHERE " + apprecitate.getTopicId() + " =? AND "
				+ apprecitate.getTopicUid() + " =? ";
		Cursor cursor = this.rawQuery(sql, new String[] { topicId + "", topicUid + "" });
		int count = 0;
		if (null != cursor && cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		if (null != cursor) {
			cursor.close();
		}
		return count > 0 ? true : false;
	}

}
