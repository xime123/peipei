package com.tshang.peipei.storage.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.table.AppreciateTable;
import com.tshang.peipei.storage.database.table.ChatTable;
import com.tshang.peipei.storage.database.table.DynamicTable;
import com.tshang.peipei.storage.database.table.FriendTable;
import com.tshang.peipei.storage.database.table.NewDynamicReplyTable;
import com.tshang.peipei.storage.database.table.PeiPeiSessionTable;
import com.tshang.peipei.storage.database.table.Phototable;
import com.tshang.peipei.storage.database.table.Publishtable;
import com.tshang.peipei.storage.database.table.ReceiptTable;
import com.tshang.peipei.storage.database.table.RelationshipTable;
import com.tshang.peipei.storage.database.table.SessionTable;
import com.tshang.peipei.storage.database.table.ShowsTable;

/**
 * 数据对象类（初始化表，库）
 * 
 * 
 */
public class PeiPeiDatabase {

	private static PeiPeiDatabaseHelper dbHelper = null;

	private static PeiPeiDatabase mBADatabase = null;

	public static PeiPeiDatabase getBADatabase(Context context, String uid) {
		if (mBADatabase == null) {
			mBADatabase = new PeiPeiDatabase(context, uid);
		} else {
			if (!dbHelper.getDb_name().equals(uid)) {
				closeEuDb();
				mBADatabase = new PeiPeiDatabase(context, uid);
			}
		}

		return mBADatabase;
	}

	/**
	 * 构造方法
	 */
	private PeiPeiDatabase(Context context, String db_name) {
		if (dbHelper == null) {
			getDBHelperByName(context, db_name);
		}
		dbHelper.setBAdb(this);
	}

	private void getDBHelperByName(Context context, String db_name) {
		dbHelper = new PeiPeiDatabaseHelper(context, db_name, BAConstants.PEIPEI_DB_VERSION);
	}

	public PeiPeiDatabaseHelper getDbHelper() {
		return dbHelper;
	}

