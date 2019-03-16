package com.tshang.peipei.model.bizcallback;

import com.tshang.peipei.protocol.asn.gogirl.RelationshipInfo;

/**
 * @Title: BizCallBackGetRelationship.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author aaa  
 *
 * @date 2014-5-4 上午10:24:39 
 *
 * @version V1.0   
 */
public interface BizCallBackGetRelationship {

	public void getRelationshipCallBack(int retCode, RelationshipInfo relation);
}
