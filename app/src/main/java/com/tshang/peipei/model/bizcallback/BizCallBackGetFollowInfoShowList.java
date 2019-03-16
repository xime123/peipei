package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RetFollowShowInfoList;

/**
 * @Title: BizCallBackGetFollowInfoShowList.java 
 *
 * @Description: 关注女神的动态
 *
 * @author vactor
 *
 * @date 2014-5-14 下午2:25:14 
 *
 * @version V1.0   
 */
public interface BizCallBackGetFollowInfoShowList {

	public void getFollowListCallBack(int retCode, int isEnd, RetFollowShowInfoList list);

}
