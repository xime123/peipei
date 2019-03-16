package com.tshang.peipei.storage.database.operate;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.ReceiptEntity;
import com.tshang.peipei.storage.database.table.ReceiptTable;

/**
 * @Title: ReceiptOperate.java 
 *
 * @Description: 回执数据库操作类
 *
 * @author allen  
 *
 * @date 2014-6-16 下午3:11:06 
 *
 * @version V1.0   
 */
public class ReceiptOperate extends PeiPeiDatabaseOperate {

	private static ReceiptOperate mReceiptOperate;

	public ReceiptOperate(Context mContext) {
		super(mContext);
	}

	public static void closeReceipt(Context context) {
		mReceiptOperate = null;
	}

	public static ReceiptOperate getInstance(Context context, String db_name) {
		if (mReceiptOperate == null) {
			mReceiptOperate = new ReceiptOperate(context);
		} else {
			if (!String.valueOf(BAApplication.mLocalUserInfo.uid.intValue()).equals(db_name)) {
				mReceiptOperate = new ReceiptOperate(context);
			}
		}
		return mReceiptOperate;
	}

	/**
	 * 插入
	 *
	 * @param friendUid
	 * @param burnId
	 */
	public void insertReceipt(int friendUid, String burnId, String fNick, String nick, int fSex, int sex) {
		ReceiptTable receiptTable = new ReceiptTable();
		String sql = "INSERT INTO " + receiptTable.TABLE_NAME + " ( " + receiptTable.getTableVer() + " , " + receiptTable.getFromID() + " , "
				+ receiptTable.getMesSvrID() + " , " + receiptTable.getFNick() + " , " + receiptTable.getNick() + " , " + receiptTable.getFSex()
				+ " , " + receiptTable.getSex() + " )" + " VALUES (?,?,?,?,?,?,?)";
		//		BaseLog.d("sql_log", "ReceiptOperate insert =" + sql);

		Object[] objs = new Object[] { BAConstants.PEIPEI_DB_VERSION, friendUid, burnId, fNick, nick, fSex, sex };
		this.execSql(sql, objs);
	}

	/**
	 * 删除
	 *
	 */
	public void deleteBurnId(int friendUid, String burnId) {
		ReceiptTable receiptTable = new ReceiptTable();
		String sql = "DELETE  FROM " + receiptTable.TABLE_NAME + " WHERE " + receiptTable.getFromID() + " = ? AND " + receiptTable.getMesSvrID()
				+ " = ?";

		BaseLog.d("sql_log", "ReceiptOperate deleteTopicId " + sql);
		this.execSql(sql, new Object[] { friendUid, burnId });
	}

	/**
	 * 获取数据
	 */
	public List<ReceiptEntity> selectReceiptList(List<ReceiptEntity> list) {
		if (list == null) {
			return null;
		}
		Cursor cursor = null;
		ReceiptTable receiptTable = new ReceiptTable();
		String sql = null;
		ReceiptEntity chatEntity = null;

		try {
			sql = "SELECT " + receiptTable.getFromID() + "," + receiptTable.getMesSvrID() + "," + receiptTable.getFNick() + ","
					+ receiptTable.getNick() + "," + receiptTable.getFSex() + "," + receiptTable.getSex() + " FROM " + receiptTable.TABLE_NAME;

			cursor = rawQuery(sql, null);

			while (cursor.moveToNext()) {
				chatEntity = new ReceiptEntity();
				chatEntity.setFromID(cursor.getInt(0));
				chatEntity.setMesSvrID(cursor.getString(1));
				chatEntity.setFNick(cursor.getString(2));
				chatEntity.setNick(cursor.getString(3));
				chatEntity.setFSex(cursor.getInt(4));
				chatEntity.setSex(cursor.getInt(5));

				list.add(chatEntity);
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
}
