package com.tshang.peipei.model.hall;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.bizcallback.BizCallBackGetOnlineUserList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetSkillList;
import com.tshang.peipei.model.request.RequestGetNearUserList.IGetNearUserList;
import com.tshang.peipei.model.request.RequestGetOnlineFreshUserList.iGetOnlineFresh;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.UserInfoAndSkillInfoList;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: HallBase.java 
 *
 * @Description: 大厅逻辑基类
 *
 * @author Jeff  
 *
 * @date 2014年10月10日 上午10:41:16 
 *
 * @version V1.3.0   
 */
public class HallBase implements BizCallBackGetSkillList, BizCallBackGetOnlineUserList, iGetOnlineFresh, IGetNearUserList {

	public static final int REFRESH_CODE = 1;//下拉刷新
	public static final int LOADMORE_CODE = 2;//上拉加载更多

	protected Activity activity;//上下文对象
	protected BAHandler mHandler;
	protected static final int LOADCOUNT = 24;

	public HallBase(Activity activity) {
		this.activity = activity;
	}

	protected void getHallUserList(int sex, int start) {//拉去最新在线
		int la = SharedPreferencesTools.getInstance(activity).getIntValueByKeyToLation(BAConstants.PEIPEI_GPS_LA);
		int lo = SharedPreferencesTools.getInstance(activity).getIntValueByKeyToLation(BAConstants.PEIPEI_GPS_LO);
		MainHallBiz.getInstance().getUserOnLineList(activity, sex, start, LOADCOUNT, 0, la, lo, this);
	}

	protected void getHallNearUserList(int sex, int start, int dis) {//拉去附近
		int la = SharedPreferencesTools.getInstance(activity).getIntValueByKeyToLation(BAConstants.PEIPEI_GPS_LA);
		int lo = SharedPreferencesTools.getInstance(activity).getIntValueByKeyToLation(BAConstants.PEIPEI_GPS_LO);
		MainHallBiz.getInstance().getUserNearList(activity, sex, start, LOADCOUNT, dis, la, lo, this);
	}

	//拉取新人
	protected void getFreshOnline(int sex, int start) {
		MainHallBiz.getInstance().getFreshOnLineList(activity, sex, start, LOADCOUNT, this);
	}

	protected void getSkillList(int start, int type) {//拉去技能
		MainHallBiz.getInstance().getSkillList(activity, start, LOADCOUNT, type, this);
	};

	@Override
	public void getOnlineUserListCallBack(int retCode, int isEnd, UserAndSkillInfoList list) {//拉去最新回调回来

	}

	@Override
	public void getHallSkillListCallBack(int retcode, int isEnd, RetGGSkillInfoList list) {//拉取技能回调回来

	}

	@Override
	public void getFreshList(int retCode, int isend, UserAndSkillInfoList userlist) {//新人榜回调回来

	}

	public void getHallListData(boolean isUseCacheData, boolean isRefresh) {//获取大厅的数据

	}

	@Override
	public void getNearUserListCallBack(int retCode, int isEnd, int nextstart, int nextdis, UserAndSkillInfoList list) {

	}
}