	/**
	 * @param isWritable
	 *            true-写 false-读
	 * @return 根据参数返回db对象
	 */
	public SQLiteDatabase getEuDb(boolean isWritable) {
		SQLiteDatabase sqliteDatabase = null;

		try {
			if (dbHelper != null) {
				if (isWritable) {
					sqliteDatabase = dbHelper.getWritableDatabase();
				} else {
					sqliteDatabase = dbHelper.getReadableDatabase();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqliteDatabase;
	}

	/**
	 * 关闭数据库
	 */
	public static void closeEuDb() {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		mBADatabase = null;
	}

	public void createAllTable(SQLiteDatabase db) {
		if (db != null && db.isOpen()) {
			db.execSQL(new FriendTable().getCreateSQL());
			db.execSQL(new SessionTable().getCreateSQL());
			db.execSQL(new Phototable().getCreateSQL());
			db.execSQL(new Publishtable().getCreateSQL());
			db.execSQL(new AppreciateTable().getCreateSQL());
			db.execSQL(new ReceiptTable().getCreateSQL());
			db.execSQL(new PeiPeiSessionTable().getCreateSQL());
			db.execSQL(new DynamicTable().getCreateSQL());
			db.execSQL(new RelationshipTable().getCreateSQL());
			db.execSQL(new ShowsTable().getCreateSQL());
			db.execSQL(new NewDynamicReplyTable().getCreateSQL());
		}
	}

	public void deleteAllTable(SQLiteDatabase db) {
		if (db != null && db.isOpen()) {
			db.execSQL(new FriendTable().getDropSQL());
			db.execSQL(new SessionTable().getDropSQL());
			db.execSQL(new Phototable().getDropSQL());
			db.execSQL(new Publishtable().getDropSQL());
			db.execSQL(new AppreciateTable().getDropSQL());
			db.execSQL(new ReceiptTable().getDropSQL());
			db.execSQL(new PeiPeiSessionTable().getDropSQL());
			db.execSQL(new DynamicTable().getDropSQL());
			db.execSQL(new RelationshipTable().getDropSQL());
			db.execSQL(new ShowsTable().getDropSQL());
			db.execSQL(new NewDynamicReplyTable().getDropSQL());
		}
	}

	public void onUpgradeSigle(SQLiteDatabase db, String db_name, final int flag) {
		if (db != null && db.isOpen()) {
			if (flag == 2) {
				updateSessionToPeipei(db, db_name);
			} else if (flag == 3) {
				upgradeTables(db, PeiPeiSessionTable.TABLE_NAME, new PeiPeiSessionTable().getColumns());
				setChatTableUpdate(db);
			} else if (flag == 4) {
				upgradeTablesSessionTyep(db, PeiPeiSessionTable.TABLE_NAME);
			} else if (flag == 5 || flag == 6) {
				createAllTable(db);
			} else if (flag == 7) {
				createAllTable(db);
			}
		}
	}

	/**
	 * 会话列表升级，低版本保存序列化对象错误
	 * @author allen
	 *
	 */
	private void updateSessionToPeipei(SQLiteDatabase db, String db_name) {
		Cursor cursor = null;
		SessionDatabaseEntity sessionEntity = null;
		List<SessionDatabaseEntity> list = new ArrayList<SessionDatabaseEntity>();

		SessionTable sessionTable = new SessionTable();
		String sql = "SELECT " + sessionTable.getUserID() + "," + sessionTable.getUnreadCount() + "," + sessionTable.getLatestUpdateTime() + " FROM "
				+ SessionTable.TABLE_NAME + " WHERE " + sessionTable.getUserID() + " != " + BAConstants.PEIPEI_BROADCASET + " ORDER BY "
				+ sessionTable.getLatestUpdateTime() + " DESC";
		cursor = db.rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {//取出原数据库数据
				sessionEntity = new SessionDatabaseEntity();
				sessionEntity.setUserID(cursor.getInt(0));
				sessionEntity.setUnreadCount(cursor.getInt(1));
				sessionEntity.setLatestUpdateTime(cursor.getLong(2));

				list.add(sessionEntity);
			}
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}

		if (list.size() > 0) {
			db.execSQL(new PeiPeiSessionTable().getCreateSQL());
			for (int i = 0; i < list.size(); i++) {
				sessionEntity = list.get(i);
				String tablename = "chat_" + sessionEntity.getUserID() + "P";
				sql = "SELECT " + ChatTable.Message + " FROM \'" + tablename + "\' ORDER BY " + ChatTable.MesLocalID + " DESC";

				cursor = db.rawQuery(sql, null);

				while (cursor.moveToNext()) {//到会话中回去最后一条数据
					sessionEntity.setSessionData(cursor.getString(0));
					break;
				}

				//插入新的数据库中
				sql = "INSERT INTO " + PeiPeiSessionTable.TABLE_NAME + " (" + PeiPeiSessionTable.TableVer + "," + PeiPeiSessionTable.UserID + ","
						+ PeiPeiSessionTable.UnreadCount + "," + PeiPeiSessionTable.SessionData + "," + PeiPeiSessionTable.LatestUpdateTime + ","
						+ PeiPeiSessionTable.Sex + "," + PeiPeiSessionTable.Nick + ") VALUES (?,?,?,?,?,?,?)";

				BaseLog.d("sql_log", "sessiondb insert =" + sql);
				Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, sessionEntity.getUserID(), sessionEntity.getUnreadCount(),
						sessionEntity.getSessionData(), sessionEntity.getLatestUpdateTime(), sessionEntity.getSex(), sessionEntity.getNick() };

				db.execSQL(sql, objs);
			}

			//网络获取昵称和性别
			new UpdateSqlThreadBySession(list).start();
		}

		db.execSQL(new SessionTable().getDropSQL());//删除原数据

	}

	/**
	 * 用户特定表升级
	 * @param db
	 * @param tableName
	 * @param columns
	 * @param db_name
	 */
	protected void upgradeTable(SQLiteDatabase db, String tableName, String columns) {
		try {
			db.beginTransaction();

			String tempTableName = tableName + "_temp";
			String sql = "ALTER TABLE \'" + tableName + "\' RENAME TO \'" + tempTableName + "\'";
			db.execSQL(sql);

			String sqlCreate = ChatTable.getCreateSQL(tableName);
			db.execSQL(sqlCreate);

			sql = "INSERT INTO \'" + tableName + "\' (" + columns + ") " + " SELECT " + columns + " FROM \'" + tempTableName + "\'";

			db.execSQL(sql);

			db.execSQL("DROP TABLE IF EXISTS \'" + tempTableName + "\'");

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * 用于所有表升级
	 * @param db
	 * @param tableName
	 * @param columns
	 * @param db_name
	 */
	protected void upgradeTables(SQLiteDatabase db, String tableName, String columns) {
		try {
			db.beginTransaction();

			String tempTableName = tableName + "_temp";
			String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
			db.execSQL(sql);

			createAllTable(db);

			sql = "INSERT INTO " + tableName + " (" + columns + ") " + " SELECT " + columns + " FROM " + tempTableName;

			db.execSQL(sql);

			db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	private void setChatTableUpdate(SQLiteDatabase db) {
		String sql = "select name from sqlite_master where type='table' order by name";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			String curr_table_name = null;

			String str;
			while (cursor.moveToNext()) {
				curr_table_name = cursor.getString(0);
				if (curr_table_name.startsWith("chat_")) {
					str = ChatTable.comluns();
					upgradeTable(db, curr_table_name, str);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			cursor = null;

		}
	}

	/**
	 * 用于会话表升级
	 * @param db
	 * @param tableName
	 * @param columns
	 * @param db_name
	 */
	protected void upgradeTablesSessionTyep(SQLiteDatabase db, String tableName) {
		try {
			String sql = "UPDATE " + tableName + " SET Type = 0 WHERE Type is null";
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
}
