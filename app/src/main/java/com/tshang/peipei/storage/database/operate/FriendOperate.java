package com.tshang.peipei.storage.database.operate;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.FriendDatabaseEntity;
import com.tshang.peipei.storage.database.table.FriendTable;
import com.tshang.peipei.storage.database.table.SessionTable;

/**
 * @Title: 好友表操作类
 *
 * @Description: 在获取操作类对象，增删改查方式
 *
 * @author allen
 *
 * @version V1.0   
 */
public class FriendOperate extends PeiPeiDatabaseOperate {

	private static FriendOperate friendDB;

	public static void closeFriend(Context context) {
		friendDB = null;
	}

	public FriendOperate(Context context) {
		super(context);
	}

	public static FriendOperate getInstance(Context context) {
		if (BAApplication.mLocalUserInfo != null) {
			if (friendDB == null) {
				friendDB = new FriendOperate(context);
			} else {
				if (!String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()).equals(BAApplication.mLocalUserInfo.uid.intValue() + "")) {
					friendDB = new FriendOperate(context);
				}
			}
		}

		return friendDB;
	}

	/**
	 * 插入数据
	 * @param friendEntity 数据对象
	 */
	public void insert(FriendDatabaseEntity friendEntity) {
		FriendTable friendTable = new FriendTable();
		String sql = "INSERT INTO" + FriendTable.TABLE_NAME + " (" + friendTable.getTableVer() + "," + friendTable.getUserID() + ","
				+ friendTable.getAvatar() + "," + friendTable.getEmail() + "," + friendTable.getGender() + "," + friendTable.getLastChatTime() + ","
				+ friendTable.getMemo() + "," + friendTable.getMobile() + "," + friendTable.getMood() + "," + friendTable.getType() + ","
				+ friendTable.getUserName() + "," + friendTable.getUserNick() + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		BaseLog.d("sql_log", "friendDb insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, friendEntity.getUserID(), friendEntity.getAvatar(), friendEntity.getEmail(),
				friendEntity.getGender(), friendEntity.getLastChatTime(), friendEntity.getMemo(), friendEntity.getMobile(), friendEntity.getMood(),
				friendEntity.getType(), friendEntity.getUserName(), friendEntity.getUserNick() };

		this.execSql(sql, objs);
	}

	/**
	 * 升级数据
	 * @param friendEntity 数据对象
	 * @param userId 传入的对应好友id
	 */
	public void update(FriendDatabaseEntity friendEntity, int userId) {
		FriendTable friendTable = new FriendTable();
		String sql = "UPDATE " + FriendTable.TABLE_NAME + " SET " + friendTable.getAvatar() + " = " + friendEntity.getAvatar() + ", "
				+ friendTable.getEmail() + " = " + friendEntity.getEmail() + ", " + friendTable.getLastChatTime() + " = "
				+ friendEntity.getLastChatTime() + ", " + friendTable.getMemo() + " = " + friendEntity.getMemo() + ", " + friendTable.getMobile()
				+ " = " + friendEntity.getMobile() + ", " + friendTable.getMood() + " = " + friendEntity.getMood() + ", " + friendTable.getType()
				+ " = " + friendEntity.getType() + ", " + friendTable.getUserName() + " = " + friendEntity.getUserName() + ", "
				+ friendTable.getUserNick() + " = " + friendEntity.getUserNick();

		BaseLog.d("sql_log", "friend update =" + sql);
		this.execSql(sql);
	}

	/**
	 * 删除数据
	 * @param userId 对应的好友id
	 */
	public void delete(int userId) {
		FriendTable friendTable = new FriendTable();
		String sql = "DELETE FROM " + FriendTable.TABLE_NAME + " WHERE " + friendTable.getUserID() + " = " + userId;

		BaseLog.d("sql_log", "friend deleteByUserID =" + sql);
		this.execSql(sql);
	}

	/**
	 * 判断是否存在该好友
	 * @param userId 对应的好友id
	 * @return true：存在；false：不存在
	 */
	public boolean isHaveFriend(int userId) {
		Cursor cursor = null;
		String sql = null;
		FriendTable friendTable = new FriendTable();
		try {
			sql = "SELECT " + friendTable.getUserID() + " FROM " + FriendTable.TABLE_NAME + "WHERE " + friendTable.getUserID() + " = " + userId;

			BaseLog.d("sql_log", "friend isHaveSession =" + sql);
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

	/**
	 * 查询数据
	 * @param list 用于保存数据
	 * @return 数据条数
	 */
	public int selectFriendList(List<FriendDatabaseEntity> list) {
		Cursor cursor = null;
		String sql = null;
		FriendDatabaseEntity friendEntity = null;
		FriendTable friendTable = new FriendTable();
		try {
			sql = "SELECT " + friendTable.getUserID() + "," + friendTable.getAvatar() + "," + friendTable.getEmail() + "," + friendTable.getGender()
					+ ", " + friendTable.getLastChatTime() + ", " + friendTable.getMemo() + ", " + friendTable.getMobile() + ", "
					+ friendTable.getMood() + ", " + friendTable.getType() + ", " + friendTable.getUserName() + ", " + friendTable.getUserNick()
					+ "FROM " + SessionTable.TABLE_NAME + "ORDER BY " + friendTable.getLastChatTime() + " DESC";

			BaseLog.d("sql_log", "friend selectFriendList =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				friendEntity = new FriendDatabaseEntity();
				friendEntity.setUserID(cursor.getInt(0));
				friendEntity.setAvatar(cursor.getString(1));
				friendEntity.setEmail(cursor.getString(2));
				friendEntity.setGender(cursor.getInt(3));
				friendEntity.setLastChatTime(cursor.getInt(4));
				friendEntity.setMemo(cursor.getString(5));
				friendEntity.setMobile(cursor.getString(6));
				friendEntity.setMood(cursor.getString(7));
				friendEntity.setType(cursor.getInt(8));
				friendEntity.setUserName(cursor.getString(9));
				friendEntity.setUserNick(cursor.getString(10));

				list.add(friendEntity);
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
	 * 升级最后聊天时间
	 * @param LastChatTime 最后聊天时间
	 * @param userid 对应的好友id
	 */
	public void updateLastChatTime(int LastChatTime, int userid) {
		FriendTable friendTable = new FriendTable();
		String sql = "UPDATE " + FriendTable.TABLE_NAME + " SET " + friendTable.getLastChatTime() + " = " + LastChatTime + " WHERE "
				+ friendTable.getUserID() + " = " + userid;

		BaseLog.d("sql_log", "session updateLastChatTime =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级用户昵称
	 * @param UserNick 用户昵称
	 * @param userid 对应的好友id
	 */
	public void updateUserNick(String UserNick, int userid) {
		FriendTable friendTable = new FriendTable();
		String sql = "UPDATE " + FriendTable.TABLE_NAME + " SET " + friendTable.getUserNick() + " = " + UserNick + " WHERE "
				+ friendTable.getUserID() + " = " + userid;

		BaseLog.d("sql_log", "session updateUserNick =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级用户签名
	 * @param Mood 用户签名
	 * @param userid 对应的好友id
	 */
	public void updateMood(String Mood, int userid) {
		FriendTable friendTable = new FriendTable();
		String sql = "UPDATE " + FriendTable.TABLE_NAME + " SET " + friendTable.getMood() + " = " + Mood + " WHERE " + friendTable.getUserID()
				+ " = " + userid;

		BaseLog.d("sql_log", "session updateMood =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级用户头像
	 * @param Avatar 用户头像
	 * @param userid 对应的好友id
	 */
	public void updateAvatar(String Avatar, int userid) {
		FriendTable friendTable = new FriendTable();
		String sql = "UPDATE " + FriendTable.TABLE_NAME + " SET " + friendTable.getAvatar() + " = " + Avatar + " WHERE " + friendTable.getUserID()
				+ " = " + userid;

		BaseLog.d("sql_log", "session updateAvatar =" + sql);
		this.execSql(sql);
	}

	/**
	 * 获取用户头像
	 * @param userid 对应的好友id
	 * @return 返回头像数据
	 */
	public String getAvatarByUserID(int userid) {
		Cursor cursor = null;
		String sql = null;
		FriendTable friendTable = new FriendTable();
		try {
			sql = "SELECT " + friendTable.getAvatar() + "FROM " + SessionTable.TABLE_NAME + "WHERE " + friendTable.getUserID() + " = " + userid;

			BaseLog.d("sql_log", "friend getAvatarByUserID =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				return cursor.getString(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return null;
	}
}
