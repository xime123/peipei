package com.tshang.peipei.model.xutils;

/**
 * @Title: HttpRequestCallBack.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-8-13 下午1:53:23 
 *
 * @version V1.0   
 */
public interface HttpRequestCallBack {

	public void onStart();

	public void onSuccess(String result);

	public void onError(String error);
}
