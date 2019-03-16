package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackGetReward.java 
 *
 * @Description: 领取奖励回调 
 *
 * @author allen 
 *
 * @date 2014-5-6 上午10:57:06 
 *
 * @version V1.0   
 */
public interface BizCallBackGetReward {
	public void getRewardBack(int retCode, int type);
}
