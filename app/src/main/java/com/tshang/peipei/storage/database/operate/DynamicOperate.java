package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.DynamicEntity;
import com.tshang.peipei.storage.database.table.DynamicTable;

/**
 * @Title: DynamicOperate.java 
 *
 * @Description: 我的动态操作类
 *
 * @author allen  
 *
 * @date 2014-12-3 上午10:34:55 
 *
 * @version V1.0   
 */
public class DynamicOperate extends PeiPeiDatabaseOperate {

	private static DynamicOperate sessionDB;

	private DynamicOperate(Context mContext) {
		super(mContext);
	}

	public static DynamicOperate getInstance(Context context) {
		if (sessionDB == null) {
			synchronized (PeipeiSessionOperate.class) {
				if (sessionDB == null) {
					sessionDB = new DynamicOperate(context);
				}
			}
		}
		return sessionDB;
	}

	public static void closeSession(Context context) {
		sessionDB = null;
	}

	/**
	 * 插入数据库
	 */
	public void insert(DynamicEntity sessionEntity) {
		String sql = "INSERT INTO " + DynamicTable.TABLE_NAME + " (" + DynamicTable.TableVer + "," + DynamicTable.UserID + ","
				+ DynamicTable.SessionData + "," + DynamicTable.LatestUpdateTime + "," + DynamicTable.Sex + "," + DynamicTable.Nick + ","
				+ DynamicTable.Topicid + "," + DynamicTable.TopicUid + "," + DynamicTable.Type + "," + DynamicTable.VerStr
				+ ") VALUES (?,?,?,?,?,?,?,?,?,?)";

		BaseLog.d("sql_log", "DynamicOperate insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, sessionEntity.getUserID(), sessionEntity.getSessionData(),
				sessionEntity.getLatestUpdateTime(), sessionEntity.getSex(), sessionEntity.getNick(), sessionEntity.getTopicId(),
				sessionEntity.getTopicUid(), sessionEntity.getType(), sessionEntity.getVerStr() };

		this.execSql(sql, objs);
	}

	/**
	 * 查找聊天列表
	 * 
	 * @param list 保存查询到的数据
	 * @return 返回条数
	 */
	public ArrayList<DynamicEntity> selectChatList(int start, int num) {
		ArrayList<DynamicEntity> list = new ArrayList<DynamicEntity>();
		Cursor cursor = null;
		try {
			String sql = "SELECT " + DynamicTable.UserID + "," + DynamicTable.SessionData + "," + DynamicTable.LatestUpdateTime + ","
					+ DynamicTable.Sex + "," + DynamicTable.Nick + "," + DynamicTable.Topicid + "," + DynamicTable.TopicUid + "," + DynamicTable.Type
					+ "," + DynamicTable.VerStr + " FROM " + DynamicTable.TABLE_NAME + " ORDER BY " + DynamicTable.LatestUpdateTime + " DESC  LIMIT "
					+ start + " , " + num;

			BaseLog.d("sql_log", "DynamicOperate selectChatList =" + sql);
			cursor = rawQuery(sql, null);

			if (cursor != null) {
				while (cursor.moveToNext()) {
					DynamicEntity sessionEntity = new DynamicEntity();
					sessionEntity.setUserID(cursor.getInt(0));
					sessionEntity.setSessionData(cursor.getString(1));
					sessionEntity.setLatestUpdateTime(cursor.getLong(2));
					sessionEntity.setSex(cursor.getInt(3));
					sessionEntity.setNick(cursor.getString(4));
					sessionEntity.setTopicId(cursor.getInt(5));
					sessionEntity.setTopicUid(cursor.getInt(6));
					sessionEntity.setType(cursor.getInt(7));
					sessionEntity.setVerStr(cursor.getString(8));

					if (!TextUtils.isEmpty(cursor.getString(4))) {//取不到昵称抛弃数据
						list.add(sessionEntity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}

		return list;
	}

	/**
	 * 删除
	 */
	public void deleteTable() {
		final String deleteSql = "DELETE FROM ".concat(DynamicTable.TABLE_NAME);
		try {
			this.execSql(deleteSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCount() {
		String sql = "SELECT  COUNT(" + DynamicTable.UserID + ") FROM " + DynamicTable.TABLE_NAME;
		BaseLog.d("sql_log", "DynamicOperate getCount =" + sql);

		int count = 0;
		Cursor cursor = null;

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

		return count;
	}
}
