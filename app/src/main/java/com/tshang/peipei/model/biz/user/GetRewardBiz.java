package com.tshang.peipei.model.biz.user;

import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfoList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetDailyTaskList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetFreshTaskList;
import com.tshang.peipei.model.bizcallback.BizCallBackGetReward;
import com.tshang.peipei.model.request.RequestGetDailyTaskList;
import com.tshang.peipei.model.request.RequestGetDailyTaskList.IGetDailyTaskList;
import com.tshang.peipei.model.request.RequestGetFreshTaskList;
import com.tshang.peipei.model.request.RequestGetFreshTaskList.IGetFreshTaskList;
import com.tshang.peipei.model.request.RequestGetReward;
import com.tshang.peipei.model.request.RequestGetReward.IGetReward;

/**
 * @Title: GetRewardBiz.java 
 *
 * @Description: 获取奖励操作类 
 *
 * @author allen  
 *
 * @date 2014-5-6 下午1:47:39 
 *
 * @version V1.0   
 */
public class GetRewardBiz implements IGetReward, IGetFreshTaskList, IGetDailyTaskList {

	private BizCallBackGetReward getReward;
	private BizCallBackGetFreshTaskList getFresh;
	private BizCallBackGetDailyTaskList getDaily;

	public void getRewardToSer(byte[] auth, int ver, int uid, int type, BizCallBackGetReward callback) {
		RequestGetReward req = new RequestGetReward();
		getReward = callback;
		req.geteward(auth, ver, uid, type, this);
	}

	@Override
	public void getRewardCallBack(int retCode, int type) {
		if (getReward != null) {
			getReward.getRewardBack(retCode, type);
		}

	}

	public void getFreshTaskList(byte[] auth, int ver, int uid, int start, int num, BizCallBackGetFreshTaskList callback) {
		RequestGetFreshTaskList req = new RequestGetFreshTaskList();
		getFresh = callback;
		req.getFreshTaskList(auth, ver, uid, start, num, this);
	}

	@Override
	public void appreciateCallBack(int retCode, GGTaskInfoList list) {
		if (getFresh != null) {
			getFresh.getFreshTaskListCallBack(retCode, list);
		}
	}

	public void getDailyTaskList(byte[] auth, int ver, int uid, int start, int num, BizCallBackGetDailyTaskList callback) {
		RequestGetDailyTaskList req = new RequestGetDailyTaskList();
		getDaily = callback;
		req.getDailyTaskList(auth, ver, uid, start, num, this);
	}

	@Override
	public void dailyCallBack(int retCode, GGTaskInfoList list,int loginreward) {
		if (getDaily != null) {
			getDaily.getDailyTaskListCallBack(retCode, list,loginreward);
		}
	}

}
