package com.tshang.peipei.storage.db;

import java.util.HashMap;
import java.util.Map;

public class BroadCastColumn extends DatabaseColumn {

	public static final String TABLE_NAME = "BroadCastRecord";
	public static final String NAME = "name";
	public static final String CONTNET = "contnet";//发送的内容
	public static final String TYPE = "type";//置顶或普通广播
	public static final String STAUTS = "stauts";//是我发送的还是@我的 1我发送的，2别人发送的
	public static final String CREATETIME = "createtime";//发送时间
	public static final String TOUSER = "touser";//@的用户
	public static final String SENDUSER = "senduser";//发送方
	public static final String USERUID = "useruid";//用户的uid
	public static final String REVINT = "revint";//保留字段  语音广播
	public static final String REVSTR1 = "revstr1";
	public static final String REVSTR2 = "revstr2";
	public static final String REVSTR3 = "revstr3";
	public static final String REVSTR4 = "revstr4";
	public static final String REVINT0 = "revint0";
	public static final String REVINT1 = "revint1";
	public static final String REVINT2 = "revint2";
	public static final String REVINT3 = "revint3";
	public static final String REVINT4 = "revint4";

	private static final Map<String, String> mColumnMap = new HashMap<String, String>();
	static {
		mColumnMap.put(_ID, "integer primary key autoincrement");
		mColumnMap.put(NAME, "text");
		mColumnMap.put(CONTNET, "text");
		mColumnMap.put(TYPE, "integer");
		mColumnMap.put(STAUTS, "text");
		mColumnMap.put(CREATETIME, "text");
		mColumnMap.put(TOUSER, "text");
		mColumnMap.put(SENDUSER, "text");
		mColumnMap.put(USERUID, "integer");
		mColumnMap.put(REVINT, "text");
		mColumnMap.put(REVSTR1, "text");
		mColumnMap.put(REVSTR2, "text");
		mColumnMap.put(REVSTR3, "text");
		mColumnMap.put(REVSTR4, "text");
		mColumnMap.put(REVINT0, "integer DEFAULT 2105376");
		mColumnMap.put(REVINT1, "integer");
		mColumnMap.put(REVINT2, "integer");
		mColumnMap.put(REVINT3, "integer");
		mColumnMap.put(REVINT4, "integer");
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> getTableMap() {
		return mColumnMap;
	}

}
