package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.table.ChatTable;
import com.tshang.peipei.storage.database.table.SolitaireRedpacketTable;

/**
 * @Title: RedpacketOperate.java 
 *
 * @Description: 红包数据库操作类
 *
 * @author DYH  
 *
 * @date 2015-12-14 下午8:36:45 
 *
 * @version V1.0   
 */
public class RedpacketOperate extends PeiPeiDatabaseOperate{

	private static RedpacketOperate mRedpacketOperate;
	private String TABLE_NAME;

	public static void closeRedpacket(Context context) {
		mRedpacketOperate = null;
	}

	public RedpacketOperate(Context mContext, String tablename) {
		super(mContext);
		this.execSql(SolitaireRedpacketTable.getCreateSQL(tablename));
		TABLE_NAME = SolitaireRedpacketTable.TABLE_NAME + tablename;
	}

	/**
	 * 由于聊天表的表名是根据用户id生成的，所以必须传入表名进行判断是那张表，判断现在的操作类对象是否和传入的表名相同。
	 * 
	 * @author allen
	 *
	 * @param context 上下文
	 * @param db_name 库名
	 * @param tablename 类名
	 * @return 操作类对象
	 */
	public static RedpacketOperate getInstance(Context context, int fuid) {
		return new RedpacketOperate(context, String.valueOf(fuid));
	}
	
	/**
	 * 插入数据
	 * @param chatEntity
	 */
	public long insert(ChatDatabaseEntity chatEntity) {

		if (db == null) {
			db = database.getEuDb(true);
		}
		
		if(getCountById(chatEntity.getMesSvrID()) > 0){
			return -1;
		}
		
		ContentValues values = new ContentValues();
		values.put(SolitaireRedpacketTable.TableVer, BAConstants.PEIPEI_DB_VERSION);
		values.put(SolitaireRedpacketTable.CreateTime, chatEntity.getCreateTime());
		values.put(SolitaireRedpacketTable.Des, chatEntity.getDes());
		values.put(SolitaireRedpacketTable.FromID, chatEntity.getFromID());
		values.put(SolitaireRedpacketTable.Message, chatEntity.getMessage());
		values.put(SolitaireRedpacketTable.MesSvrID, chatEntity.getMesSvrID());
		values.put(SolitaireRedpacketTable.Progress, chatEntity.getProgress());
		values.put(SolitaireRedpacketTable.Status, chatEntity.getStatus());
		values.put(SolitaireRedpacketTable.Type, chatEntity.getType());
		values.put(SolitaireRedpacketTable.GroupId, chatEntity.getToUid());
		values.put(SolitaireRedpacketTable.revstr1, chatEntity.getRevStr1());
		values.put(SolitaireRedpacketTable.revstr2, chatEntity.getRevStr2());
		values.put(SolitaireRedpacketTable.revstr3, chatEntity.getRevStr3());

		return db.insert(TABLE_NAME, null, values);
	}

	/**
	 * 删除数据
	 * @param mesLocalID 本地自增id
	 */
	public void delete(int id) {
		String sql = "DELETE FROM \'" + TABLE_NAME + "\' WHERE " + SolitaireRedpacketTable.MesSvrID + " = " + id;
		BaseLog.d("sql_log", "chatdb delete =" + sql);
		this.execSql(sql);
	}
	
