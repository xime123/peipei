package com.tshang.peipei.storage.database.operate;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.ShowsEntity;
import com.tshang.peipei.storage.database.table.ShowsTable;

/**
 * @Title: ShowsOperate.java 
 *
 * @Description: 秀场数据库操作类 
 *
 * @author allen  
 *
 * @date 2015-1-21 上午10:30:55 
 *
 * @version V1.0   
 */
public class ShowsOperate extends PeiPeiDatabaseOperate {
	private static ShowsOperate sessionDB;

	private ShowsOperate(Context mContext) {
		super(mContext);
	}

	public static ShowsOperate getInstance(Context context) {
		if (sessionDB == null) {
			synchronized (ShowsOperate.class) {
				if (sessionDB == null) {
					sessionDB = new ShowsOperate(context);
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
	public void insert(String data, int type, int status) {
		if (getCount() >= 300) {
			deletFrist();
		}
		String sql = "INSERT INTO " + ShowsTable.TABLE_NAME + " (" + ShowsTable.TableVer + "," + ShowsTable.data + "," + ShowsTable.type + ","
				+ ShowsTable.status + ") VALUES (?,?,?,?)";

		BaseLog.d("sql_log", "RelationOperate insert =" + sql);
		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, data, type, status };

		this.execSql(sql, objs);
	}

	public void deletFrist() {
		String sql = "SELECT data FROM showstable limit 0,1";

		String data = "";
		Cursor cursor = null;

		try {
			cursor = this.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				data = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}

		sql = "DELETE  FROM " + ShowsTable.TABLE_NAME + " WHERE data =" + data;
		this.execSql(sql);
	}

	/**
	 * 获取总条数
	 * @return 
	 */
	public int getCount() {
		String sql = "SELECT  COUNT(" + ShowsTable.data + ") FROM " + ShowsTable.TABLE_NAME;

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
	 * 判断是否存在该会话
	 * @param userId 对应的好友id
	 * @return true：存在；false：不存在
	 */
	public boolean isHaveSession(String data, int type) {
		boolean hasSession = false;
		Cursor cursor = null;
		try {
			String[] columns = { ShowsTable.data };
			String[] whereArgs = { data, String.valueOf(type) };
			cursor = query(ShowsTable.TABLE_NAME, columns, ShowsTable.data + "=?" + " AND " + ShowsTable.type + " =?", whereArgs, null, null);

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

	public int selectStatus(String key, int type) {
		int status = 0;
		Cursor cursor = null;
		String sql = null;
		try {
			sql = "SELECT " + ShowsTable.status + " FROM " + ShowsTable.TABLE_NAME + " WHERE " + ShowsTable.data + " = " + key + " AND "
					+ ShowsTable.type + " = " + type;

			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				status = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return status;
	}

	/**
	 * 获取数据
	 */
	public List<ShowsEntity> selectReceiptList(List<ShowsEntity> list) {
		if (list == null) {
			return null;
		}
		Cursor cursor = null;
		String sql = null;
		ShowsEntity showsEntity = null;

		try {
			sql = "SELECT " + ShowsTable.data + "," + ShowsTable.status + "," + ShowsTable.type + " FROM " + ShowsTable.TABLE_NAME;

			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				showsEntity = new ShowsEntity();
				showsEntity.setData(cursor.getString(0));
				showsEntity.setStatus(cursor.getInt(1));
				showsEntity.setType(cursor.getInt(2));

				list.add(showsEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return list;
	}

	public void updateStutas(String datakey, int status) {
		String sql = "UPDATE " + ShowsTable.TABLE_NAME + " SET " + ShowsTable.status + " = " + status + " WHERE " + ShowsTable.data + " = \'"
				+ datakey + "\'";

		BaseLog.d("sql_log", "ShowsOperate updateStutas =" + sql);
		this.execSql(sql);
	}

}
