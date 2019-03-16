package com.tshang.peipei.storage.database.operate;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tshang.peipei.storage.database.PeiPeiDatabaseOperate;
import com.tshang.peipei.storage.database.entity.NewDynamicReplyEntity;
import com.tshang.peipei.storage.database.table.NewDynamicReplyTable;

/**
 * @Title: NewDynamicReplyOperate.java 
 *
 * @Description: 新动态回复操作类 
 *
 * @author Aaron  
 *
 * @date 2015-9-17 下午5:10:00 
 *
 * @version V1.0   
 */
public class NewDynamicReplyOperate extends PeiPeiDatabaseOperate {

	private static NewDynamicReplyOperate operate;

	public NewDynamicReplyOperate(Context mContext) {
		super(mContext);
	}

	public static NewDynamicReplyOperate getInstance(Context context) {
		if (operate == null) {
			synchronized (NewDynamicReplyOperate.class) {
				if (operate == null) {
					operate = new NewDynamicReplyOperate(context);
				}
			}
		}
		return operate;
	}

	public static void closeSession(Context context) {
		operate = null;
	}

	/**
	 * 插入
	 * @author Aaron
	 * @param entity
	 */
	public void insert(NewDynamicReplyEntity entity) {
		String sql = "INSERT INTO " + NewDynamicReplyTable.TABLE_NAME + " (" + NewDynamicReplyTable.Type + "," + NewDynamicReplyTable.Fromuid + ","
				+ NewDynamicReplyTable.Topicuid + "," + NewDynamicReplyTable.Topicid + "," + NewDynamicReplyTable.Commentuid + ","
				+ NewDynamicReplyTable.Auditstatus + "," + NewDynamicReplyTable.Nick + "," + NewDynamicReplyTable.Headpickey + ","
				+ NewDynamicReplyTable.Sex + "," + NewDynamicReplyTable.Createtime + "," + NewDynamicReplyTable.ReplyContent + ","
				+ NewDynamicReplyTable.DynamicContent + "," + NewDynamicReplyTable.ImageKey + "," + NewDynamicReplyTable.Imei + ","
				+ NewDynamicReplyTable.Revint0 + "," + NewDynamicReplyTable.Revint1 + "," + NewDynamicReplyTable.Revint2 + ","
				+ NewDynamicReplyTable.Revint3 + "," + NewDynamicReplyTable.Revstr0 + "," + NewDynamicReplyTable.Revstr1 + ","
				+ NewDynamicReplyTable.Revstr2 + "," + NewDynamicReplyTable.Revstr3 + "," + NewDynamicReplyTable.STUTAS + ","
				+ NewDynamicReplyTable.GLOBALID + "," + NewDynamicReplyTable.COLOR + "," + NewDynamicReplyTable.FONTTYPE
				+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Log.d("Aaron", "sql====" + sql);

		Object[] obj = new Object[] { entity.getType(), entity.getFromuid(), entity.getTopicuid(), entity.getTopicid(), entity.getCommentuid(),
				entity.getAuditstatus(), entity.getNick(), entity.getHeadpickey(), entity.getSex(), entity.getCreatetime(), entity.getReplyContent(),
				entity.getDynamicContent(), entity.getImageKey(), entity.getImei(), entity.getRevint0(), entity.getRevint1(), entity.getRevint2(),
				entity.getRevint3(), entity.getRevstr0(), entity.getRevstr1(), entity.getRevstr2(), entity.getRevstr3(), entity.getStatus(),
				entity.getGlobalid(), entity.getColor(), entity.getFonttype() };

		this.execSql(sql, obj);
	}

