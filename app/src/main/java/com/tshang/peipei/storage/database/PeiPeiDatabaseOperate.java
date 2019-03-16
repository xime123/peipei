package com.tshang.peipei.storage.database;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tshang.peipei.activity.BAApplication;

/**
 * db操作类
 * 
 * @copyright 移动梦想科技
 * 
 */
public class PeiPeiDatabaseOperate {

	protected PeiPeiDatabase database;

	protected SQLiteDatabase db;

	public PeiPeiDatabaseOperate(Context mContext) {
		if (BAApplication.mLocalUserInfo != null) {
			database = PeiPeiDatabase.getBADatabase(new WeakReference<Context>(mContext).get(),
					String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()));
			db = database.getEuDb(true);
		}
	}

	/**
	 * 检查表是否已经ok
	 * 
	 * @return 返回当然db是否可用
	 */
	private synchronized boolean checkDbIsOk() {
		if (db != null && db.isOpen()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 执行不带参数sql语句
	 * 
	 * @param sqlStr
	 *            sql语句
	 */
	public void execSql(String sqlStr) {
		try {
			if (!checkDbIsOk()) {
				db = database.getEuDb(true);
			}

			db.execSQL(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行带参数sql语句
	 * 
	 * @param sqlStr
	 *            sql语句
	 * @param args
	 *            参数
	 */
	public void execSql(String sqlStr, Object[] args) {
		try {
			if (!checkDbIsOk()) {
				db = database.getEuDb(true);
			}

			db.execSQL(sqlStr, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行查询sql语句
	 * 
	 * @param sqlStr
	 *            sql语句
	 * @param selectionArgs
	 *            查询条件
	 * @return 返回相应的cursor
	 */
	public Cursor rawQuery(String sqlStr, String[] selectionArgs) {
		Cursor cursor = null;
		try {
			if (!checkDbIsOk()) {
				db = database.getEuDb(true);
			}

			if (db != null)
				cursor = db.rawQuery(sqlStr, selectionArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursor;
	}

	/**
	 * 清空相应的表
	 * 
	 * @param tableName
	 *            要请空的表
	 */
	public void deleteTable(String tableName) {
		if (db == null)
			db = database.getEuDb(true);
		db.delete(tableName, null, null);
	}

	public Cursor query(String Table_Name, String[] columns, String whereStr, String[] whereArgs, String orderBy, String limit) {
		if (db == null) {
			database.getEuDb(true);
		}
		return db.query(Table_Name, columns, whereStr, whereArgs, null, null, orderBy, limit);
	}
}
