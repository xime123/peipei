package com.tshang.peipei.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "peipei_broadcast.db";
	private static final int DB_VERSION = 3;

	private SQLiteDatabase db;

	private static DBHelper mdbHelper;

	public static DBHelper getInstance(Context context) {
		if (mdbHelper == null) {
			mdbHelper = new DBHelper(context);
		}
		return mdbHelper;
	}

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	private DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		operateTable(db, "");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == newVersion) {
			return;
		}
		operateTable(db, "DROP TABLE IF EXISTS ");
	}

	protected String getColumnNames(SQLiteDatabase db, String tableName) {
		StringBuffer sb = new StringBuffer();
		Cursor c = null;

		try {
			c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
			if (null != c) {
				int columnIndex = c.getColumnIndex("name");
				if (-1 == columnIndex) {
					return null;
				}

				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					sb.append(c.getString(columnIndex)).append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
				c = null;
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public void operateTable(SQLiteDatabase db, String actionString) {
		Class<DatabaseColumn>[] columnsClasses = DatabaseColumn.getSubClasses();
		DatabaseColumn columns = null;
		for (int i = 0; i < columnsClasses.length; i++) {
			try {
				columns = columnsClasses[i].newInstance();
				if ("".equals(actionString) || actionString == null) {
					db.execSQL(columns.getTableCreateor());

				} else {

					String tableName = columns.getTableName();
					String columnsName = getColumnNames(db, tableName);
					try {
						db.beginTransaction();

						// 1, Rename table.  
						String tempTableName = tableName + "_temp";
						String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
						db.execSQL(sql);

						// 2, Create table.  
						db.execSQL(columns.getTableCreateor());

						// 3, Load data  
						sql = "INSERT INTO " + tableName + " (" + columnsName + ") " + " SELECT " + columnsName + " FROM " + tempTableName;
						db.execSQL(sql);

						// 4, Drop the temporary table.  
						db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
						db.setTransactionSuccessful();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
					//					db.execSQL(actionString + columns.getTableName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public long insert(String Table_Name, ContentValues values) {
		if (db == null)
			db = getWritableDatabase();
		return db.insert(Table_Name, null, values);
	}

	/**
	 * 
	 * @param Table_Name
	 * @param id
	 * @return 影响行数
	 */
	public int delete(String Table_Name, int id) {
		if (db == null)
			db = getWritableDatabase();
		return db.delete(Table_Name, BaseColumns._ID + "=?", new String[] { String.valueOf(id) });
	}

	/**
	 * 删除表中所有数据，保存表结构
	 * 
	 * @param Table_Name
	 * @return
	 */
	public int deleteAllData(String Table_Name) {
		if (db == null)
			db = getWritableDatabase();
		return db.delete(Table_Name, null, null);
	}

	/**
	 * @param Table_Name
	 * @param values
	 * @param WhereClause
	 * @param whereArgs
	 * @return 影响行数
	 */
	public int update(String Table_Name, ContentValues values, String WhereClause, String[] whereArgs) {
		if (db == null) {
			db = getWritableDatabase();
		}
		return db.update(Table_Name, values, WhereClause, whereArgs);
	}

	public Cursor query(String Table_Name, String[] columns, String whereStr, String[] whereArgs) {
		if (db == null) {
			db = getReadableDatabase();
		}

		return db.query(Table_Name, columns, whereStr, whereArgs, null, null, null);
	}

	public Cursor query(String Table_Name, String[] columns, String whereStr, String[] whereArgs, String orderBy, String limit) {
		if (db == null) {
			db = getReadableDatabase();
		}
		return db.query(Table_Name, columns, whereStr, whereArgs, null, null, orderBy, limit);
	}

	public long getCount(String Table_Name) {
		Cursor cursor = rawQuery("select count(*) from " + Table_Name, null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		return count;
	}

	public Cursor rawQuery(String sql, String[] args) {
		if (db == null) {
			db = getReadableDatabase();
		}
		return db.rawQuery(sql, args);
	}

	public void ExecSQL(String sql) {
		if (db == null) {
			db = getWritableDatabase();
		}
		db.execSQL(sql);
	}

	public boolean dataExists(String Table_Name, String[] columns, String whereStr, String[] selectionArgs) {
		boolean value = false;
		Cursor query = query(Table_Name, columns, whereStr + " = ?", selectionArgs);

		if (query != null && query.getCount() > 0) {
			value = true;
		}

		query.close();
		return value;
	}

	public void closeDb() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

}
