package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackDeletFollow.java 
 *
 * @Description:　取消关注
 *
 * @author vactor
 *
 * @date 2014-4-30 下午2:41:19 
 *
 * @version V1.0   
 */
public interface BizCallBackDeletFollow {

	public void deleteFollowCallBack(int retCode, int followId);
}
