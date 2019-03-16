package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.table.ChatTable;

/**
 * @Title: 聊天表操作类
 *
 * @Description: 在获取操作类对象，增删改查方式
 *
 * @author allen
 *
 * @version V1.0   
 */
public class ChatOperate extends PeiPeiDatabaseOperate {
	private String tablename = "";

	private ChatOperate(Context context) {
		super(context);
	}

	private ChatOperate(Context context, String tablename) {
		super(context);
		this.tablename = tablename;
		this.execSql(ChatTable.getCreateSQL(tablename));
		this.execSql("CREATE INDEX IF NOT EXISTS [CreateTime_idx] ON [" + tablename + "]([" + ChatTable.CreateTime + "])");
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
	public static ChatOperate getInstance(Context context, int fuid, boolean isGroupChat) {
		return new ChatOperate(context, getTableName(fuid, isGroupChat));
	}

	private static String getTableName(int fuid, boolean isGroupChat) {//获取表名,如果是群就是群UID，个人就是对方的uid
		if (isGroupChat) {
			return "chat_" + fuid + "G";
		}
		return "chat_" + fuid + "P";
	}

	/**
	 * 插入数据
	 * @param chatEntity
	 */
	public long insert(ChatDatabaseEntity chatEntity) {

		if (db == null) {
			db = database.getEuDb(true);
		}
		ContentValues values = new ContentValues();
		values.put(ChatTable.TableVer, BAConstants.PEIPEI_DB_VERSION);
		values.put(ChatTable.CreateTime, chatEntity.getCreateTime());
		values.put(ChatTable.Des, chatEntity.getDes());
		values.put(ChatTable.FromID, chatEntity.getFromID());
		values.put(ChatTable.Message, chatEntity.getMessage());
		values.put(ChatTable.MesSvrID, chatEntity.getMesSvrID());
		values.put(ChatTable.Progress, chatEntity.getProgress());
		values.put(ChatTable.Status, chatEntity.getStatus());
		values.put(ChatTable.Type, chatEntity.getType());
		values.put(ChatTable.GroupId, chatEntity.getToUid());
		values.put(ChatTable.revstr1, chatEntity.getRevStr1());
		values.put(ChatTable.revstr2, chatEntity.getRevStr2());
		values.put(ChatTable.revstr3, chatEntity.getRevStr3());
		
		return db.insert(tablename, null, values);
	}

	/**
	 * 删除数据
	 * @param mesLocalID 本地自增id
	 */
	public void delete(long mesLocalID) {
		String sql = "DELETE FROM \'" + tablename + "\' WHERE " + ChatTable.MesLocalID + " = " + mesLocalID;
		BaseLog.d("sql_log", "chatdb delete =" + sql);
		this.execSql(sql);
	}

	public void deletelimit(long mesLocalID) {
		String sql = "DELETE FROM \'" + tablename + "\' WHERE " + ChatTable.MesLocalID + " < " + mesLocalID;
		BaseLog.d("sql_log", "chatdb delete = 数据" + sql);
		this.execSql(sql);
	}

	/**
	 * 删除数据
	 * @param mesLocalID 本地自增id
	 */
	public void deleteByMesSerId(String MesSerId) {
		String sql = "DELETE FROM \'" + tablename + "\' WHERE " + ChatTable.MesSvrID + " = " + MesSerId;

		BaseLog.d("sql_log", "chatdb delete =" + sql);
		this.execSql(sql);
	}

	/**
	 * 升级数据
	 * @param chatEntity 数据对象
	 * @param MesSvrID 服务端的数据id
	 */
	public void update(ChatDatabaseEntity chatEntity, String MesSvrID) {
		String sql = "UPDATE \'" + tablename + "\' SET " + ChatTable.CreateTime + " = " + chatEntity.getCreateTime() + ", " + ChatTable.Des + " = "
				+ chatEntity.getDes() + ", " + ChatTable.FromID + " = " + chatEntity.getFromID() + ", " + ChatTable.Message + " = "
				+ chatEntity.getMessage() + ", " + ChatTable.MesSvrID + " = " + chatEntity.getMesSvrID() + ", " + ChatTable.Progress + " = "
				+ chatEntity.getProgress() + ", " + ChatTable.Status + " = " + chatEntity.getStatus() + ", " + ChatTable.Type + " = "
				+ chatEntity.getType() + " WHERE " + ChatTable.MesLocalID + " = " + chatEntity.getMesLocalID();

		this.execSql(sql);
	}

	/**
	 * @param Table_Name
	 * @param values
	 * @param WhereClause
	 * @param whereArgs
	 * @return 影响行数
	 */
	public int update(ContentValues values, String WhereClause, String[] whereArgs) {
		return db.update(tablename, values, WhereClause, whereArgs);
	}

	/**
	 * 获取数据

	 * @param start 起始位置
	 * @param num 条数
	 * @return 
	 */
	public List<ChatDatabaseEntity> selectChatList(int start, int num) {
		Cursor cursor = null;
		Cursor cursorData = null;
		List<ChatDatabaseEntity> list_temp = new ArrayList<ChatDatabaseEntity>();
		try {
			String sql = "SELECT " + ChatTable.MesLocalID + " FROM \'" + tablename + "\' ORDER BY " + ChatTable.CreateTime + " DESC  LIMIT " + start
					+ " , " + num;

			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String[] whereArgs = { String.valueOf(cursor.getInt(cursor.getColumnIndex(ChatTable.MesLocalID))) };
				cursorData = query(tablename, null, ChatTable.MesLocalID + "=?", whereArgs, null, null);
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

	/**
	 * 获取最后一条聊天数据
	 *
	 * @return
	 */
	public ChatDatabaseEntity getLastestMessage() {
		Cursor cursor = null;
		String sql = null;
		ChatDatabaseEntity chatEntity = null;

		try {
			sql = "SELECT " + ChatTable.CreateTime + "," + ChatTable.Des + "," + ChatTable.FromID + "," + ChatTable.MesLocalID + ", "
					+ ChatTable.Message + ", " + ChatTable.MesSvrID + ", " + ChatTable.Progress + ", " + ChatTable.Status + ", " + ChatTable.Type
					+ " FROM \'" + tablename + "\' ORDER BY " + ChatTable.MesLocalID + " DESC";

			BaseLog.d("sql_log", "chatdb selectChatList =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				chatEntity = new ChatDatabaseEntity();
				chatEntity.setCreateTime(cursor.getLong(0));
				chatEntity.setDes(cursor.getInt(1));
				chatEntity.setFromID(cursor.getInt(2));
				chatEntity.setMesLocalID(cursor.getInt(3));
				chatEntity.setMessage(cursor.getString(4));
				chatEntity.setMesSvrID(cursor.getString(5));
				chatEntity.setProgress(cursor.getInt(6));
				chatEntity.setStatus(cursor.getInt(7));
				chatEntity.setType(cursor.getInt(8));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return chatEntity;
	}

	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCount() {
		String sql = "SELECT  COUNT(" + ChatTable.CreateTime + ") FROM \'" + tablename + "\'";
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
	public void updateMesSvrID(String mesSvrID, int mesLocalID) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.MesSvrID + " = " + mesSvrID + " WHERE " + ChatTable.MesLocalID + " = " + mesLocalID;

		BaseLog.d("sql_log", "chatdb updateMesSvrID =" + sql);
		this.execSql(sql);
	}

	/**
	 * 获取消息状态
	 * @param mesLocalID 传入的对应本地id
	 * @return 状态值
	 */
	public int getStatus(int mesLocalID) {
		Cursor cursor = null;
		String sql = null;
		try {
			sql = "SELECT " + ChatTable.Status + "FROM " + tablename + " WHERE " + ChatTable.MesLocalID + " = " + mesLocalID;

			BaseLog.d("sql_log", "chatdb getStatus =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				return cursor.getInt(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return 0;
	}

	/**
	 * 升级状态值
	 * @param status 状态值
	 * @param mesSvrID 服务器消息id
	 */
	public void updateStatus(int status, String mesSvrID) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Status + " = " + status + " WHERE " + ChatTable.MesSvrID + " = " + mesSvrID;

		BaseLog.d("sql_log", "chatdb updateStatus =" + sql);
		this.execSql(sql);
	}

	public void updataStatusById(int status, long localId, long time) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Status + " = " + status + " , " + ChatTable.CreateTime + " = " + time + " WHERE "
				+ ChatTable.MesLocalID + " = " + localId;

		BaseLog.d("sql_log", "chatdb updataStatusById =" + sql);
		this.execSql(sql);
	}

	public void updateDesById(int status, long localId) {//修改发送的状态
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Des + " = " + status + " WHERE " + ChatTable.MesLocalID + " = " + localId;

		this.execSql(sql);
	}

	public void updataStatusById(int status, int localId) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Status + " = " + status + " WHERE " + ChatTable.MesLocalID + " = " + localId;

		BaseLog.d("sql_log", "chatdb updataStatusById =" + sql);
		this.execSql(sql);
	}

	/**
	 * 判断时有有相同时间戳的数据
	 * @return 
	 */
	public boolean selectDataByTime(long time) {
		Cursor cursor = null;
		String sql = null;
		try {
			sql = "SELECT * FROM " + tablename + " WHERE " + ChatTable.CreateTime + " = " + time;

			BaseLog.d("sql_log", "chatdb selectDataByTime =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
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

	/**
	 * 升级阅后即焚消息进度条，只在打开阅后即焚消息时保存当前时间
	 * @param mesSerID 对应的服务器消息id
	 * @param time 当前时间
	 */
	public void updateProgress(int mesSerID, int time) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Progress + " = " + time + " WHERE " + ChatTable.MesSvrID + " = " + mesSerID;

		BaseLog.d("sql_log", "chatdb updateProgress =" + sql);
		this.execSql(sql);
	}

	/**
	 * 删除
	 */
	public void deleteTable() {
		final String deleteSql = "DELETE FROM '".concat(tablename).concat("\'");
		try {
			this.execSql(deleteSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//返回同类型的数据的message
	public List<String> getMessageList(int type) {
		Cursor cursor = null;
		String sql = null;
		List<String> list = new ArrayList<String>();
		try {
			sql = "SELECT " + ChatTable.Message + " FROM \'" + tablename + "\' WHERE " + ChatTable.Type + " = " + type;

			BaseLog.d("sql_log", "chatdb getMessageList =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				list.add(cursor.getString(0));
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
	 *
	 * @return
	 */
	public ChatDatabaseEntity getMessageByLocalId(long localid) {
		Cursor cursor = null;
		String sql = null;
		ChatDatabaseEntity chatEntity = null;

		try {
			sql = "SELECT " + ChatTable.CreateTime + "," + ChatTable.Des + "," + ChatTable.FromID + "," + ChatTable.MesLocalID + ", "
					+ ChatTable.Message + ", " + ChatTable.MesSvrID + ", " + ChatTable.Progress + ", " + ChatTable.Status + ", " + ChatTable.Type
					+ " FROM \'" + tablename + "\' WHERE " + ChatTable.MesLocalID + " = " + localid;

			BaseLog.d("sql_log", "chatdb getMessageByLocalId =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				chatEntity = new ChatDatabaseEntity();
				chatEntity.setCreateTime(cursor.getLong(0));
				chatEntity.setDes(cursor.getInt(1));
				chatEntity.setFromID(cursor.getInt(2));
				chatEntity.setMesLocalID(cursor.getInt(3));
				chatEntity.setMessage(cursor.getString(4));
				chatEntity.setMesSvrID(cursor.getString(5));
				chatEntity.setProgress(cursor.getInt(6));
				chatEntity.setStatus(cursor.getInt(7));
				chatEntity.setType(cursor.getInt(8));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return chatEntity;
	}
	
	/**
	 *
	 * @return
	 */
	public List<ChatDatabaseEntity> getMessageByType(int type) {
		List<ChatDatabaseEntity> list = new ArrayList<ChatDatabaseEntity>();
		Cursor cursor = null;
		String sql = null;
		ChatDatabaseEntity chatEntity = null;

		try {
			sql = "SELECT " + ChatTable.CreateTime + "," + ChatTable.Des + "," + ChatTable.FromID + "," + ChatTable.MesLocalID + ", "
					+ ChatTable.Message + ", " + ChatTable.MesSvrID + ", " + ChatTable.Progress + ", " + ChatTable.Status + ", " + ChatTable.Type
					+ " FROM \'" + tablename + "\' WHERE " + ChatTable.Type + " = " + type;

			BaseLog.d("sql_log", "chatdb getMessageByType =" + sql);
			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				chatEntity = new ChatDatabaseEntity();
				chatEntity.setCreateTime(cursor.getLong(0));
				chatEntity.setDes(cursor.getInt(1));
				chatEntity.setFromID(cursor.getInt(2));
				chatEntity.setMesLocalID(cursor.getInt(3));
				chatEntity.setMessage(cursor.getString(4));
				chatEntity.setMesSvrID(cursor.getString(5));
				chatEntity.setProgress(cursor.getInt(6));
				chatEntity.setStatus(cursor.getInt(7));
				chatEntity.setType(cursor.getInt(8));
				list.add(chatEntity);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public ChatDatabaseEntity getMessageByCreateTime(long createtime) {
		Cursor cursor = null;
		String sql = null;
		ChatDatabaseEntity chatEntity = null;
		
		try {
			sql = "SELECT " + ChatTable.CreateTime + "," + ChatTable.Des + "," + ChatTable.FromID + "," + ChatTable.MesLocalID + ", "
					+ ChatTable.Message + ", " + ChatTable.MesSvrID + ", " + ChatTable.Progress + ", " + ChatTable.Status + ", " + ChatTable.Type
					+ " FROM \'" + tablename + "\' WHERE " + ChatTable.CreateTime + " = " + createtime;
			
			BaseLog.d("sql_log", "chatdb getMessageByCreateTime =" + createtime);
			cursor = rawQuery(sql, null);
			
			while (cursor.moveToNext()) {
				chatEntity = new ChatDatabaseEntity();
				chatEntity.setCreateTime(cursor.getLong(0));
				chatEntity.setDes(cursor.getInt(1));
				chatEntity.setFromID(cursor.getInt(2));
				chatEntity.setMesLocalID(cursor.getInt(3));
				chatEntity.setMessage(cursor.getString(4));
				chatEntity.setMesSvrID(cursor.getString(5));
				chatEntity.setProgress(cursor.getInt(6));
				chatEntity.setStatus(cursor.getInt(7));
				chatEntity.setType(cursor.getInt(8));
				
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return chatEntity;
	}
	
	public ChatDatabaseEntity getMessageByIdAndType(int id, int type) {
		Cursor cursor = null;
		String sql = null;
		ChatDatabaseEntity chatEntity = null;
		
		try {
			sql = "SELECT " + ChatTable.CreateTime + "," + ChatTable.Des + "," + ChatTable.FromID + "," + ChatTable.MesLocalID + ", "
					+ ChatTable.Message + ", " + ChatTable.MesSvrID + ", " + ChatTable.Progress + ", " + ChatTable.Status + ", " + ChatTable.Type
					+ " FROM \'" + tablename + "\' WHERE " + ChatTable.Type + " = " + type + " AND " + ChatTable.MesSvrID + " = " + id;
			
			BaseLog.d("sql_log", "chatdb getMessageByIdAndType =" + sql);
			cursor = rawQuery(sql, null);
			
			while (cursor.moveToNext()) {
				chatEntity = new ChatDatabaseEntity();
				chatEntity.setCreateTime(cursor.getLong(0));
				chatEntity.setDes(cursor.getInt(1));
				chatEntity.setFromID(cursor.getInt(2));
				chatEntity.setMesLocalID(cursor.getInt(3));
				chatEntity.setMessage(cursor.getString(4));
				chatEntity.setMesSvrID(cursor.getString(5));
				chatEntity.setProgress(cursor.getInt(6));
				chatEntity.setStatus(cursor.getInt(7));
				chatEntity.setType(cursor.getInt(8));
				
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return chatEntity;
	}
	
	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCountByType(int type, int progress1, int progress2) {
		String sql = "SELECT  COUNT(" + ChatTable.CreateTime + ") FROM \'" + tablename + "\' WHERE "
				+ ChatTable.Type + " = " + type + " AND " + ChatTable.Progress + " = " + progress1 + " OR " + ChatTable.Progress + " = " + progress2;
		BaseLog.d("sql_log", "chatdb getCountByType =" + sql);

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
	
	public void updateProgressByTypeAndId(int type, int id, int progress) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Progress + " = " + progress + " WHERE " + ChatTable.Type + " = " + type + " AND " + ChatTable.MesSvrID + " = " + id;

		BaseLog.d("sql_log", "chatdb updateProgressByTypeAndId =" + sql);
		this.execSql(sql);
	} 

	/**
	 * 升级服务器ID，发送时先保存的服务器id不是正确的，得道回复值后重新赋值
	 * @param mesSvrID 服务器id
	 * @param mesocalID 本地id
	 */
	public void updateProgressByMesSvrID(String mesSvrID, int message) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.Progress + " = " + message + " WHERE " + ChatTable.MesSvrID + " = \'" + mesSvrID
				+ "\'";

		BaseLog.d("sql_log", "chatdb updateMesSvrID =" + sql);
		this.execSql(sql);
	} 

	public void updateStr3ByCreateTime(String MesSvrID, String str) {
		String sql = "UPDATE " + tablename + " SET " + ChatTable.revstr3 + " = " + str + " WHERE " + ChatTable.MesSvrID + " = \'" + MesSvrID
				+ "\'";
		this.execSql(sql);
	}
}
