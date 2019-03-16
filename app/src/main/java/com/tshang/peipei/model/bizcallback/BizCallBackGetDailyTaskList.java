package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfoList;

/**
 * @Title: BizCallBackGetFreshTaskList.java 
 *
 * @Description: 每日任务回调 
 *
 * @author allen  
 *
 * @date 2014-7-18 下午1:40:27 
 *
 * @version V1.0   
 */
public interface BizCallBackGetDailyTaskList {
	public void getDailyTaskListCallBack(int retCode, GGTaskInfoList list, int loginreward);
}
