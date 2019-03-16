package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;

/**
 * @Title: BizCallBackGetSkillList.java 
 *
 * @Description: 获取技能列表回调
 *
 * @author allen  
 *
 * @date 2014-7-14 下午8:31:03 
 *
 * @version V1.0   
 */
public interface BizCallBackGetSkillList {
	public void getHallSkillListCallBack(int retcode, int isEnd, RetGGSkillInfoList list);
}
