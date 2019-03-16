package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfoList;

/**
 * @Title: BizCallBackGetFreshTaskList.java 
 *
 * @Description: 新手任务回调 
 *
 * @author allen  
 *
 * @date 2014-7-18 下午1:40:27 
 *
 * @version V1.0   
 */
public interface BizCallBackGetFreshTaskList {
	public void getFreshTaskListCallBack(int retCode, GGTaskInfoList list);
}
