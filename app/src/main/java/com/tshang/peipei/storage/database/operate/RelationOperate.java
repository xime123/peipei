package com.tshang.peipei.storage.database.operate;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.RelationEntity;
import com.tshang.peipei.storage.database.table.RelationshipTable;

/**
 * @Title: RelationOperate.java 
 *
 * @Description: 忠诚度数据库操作类 
 *
 * @author allen  
 *
 * @date 2014-12-20 下午3:59:33 
 *
 * @version V1.0   
 */
public class RelationOperate extends PeiPeiDatabaseOperate {
	private static RelationOperate sessionDB;

	private RelationOperate(Context mContext) {
		super(mContext);
	}

	public static RelationOperate getInstance(Context context) {
		if (sessionDB == null) {
			synchronized (RelationOperate.class) {
				if (sessionDB == null) {
					sessionDB = new RelationOperate(context);
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
	public void insert(RelationEntity sessionEntity) {
		String sql = "INSERT INTO " + RelationshipTable.TABLE_NAME + " (" + RelationshipTable.TableVer + "," + RelationshipTable.toUid + ","
				+ RelationshipTable.Relationship + "," + RelationshipTable.chatthreshold + "," + RelationshipTable.isUpdate + ","
				+ RelationshipTable.RelationshipTime + "," + RelationshipTable.verInt + "," + RelationshipTable.verStr + ") VALUES (?,?,?,?,?,?,?,?)";

		BaseLog.d("sql_log", "RelationOperate insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, sessionEntity.getToUid(), sessionEntity.getRelationship(),
				sessionEntity.getChatthreshold(), sessionEntity.getIsUpdate(), sessionEntity.getRelationshipTime(), sessionEntity.getVerInt(),
				sessionEntity.getVerStr() };

		this.execSql(sql, objs);
	}

	/**
	 * 送礼忠诚度变化升级
	 *
	 * @param uid
	 */
	public void updateRelation(int uid, int update) {
		String sql = "UPDATE " + RelationshipTable.TABLE_NAME + " SET " + RelationshipTable.isUpdate + " = " + update + " , "
				+ RelationshipTable.RelationshipTime + " = " + System.currentTimeMillis() + " WHERE " + RelationshipTable.toUid + " = " + uid;

		BaseLog.d("sql_log", "RelationOperate updateRelation =" + sql);
		this.execSql(sql);
	}

	/**
	 * 忠诚度不够升级
	 *
	 * @param uid
	 */
	public void updateRelation(int uid, int relationship, int chatthreshold) {
		String sql = "UPDATE " + RelationshipTable.TABLE_NAME + " SET " + RelationshipTable.Relationship + " = " + relationship + " , "
				+ RelationshipTable.chatthreshold + " = " + chatthreshold + " , " + RelationshipTable.RelationshipTime + " = "
				+ System.currentTimeMillis() + " WHERE " + RelationshipTable.toUid + " = " + uid;

		BaseLog.d("sql_log", "RelationOperate updateRelation2 =" + sql);
		this.execSql(sql);
	}

	/**
	 * 忠诚度不够升级
	 *
	 * @param uid
	 */
	public void updateRelationByShip(int uid, int relationship) {
		String sql = "UPDATE " + RelationshipTable.TABLE_NAME + " SET " + RelationshipTable.Relationship + " = " + relationship + " , "
				+ RelationshipTable.RelationshipTime + " = " + System.currentTimeMillis() + " WHERE " + RelationshipTable.toUid + " = " + uid;

		BaseLog.d("sql_log", "RelationOperate updateRelation3 =" + sql);
		this.execSql(sql);
	}

	/**
	 * 忠诚度不够升级
	 *
	 * @param uid
	 */
	public void updateRelationByChat(int uid, int chatthreshold) {
		String sql = "UPDATE " + RelationshipTable.TABLE_NAME + " SET " + RelationshipTable.chatthreshold + " = " + chatthreshold + " , "
				+ RelationshipTable.RelationshipTime + " = " + System.currentTimeMillis() + " WHERE " + RelationshipTable.toUid + " = " + uid;

		BaseLog.d("sql_log", "RelationOperate updateRelation4 =" + sql);
		this.execSql(sql);
	}

	/**
	 * 判断是否存在该会话
	 * @param userId 对应的好友id
	 * @return true：存在；false：不存在
	 */
	public boolean isHaveSession(int userId) {
		Cursor cursor = null;
		try {
			String sql = "SELECT " + RelationshipTable.toUid + " FROM " + RelationshipTable.TABLE_NAME + " WHERE " + RelationshipTable.toUid + " = "
					+ userId;

			BaseLog.d("sql_log", "RelationOperate isHaveSession =" + sql);
			cursor = this.rawQuery(sql, null);

			if (cursor != null && cursor.moveToNext()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

		}
		return false;
	}

	//获取session数据
	public RelationEntity selectSessionDate(int userId) {
		RelationEntity sessionEntity = new RelationEntity();
		String sql = "SELECT " + RelationshipTable.Relationship + "," + RelationshipTable.chatthreshold + "," + RelationshipTable.isUpdate + ","
				+ RelationshipTable.RelationshipTime + " FROM " + RelationshipTable.TABLE_NAME + " WHERE " + RelationshipTable.toUid + " = " + userId;

		BaseLog.d("sql_log", "RelationOperate selectSessionDate = " + sql);

		Cursor cursor = null;
		try {
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				sessionEntity.setRelationship(cursor.getInt(0));
				sessionEntity.setChatthreshold(cursor.getInt(1));
				sessionEntity.setIsUpdate(cursor.getInt(2));
				sessionEntity.setRelationshipTime(cursor.getLong(3));
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
}
