package com.tshang.peipei.model.bizcallback;

/**
 * @Title: BizCallBackPersist.java 
 *
 * @Description: 创建长连接接口
 *
 * @author allen  
 *
 * @date 2014-4-15 下午5:15:22 
 *
 * @version V1.0   
 */
public interface BizCallBackPersist {
	public void openPersistSer(int retCode, Object obj, int seq);
}
