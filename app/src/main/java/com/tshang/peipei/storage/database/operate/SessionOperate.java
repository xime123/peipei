package com.tshang.peipei.storage.database.operate;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.table.SessionTable;

/**
 * @Title: 会话列表表操作类
 *
 * @Description: 在获取操作类对象，增删改查方式
 *
 * @author allen
 *
 * @version V1.0   
 */
public class SessionOperate extends PeiPeiDatabaseOperate {

	private static SessionOperate sessionDB;

	public SessionOperate(Context context) {
		super(context);
	}

	//	public static SessionOperate getInstance(Context context) {
	//		String dbname = BATools.getDB(context);
	//		if (sessionDB == null) {
	//			sessionDB = new SessionOperate(context);
	//		} else {
	//			if (!currdb.equals(dbname)) {
	//				sessionDB = new SessionOperate(context);
	//			}
	//		}
	//
	//		return sessionDB;
	//	}

	public static void closeSession(Context context) {
		sessionDB = null;
	}

	/**
	 * 插入数据库
	 */
	public void insert(SessionDatabaseEntity sessionEntity) {
		SessionTable sessionTable = new SessionTable();
		String sql = "INSERT INTO " + SessionTable.TABLE_NAME + " (" + sessionTable.getTableVer() + "," + sessionTable.getUserID() + ","
				+ sessionTable.getUnreadCount() + "," + sessionTable.getSessionData() + "," + sessionTable.getLatestUpdateTime()
				+ ") VALUES (?,?,?,?,?)";

		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, sessionEntity.getUserID(), sessionEntity.getUnreadCount(),
				sessionEntity.getSessionData(), sessionEntity.getLatestUpdateTime() };

		this.execSql(sql, objs);
	}

	/**
	 * 查找聊天列表
	 * 
	 * @param list 保存查询到的数据
	 * @return 返回条数
	 */
	public int selectChatList(List<SessionDatabaseEntity> list) {
		Cursor cursor = null;
		String sql = null;
		SessionDatabaseEntity sessionEntity = null;
		SessionTable sessionTable = new SessionTable();
		try {
			sql = "SELECT " + sessionTable.getUserID() + "," + sessionTable.getUnreadCount() + "," + sessionTable.getSessionData() + ","
					+ sessionTable.getLatestUpdateTime() + " FROM " + SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID() + " != "
					+ BAConstants.PEIPEI_BROADCASET + " ORDER BY " + sessionTable.getLatestUpdateTime() + " DESC";

			BaseLog.d("sql_log", "session selectChatList =" + sql);
			cursor = rawQuery(sql, null);

			if (cursor != null) {
				while (cursor.moveToNext()) {
					sessionEntity = new SessionDatabaseEntity();
					sessionEntity.setUserID(cursor.getInt(0));
					sessionEntity.setUnreadCount(cursor.getInt(1));
					sessionEntity.setSessionData(cursor.getString(2));
					sessionEntity.setLatestUpdateTime(cursor.getLong(3));

					list.add(sessionEntity);
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

		return list.size();
	}

	/**
	 * 删除数据
	 * @param userId 传入的userId
	 */
	public void deleteByUserID(int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "DELETE FROM " + SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "session deleteByUserID =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级未读的数据 
	 * @param UnreadCount 未读的条数
	 * @param userId 传入对应的userId
	 */
	public void updateUnreadCount(int UnreadCount, int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "UPDATE " + SessionTable.TABLE_NAME + " SET " + sessionTable.getUnreadCount() + " = " + UnreadCount + " WHERE "
				+ sessionTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "session updateUnreadCount =" + sql);
		this.execSql(sql);
	}

	public int getUnreadCount(int userId) {
		SessionTable sessionTable = new SessionTable();
		int num = 0;
		Cursor cursor = null;
		try {
			String sql = "SELECT " + sessionTable.getUnreadCount() + " FROM " + SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID()
					+ " = " + userId;

			BaseLog.d("sql_log", "session getUnreadCount =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {

				num = cursor.getInt(0);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return num;
	}

	//获取未读条数
	public int selectUnreadCount() {
		SessionTable sessionTable = new SessionTable();
		int num = 0;
		Cursor cursor = null;
		try {
			String sql = "SELECT SUM(" + sessionTable.getUnreadCount() + ") FROM " + SessionTable.TABLE_NAME;

			BaseLog.d("sql_log", "session selectUnreadCount =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {

				num = cursor.getInt(0);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return num;
	}

	/**
	 * 升级会话时间
	 * @param LatestUpdateTime 回话最后时间
	 * @param userId 传入对应的userid
	 */
	public void updateTime(int LatestUpdateTime, int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "UPDATE " + SessionTable.TABLE_NAME + " SET " + sessionTable.getLatestUpdateTime() + " = " + LatestUpdateTime + " WHERE "
				+ sessionTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "session updateTime =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级会话数据
	 * @param SessionData 会话数据
	 * @param userId 传入的对应userid
	 */
	public void updateSessionData(String SessionData, int userId, long lastUpdateTime) {
		SessionTable sessionTable = new SessionTable();
		String sql = "UPDATE " + SessionTable.TABLE_NAME + " SET " + sessionTable.getSessionData() + " = \'" + SessionData + "\' , "
				+ sessionTable.getLatestUpdateTime() + " = " + lastUpdateTime + " WHERE " + sessionTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "session updateSessionData1 =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级会话数据
	 * @param SessionData 会话数据
	 * @param userId 传入的对应userid
	 */
	public void updateSessionData(String SessionData, int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "UPDATE " + SessionTable.TABLE_NAME + " SET " + sessionTable.getSessionData() + " = \'" + SessionData + "\'  " + " WHERE "
				+ sessionTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "session updateSessionData2 =" + sql);
		this.execSql(sql);
	}

	//获取session数据
	public String selectSessionDate(int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "SELECT " + sessionTable.getSessionData() + " FROM " + SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID() + " = "
				+ userId;

		BaseLog.d("sql_log", "session selectSessionDate = " + sql);

		String sessionDate = "";
		Cursor cursor = null;
		try {
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {

				sessionDate = cursor.getString(0);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return sessionDate;
	}

	/**
	 * 升级数据
	 * @param sessionEntity 数据对象
	 * @param userId 传入的对应userid
	 */
	public void update(SessionDatabaseEntity sessionEntity, int userId) {
		SessionTable sessionTable = new SessionTable();
		String sql = "UPDATE " + SessionTable.TABLE_NAME + " SET " + sessionTable.getUnreadCount() + " = " + sessionEntity.getUnreadCount() + ", "
				+ sessionTable.getLatestUpdateTime() + " = " + sessionEntity.getLatestUpdateTime() + ", " + sessionTable.getSessionData() + " \'"
				+ sessionEntity.getSessionData() + " \'";

		BaseLog.d("sql_log", "session update =" + sql);
		this.execSql(sql);
	}

	/**
	 * 判断是否存在该会话
	 * @param userId 对应的好友id
	 * @return true：存在；false：不存在
	 */
	public boolean isHaveSession(int userId) {
		Cursor cursor = null;
		String sql = null;
		SessionTable sessionTable = new SessionTable();
		try {
			sql = "SELECT " + sessionTable.getUserID() + " FROM " + SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID() + " = " + userId;

			BaseLog.d("sql_log", "session isHaveSession =" + sql);
			cursor = this.rawQuery(sql, null);

			if (cursor != null && cursor.moveToNext()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			cursor = null;
		}
		return false;
	}
}
