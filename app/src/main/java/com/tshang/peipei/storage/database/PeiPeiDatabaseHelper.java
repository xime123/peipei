package com.tshang.peipei.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.tshang.peipei.base.babase.BAConstants;

/**
 * 数据库辅助管理类
 * 
 * 
 */
public final class PeiPeiDatabaseHelper extends SQLiteOpenHelper {

	private PeiPeiDatabase peipeiDatabase;

	private String db_name;

	private PeiPeiDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public PeiPeiDatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
		this.db_name = name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (peipeiDatabase != null) {
			peipeiDatabase.createAllTable(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion <= oldVersion || peipeiDatabase == null) {
			return;
		} else {
			int upgradeVersion = oldVersion;

			// **************低版本抛弃原有数据*******
			// if (upgradeVersion < BAConstants.PEIPEI_DB_VERSION_MIN) {
			// icd.deleteAllTable(db, this.db_name);
			// icd.createAllTable(db, this.db_name);
			// return;
			// }

			while (upgradeVersion < BAConstants.PEIPEI_DB_VERSION) {
				peipeiDatabase.onUpgradeSigle(db, db_name, upgradeVersion);
				upgradeVersion++;
			}

			if (upgradeVersion != newVersion) {
				// Drop tables
				peipeiDatabase.deleteAllTable(db);
				// Create tables
				peipeiDatabase.createAllTable(db);
			}
		}

	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public PeiPeiDatabase getBAdb() {
		return peipeiDatabase;
	}

	public void setBAdb(PeiPeiDatabase peipeiDatabase) {
		this.peipeiDatabase = peipeiDatabase;
	}

	public interface IDbCreateDeleteTable {

		/**
		 * 初始化创建表
		 * 
		 * @param db
		 */
		void createAllTable(SQLiteDatabase db, String db_name);

		/**
		 * 删除表
		 * 
		 * @param db
		 */
		void deleteAllTable(SQLiteDatabase db, String db_name);

		/**
		 * 根据表名升级单张表
		 * 
		 * @param db
		 *            操作对象
		 * @param table_name
		 *            表名
		 */
		void onUpgradeSigle(SQLiteDatabase db, String db_name, int flag);
	}

}
