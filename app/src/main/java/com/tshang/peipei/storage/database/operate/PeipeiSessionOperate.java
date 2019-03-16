package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.table.PeiPeiSessionTable;

/**
 * @Title: 会话列表表操作类
 *
 * @Description: 在获取操作类对象，增删改查方式
 *
 * @author allen
 *
 * @version V1.0   
 */
public class PeipeiSessionOperate extends PeiPeiDatabaseOperate {

	private static PeipeiSessionOperate sessionDB;

	public PeipeiSessionOperate(Context context) {
		super(context);
	}

	public static PeipeiSessionOperate getInstance(Context context) {
		if (sessionDB == null) {
			synchronized (PeipeiSessionOperate.class) {
				if (sessionDB == null) {
					sessionDB = new PeipeiSessionOperate(context);
				}
			}
		}
		return sessionDB;
	}

	public static void closeSession() {
		sessionDB = null;
	}

	/**
	 * 插入数据库
	 */
	private static final String insert_sql = "INSERT INTO " + PeiPeiSessionTable.TABLE_NAME + " (" + PeiPeiSessionTable.TableVer + ","
			+ PeiPeiSessionTable.UserID + "," + PeiPeiSessionTable.UnreadCount + "," + PeiPeiSessionTable.SessionData + ","
			+ PeiPeiSessionTable.LatestUpdateTime + "," + PeiPeiSessionTable.Sex + "," + PeiPeiSessionTable.Nick + "," + PeiPeiSessionTable.Type
			+ ") VALUES (?,?,?,?,?,?,?,?)";

	public void insert(SessionDatabaseEntity sessionEntity) {
		//		BaseLog.d("sql_log", "sessiondb insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, sessionEntity.getUserID(), sessionEntity.getUnreadCount(),
				sessionEntity.getSessionData(), sessionEntity.getLatestUpdateTime(), sessionEntity.getSex(), sessionEntity.getNick(),
				sessionEntity.getType() };