	/**
	 * 查询
	 * @author Aaron
	 * @param start
	 * @param num
	 * @return
	 */
	public ArrayList<NewDynamicReplyEntity> seleteDynamicReplyList(int start, int num) {
		ArrayList<NewDynamicReplyEntity> list = new ArrayList<NewDynamicReplyEntity>();
		Cursor cursor = null;

		try {
			String sql = "SELECT " + NewDynamicReplyTable.Type + "," + NewDynamicReplyTable.Fromuid + "," + NewDynamicReplyTable.Topicuid + ","
					+ NewDynamicReplyTable.Topicid + "," + NewDynamicReplyTable.Commentuid + "," + NewDynamicReplyTable.Auditstatus + ","
					+ NewDynamicReplyTable.Nick + "," + NewDynamicReplyTable.Headpickey + "," + NewDynamicReplyTable.Sex + ","
					+ NewDynamicReplyTable.Createtime + "," + NewDynamicReplyTable.ReplyContent + "," + NewDynamicReplyTable.DynamicContent + ","
					+ NewDynamicReplyTable.ImageKey + "," + NewDynamicReplyTable.Imei + "," + NewDynamicReplyTable.Revint0 + ","
					+ NewDynamicReplyTable.Revint1 + "," + NewDynamicReplyTable.Revint2 + "," + NewDynamicReplyTable.Revint3 + ","
					+ NewDynamicReplyTable.Revstr0 + "," + NewDynamicReplyTable.Revstr1 + "," + NewDynamicReplyTable.Revstr2 + ","
					+ NewDynamicReplyTable.Revstr3 + "," + NewDynamicReplyTable.STUTAS + "," + NewDynamicReplyTable.GLOBALID + ","
					+ NewDynamicReplyTable.COLOR + "," + NewDynamicReplyTable.FONTTYPE + " FROM " + NewDynamicReplyTable.TABLE_NAME + " ORDER BY "
					+ NewDynamicReplyTable.Createtime + " DESC  LIMIT " + start + " , " + num;

			//			Log.d("Aaron", "sql==" + sql);

			cursor = rawQuery(sql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					NewDynamicReplyEntity entity = new NewDynamicReplyEntity();
					entity.setType(cursor.getInt(0));
					entity.setFromuid(cursor.getInt(1));
					entity.setTopicuid(cursor.getInt(2));
					entity.setTopicid(cursor.getInt(3));
					entity.setCommentuid(cursor.getInt(4));
					entity.setAuditstatus(cursor.getInt(5));
					entity.setNick(cursor.getString(6));
					entity.setHeadpickey(cursor.getString(7));
					entity.setSex(cursor.getInt(8));
					entity.setCreatetime(cursor.getInt(9));
					entity.setReplyContent(cursor.getString(10));
					entity.setDynamicContent(cursor.getString(11));
					entity.setImageKey(cursor.getString(12));
					entity.setImei(cursor.getString(13));
					entity.setRevint0(cursor.getInt(14));
					entity.setRevint1(cursor.getInt(15));
					entity.setRevint2(cursor.getInt(16));
					entity.setRevint3(cursor.getInt(17));
					entity.setRevstr0(cursor.getString(18));
					entity.setRevstr1(cursor.getString(19));
					entity.setRevstr2(cursor.getString(20));
					entity.setRevstr3(cursor.getString(21));
					entity.setStatus(cursor.getInt(22));
					entity.setGlobalid(cursor.getInt(23));
					entity.setColor(cursor.getString(24));
					entity.setFonttype(cursor.getInt(25));
					list.add(entity);
				}
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
	 * 更新已读 未读状态
	 * @author Aaron
	 *
	 * @param status
	 * @param createTime
	 */
	public void updateReadStatus(int status, long createTime) {
		String sql = "UPDATE " + NewDynamicReplyTable.TABLE_NAME + " SET " + NewDynamicReplyTable.STUTAS + " = " + status + " WHERE "
				+ NewDynamicReplyTable.Createtime + " = " + createTime;
		//		Log.d("Aaron", "sql==" + sql);
		this.execSql(sql);
	}

	/**
	 * 删除
	 * @author Aaron
	 *
	 * @param createTime
	 */
	public void deleteReply(long createTime) {
		String sql = "DELETE FROM " + NewDynamicReplyTable.TABLE_NAME + " WHERE " + NewDynamicReplyTable.Createtime + "=" + createTime;
		this.execSql(sql);
	}

	/**
	 * 删除
	 * @author Aaron
	 *
	 * @param topicuid
	 * @param globalid
	 */
	public void deleteReply(int topicuid, int globalid) {
		String sql = "DELETE FROM " + NewDynamicReplyTable.TABLE_NAME + " WHERE " + NewDynamicReplyTable.Topicuid + "=" + topicuid + " AND "
				+ NewDynamicReplyTable.GLOBALID + " = " + globalid;
		this.execSql(sql);
	}

	/**
	 * 
	 * @author 删除动态
	 *
	 * @param topicuid
	 * @param topicid
	 */
	public void deleteReplyTo(int topicuid, int topicid) {
		String sql = "DELETE FROM " + NewDynamicReplyTable.TABLE_NAME + " WHERE " + NewDynamicReplyTable.Topicuid + "=" + topicuid + " AND "
				+ NewDynamicReplyTable.Topicid + " = " + topicid;
		this.execSql(sql);
	}
}