	/**
	 * 删除数据
	 * @param mesLocalID 本地自增id
	 */
	public void deleteAll() {
		String sql = "DELETE FROM '".concat(TABLE_NAME).concat("\'");
		BaseLog.d("sql_log", "chatdb deleteAll =" + sql);
		this.execSql(sql);
	}
	
	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCount() {
		String sql = "SELECT  COUNT(" + SolitaireRedpacketTable.CreateTime + ") FROM \'" + TABLE_NAME + "\'";
		//		BaseLog.d("sql_log", "chatdb getCount =" + sql);

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
	
	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCountById(String id) {
		String sql = "SELECT  COUNT(" + SolitaireRedpacketTable.CreateTime + ") FROM " + TABLE_NAME + " WHERE " + SolitaireRedpacketTable.MesSvrID + " = " + id;
		//		BaseLog.d("sql_log", "chatdb getCount =" + sql);
		
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
	
	/**
	 * 升级服务器ID，发送时先保存的服务器id不是正确的，得道回复值后重新赋值
	 * @param mesSvrID 服务器id
	 * @param mesocalID 本地id
	 */
	public void updateProgressByMesSvrID(String mesSvrID, int message) {
		String sql = "UPDATE " + TABLE_NAME + " SET " + SolitaireRedpacketTable.Progress + " = " + message + " WHERE " + SolitaireRedpacketTable.MesSvrID + " = \'" + mesSvrID
				+ "\'";

		BaseLog.d("sql_log", "chatdb updateMesSvrID =" + sql);
		this.execSql(sql);
	} 
	
	/**
	 * 更新消息值
	 * @param mesSvrID 服务器id
	 * @param message 消息
	 */
	public void updateMessageByMesSvrID(String mesSvrID, String message) {
		ContentValues values = new ContentValues();
		String[] args = {mesSvrID};
		values.put(SolitaireRedpacketTable.Message, message);
//		String sql = "UPDATE " + TABLE_NAME + " SET " + SolitaireRedpacketTable.Message + " = \'" + message + "\'" + " WHERE " + SolitaireRedpacketTable.MesSvrID + " = \'" + mesSvrID
//				+ "\'";
		
//		BaseLog.d("sql_log", "chatdb updateMesSvrID =" + sql);
//		this.execSql(sql);
		db.update(TABLE_NAME, values, SolitaireRedpacketTable.MesSvrID+"=?", args);
	} 
	
	/**
	 *
	 * @return
	 */
//	public List<ChatDatabaseEntity> getMessageList() {
//		List<ChatDatabaseEntity> list = new ArrayList<ChatDatabaseEntity>();
//		Cursor cursor = null;
//		String sql = null;
//		ChatDatabaseEntity chatEntity = null;
//		try {
//			sql = "SELECT " + SolitaireRedpacketTable.CreateTime + "," + SolitaireRedpacketTable.Des + "," + SolitaireRedpacketTable.FromID + "," + SolitaireRedpacketTable.MesLocalID + ", "
//					+ SolitaireRedpacketTable.Message + ", " + SolitaireRedpacketTable.MesSvrID + ", " + SolitaireRedpacketTable.Progress + ", " + SolitaireRedpacketTable.Status + ", " + SolitaireRedpacketTable.Type
//					+ " FROM \'" + TABLE_NAME + "\'";
//
//			BaseLog.d("sql_log", "chatdb getMessageList =" + sql);
//			cursor = rawQuery(sql, null);
//
//			while (cursor.moveToNext()) {
//				chatEntity = new ChatDatabaseEntity();
//				chatEntity.setCreateTime(cursor.getLong(0));
//				chatEntity.setDes(cursor.getInt(1));
//				chatEntity.setFromID(cursor.getInt(2));
//				chatEntity.setMesLocalID(cursor.getInt(3));
//				chatEntity.setMessage(cursor.getString(4));
//				chatEntity.setMesSvrID(cursor.getString(5));
//				chatEntity.setProgress(cursor.getInt(6));
//				chatEntity.setStatus(cursor.getInt(7));
//				chatEntity.setType(cursor.getInt(8));
//				list.add(chatEntity);
//				break;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return list;
//	}
	
	/**
	 * 获取数据

	 * @param start 起始位置
	 * @param num 条数
	 * @return 
	 */
	public List<ChatDatabaseEntity> getMessageList() {
		Cursor cursor = null;
		Cursor cursorData = null;
		List<ChatDatabaseEntity> list_temp = new ArrayList<ChatDatabaseEntity>();
		try {
			String sql = "SELECT " + ChatTable.MesLocalID + " FROM \'" + TABLE_NAME + "\' ORDER BY " + ChatTable.CreateTime;

			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String[] whereArgs = { String.valueOf(cursor.getInt(cursor.getColumnIndex(ChatTable.MesLocalID))) };
				cursorData = query(TABLE_NAME, null, ChatTable.MesLocalID + "=?", whereArgs, null, null);
				if (cursorData != null) {
					while (cursorData.moveToNext()) {
						ChatDatabaseEntity chatEntity = new ChatDatabaseEntity();
						chatEntity.setCreateTime(cursorData.getLong(cursorData.getColumnIndex(ChatTable.CreateTime)));
						chatEntity.setDes(cursorData.getInt(cursorData.getColumnIndex(ChatTable.Des)));
						chatEntity.setFromID(cursorData.getInt(cursorData.getColumnIndex(ChatTable.FromID)));
						chatEntity.setMesLocalID(cursorData.getInt(cursorData.getColumnIndex(ChatTable.MesLocalID)));
						chatEntity.setMessage(cursorData.getString(cursorData.getColumnIndex(ChatTable.Message)));
						chatEntity.setMesSvrID(cursorData.getString(cursorData.getColumnIndex(ChatTable.MesSvrID)));
						chatEntity.setProgress(cursorData.getInt(cursorData.getColumnIndex(ChatTable.Progress)));
						chatEntity.setStatus(cursorData.getInt(cursorData.getColumnIndex(ChatTable.Status)));
						chatEntity.setType(cursorData.getInt(cursorData.getColumnIndex(ChatTable.Type)));
						chatEntity.setToUid(cursorData.getInt(cursorData.getColumnIndex(ChatTable.GroupId)));
						chatEntity.setRevStr1(cursorData.getString(cursorData.getColumnIndex(ChatTable.revstr1)));
						chatEntity.setRevStr2(cursorData.getString(cursorData.getColumnIndex(ChatTable.revstr2)));
						chatEntity.setRevStr3(cursorData.getString(cursorData.getColumnIndex(ChatTable.revstr3)));
						list_temp.add(chatEntity);
						//						list_temp.add(0, chatEntity);
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
			if (cursorData != null) {
				cursorData.close();
				cursorData = null;
			}

		}
		Collections.reverse(list_temp);
		return list_temp;
	}

}