		this.execSql(insert_sql, objs);
	}

	/**
	 * 查找聊天列表
	 * 
	 * @param list 保存查询到的数据
	 * @return 返回条数
	 */

	private static final String query_all_session_sql = "SELECT " + PeiPeiSessionTable.UserID + "," + PeiPeiSessionTable.UnreadCount + ","
			+ PeiPeiSessionTable.SessionData + "," + PeiPeiSessionTable.LatestUpdateTime + "," + PeiPeiSessionTable.Sex + ","
			+ PeiPeiSessionTable.Nick + "," + PeiPeiSessionTable.Type + " FROM " + PeiPeiSessionTable.TABLE_NAME + " WHERE "
			+ PeiPeiSessionTable.UserID + " != " + BAConstants.PEIPEI_BROADCASET + " ORDER BY " + PeiPeiSessionTable.LatestUpdateTime + " DESC";

	public ArrayList<SessionDatabaseEntity> selectChatList() {
		ArrayList<SessionDatabaseEntity> list = new ArrayList<SessionDatabaseEntity>();
		Cursor cursor = null;
		try {
			cursor = rawQuery(query_all_session_sql, null);

			if (cursor != null) {
				while (cursor.moveToNext()) {
					SessionDatabaseEntity sessionEntity = new SessionDatabaseEntity();
					sessionEntity.setUserID(cursor.getInt(0));
					sessionEntity.setUnreadCount(cursor.getInt(1));
					sessionEntity.setSessionData(cursor.getString(2));
					sessionEntity.setLatestUpdateTime(cursor.getLong(3));
					sessionEntity.setSex(cursor.getInt(4));
					sessionEntity.setNick(cursor.getString(5));
					sessionEntity.setType(cursor.getInt(6));

					if (!TextUtils.isEmpty(cursor.getString(5))) {//取不到昵称抛弃数据
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
	 * 删除数据
	 * @param userId 传入的userId
	 */
	public void deleteByUserID(int userId, int type) {
		String sql = "DELETE FROM " + PeiPeiSessionTable.TABLE_NAME + " WHERE " + PeiPeiSessionTable.UserID + " = " + userId + " AND "
				+ PeiPeiSessionTable.Type + " = " + type;

		//		BaseLog.d("sql_log", "session deleteByUserID =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级未读的数据 
	 * @param UnreadCount 未读的条数
	 * @param userId 传入对应的userId
	 */
	public void updateUnreadCount(int UnreadCount, int userId, int type) {
		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.UnreadCount + " = " + UnreadCount + " WHERE "
				+ PeiPeiSessionTable.UserID + " = " + userId + " AND " + PeiPeiSessionTable.Type + " = " + type;

		this.execSql(sql);
	}

	/**
	 * 升级未读的数据 
	 * @param UnreadCount 未读的条数
	 * @param userId 传入对应的userId
	 */
	private static final String clean_unread_sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.UnreadCount + " = " + 0;

	public void cleanUnreadCount() {
		//		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.UnreadCount + " = " + 0;

		//		BaseLog.d("sql_log", "session cleanUnreadCount =" + sql);
		this.execSql(clean_unread_sql);
	}

	public int getUnreadCount(int userId, int type) {
		int num = 0;
		Cursor cursor = null;
		try {
			String sql = "SELECT " + PeiPeiSessionTable.UnreadCount + " FROM " + PeiPeiSessionTable.TABLE_NAME + " WHERE "
					+ PeiPeiSessionTable.UserID + " = " + userId + " AND " + PeiPeiSessionTable.Type + " = " + type;

			//			BaseLog.d("sql_log", "session getUnreadCount =" + sql);
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
	private static final String get_unread_count_sql = "SELECT SUM(" + PeiPeiSessionTable.UnreadCount + ") FROM " + PeiPeiSessionTable.TABLE_NAME;

	public int selectUnreadCount() {
		int num = 0;
		Cursor cursor = null;
		try {
			//			String sql = "SELECT SUM(" + PeiPeiSessionTable.UnreadCount + ") FROM " + PeiPeiSessionTable.TABLE_NAME;

			//			BaseLog.d("sql_log", "session selectUnreadCount =" + sql);
			cursor = rawQuery(get_unread_count_sql, null);

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
	public void updateTime(int LatestUpdateTime, int userId, int type) {
		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.LatestUpdateTime + " = " + LatestUpdateTime + " WHERE "
				+ PeiPeiSessionTable.UserID + " = " + userId + " AND " + PeiPeiSessionTable.Type + " = " + type;

		this.execSql(sql);
	}

	/**
	 * 升级会话数据
	 * @param SessionData 会话数据
	 * @param userId 传入的对应userid
	 */
	public void updateSessionData(String SessionData, int userId, String nick, long lastUpdateTime, int type) {
		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.SessionData + " = \'" + SessionData + "\' , "
				+ PeiPeiSessionTable.LatestUpdateTime + " = " + lastUpdateTime + " , " + PeiPeiSessionTable.Nick + " = \'" + nick + "\' " + " WHERE "
				+ PeiPeiSessionTable.UserID + " = " + userId + " AND " + PeiPeiSessionTable.Type + " = " + type;

		this.execSql(sql);
	}

	/**
	 * 升级会话数据
	 * @param SessionData 会话数据
	 * @param userId 传入的对应userid
	 */
	public void updateSessionData(String SessionData, int userId, int type) {
		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.SessionData + " = \'" + SessionData + "\'  "
				+ " WHERE " + PeiPeiSessionTable.UserID + " = " + userId + " AND " + PeiPeiSessionTable.Type + " = " + type;

		this.execSql(sql);
	}

	//获取session数据
	public SessionDatabaseEntity selectSessionDate(int userId, int type) {
		SessionDatabaseEntity sessionEntity = new SessionDatabaseEntity();
		String sql = "SELECT " + PeiPeiSessionTable.UserID + "," + PeiPeiSessionTable.UnreadCount + "," + PeiPeiSessionTable.SessionData + ","
				+ PeiPeiSessionTable.LatestUpdateTime + "," + PeiPeiSessionTable.Sex + "," + PeiPeiSessionTable.Nick + "," + PeiPeiSessionTable.Type
				+ " FROM " + PeiPeiSessionTable.TABLE_NAME + " WHERE " + PeiPeiSessionTable.UserID + " = " + userId + " AND "
				+ PeiPeiSessionTable.Type + " = " + type;

		//		BaseLog.d("sql_log", "session selectSessionDate = " + sql);

		Cursor cursor = null;
		try {
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				sessionEntity.setUserID(cursor.getInt(0));
				sessionEntity.setUnreadCount(cursor.getInt(1));
				sessionEntity.setSessionData(cursor.getString(2));
				sessionEntity.setLatestUpdateTime(cursor.getLong(3));
				sessionEntity.setSex(cursor.getInt(4));
				sessionEntity.setNick(cursor.getString(5));
				sessionEntity.setType(cursor.getInt(6));
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
		return sessionEntity;
	}

	public void updateNickAndSex(int uid, String nick, int sex, int type) {
		String sql = "UPDATE " + PeiPeiSessionTable.TABLE_NAME + " SET " + PeiPeiSessionTable.Nick + " = \'" + nick + "\' , "
				+ PeiPeiSessionTable.Sex + " = " + sex + " WHERE " + PeiPeiSessionTable.UserID + " = " + uid + " AND " + PeiPeiSessionTable.Type
				+ " = " + type;

		this.execSql(sql);
	}

	/**
	 * 判断是否存在该会话
	 * @param userId 对应的好友id
	 * @return true：存在；false：不存在
	 */
	public boolean isHaveSession(int userId, int type) {
		boolean hasSession = false;
		Cursor cursor = null;
		try {
			String[] columns = { PeiPeiSessionTable.UserID };
			String[] whereArgs = { String.valueOf(userId), String.valueOf(type) };
			cursor = query(PeiPeiSessionTable.TABLE_NAME, columns, PeiPeiSessionTable.UserID + "=?" + " AND " + PeiPeiSessionTable.Type + " =?",
					whereArgs, null, null);

			if (cursor != null && cursor.moveToNext()) {
				hasSession = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

		}
		return hasSession;
	}
}
