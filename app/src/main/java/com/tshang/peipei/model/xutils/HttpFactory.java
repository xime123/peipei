package com.tshang.peipei.model.xutils;

import android.app.Activity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @Title: HttpFactory.java 
 *
 * @Description: Http请求工厂类
 *
 * @author Aaron  
 *
 * @date 2015-8-13 下午1:49:15 
 *
 * @version V1.0   
 */
public class HttpFactory {
	private static HttpUtils mHttpUtils;

	public static HttpUtils getInstance() {
		if (mHttpUtils == null) {
			mHttpUtils = new HttpUtils();
			mHttpUtils.configDefaultHttpCacheExpiry(0);
		}
		return mHttpUtils;
	}

	/**
	 * 
	 * @author HttpGet请求
	 *
	 * @param activity
	 * @param url
	 * @param isShowDialog
	 * @param msg
	 * @param callBack
	 */
	public static void httpGet(final Activity activity, String url, final boolean isShowDialog, final String msg, final HttpRequestCallBack callBack) {
		getInstance().send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				callBack.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				callBack.onSuccess(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				callBack.onError(msg);
			}
		});
	}
}
