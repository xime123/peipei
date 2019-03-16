package com.tshang.peipei.model.suspension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.entity.SuspensionActEntity;
import com.tshang.peipei.model.entity.SuspensionEntity;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.model.xutils.HttpFactory;
import com.tshang.peipei.model.xutils.HttpRequestCallBack;

import de.greenrobot.event.EventBus;

/**
 * @Title: RequestSuspInfo.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-9-25 上午9:57:03 
 *
 * @version V1.0   
 */
public class RequestSuspInfo {

	/**
	 * 获取悬浮配制数据
	 *
	 */
	public static void requstSuspensionData(Activity ctx) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		
		if(BAApplication.getInstance().getSuspEntity() != null){
			long curtime = System.currentTimeMillis() / 1000;
			long pretime = BAApplication.getInstance().getSuspEntity().getNextTime();
			ILog.d("DYH", "curtime " + curtime);
			ILog.d("DYH", "pretime " + pretime);
			
			if(curtime-pretime < 0){
				return;
			}
		}
		
		String url = null;
		if (BAConstants.IS_TEST) {
			url = BAConstants.SUSPENSION_URL_TEST + BAApplication.mLocalUserInfo.auth + "&p=android&v" + BaseTools.getAppVersionName(ctx) + "&sex="
					+ BAApplication.mLocalUserInfo.sex.intValue();
		} else {
			url = BAConstants.SUSPENSION_URL + BAApplication.mLocalUserInfo.auth + "&p=android&v" + BaseTools.getAppVersionName(ctx) + "&sex="
					+ BAApplication.mLocalUserInfo.sex.intValue();
		}
		HttpFactory.httpGet(ctx, url, false, "", new HttpRequestCallBack() {

			@Override
			public void onSuccess(String result) {
				try {
					parse(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				ILog.e("Aaron", "onError error==" + error);
			}
		});
	}
	
	private static void parse(String str) throws JSONException{
		ILog.d("Aaron", "susp" + str);
		JSONObject jsonObj = new JSONObject(str);
		SuspensionEntity entity = new SuspensionEntity();
		entity.setShow(jsonObj.getInt("show"));
		entity.setNextTime(jsonObj.getLong("nextTime"));
		JSONArray jsonArr = jsonObj.getJSONArray("activities");
		for(int i=0; i<jsonArr.length(); i++){
			JSONObject obj = jsonArr.getJSONObject(i);
			SuspensionActEntity actEntity = new SuspensionActEntity();
			actEntity.setStatus(obj.getInt("status"));
			actEntity.setNumber(obj.getInt("number"));
			actEntity.setUrl(obj.getString("url"));
			actEntity.setImage(obj.getString("image"));
			entity.getActList().add(actEntity);
		}
		
		BAApplication.getInstance().setSuspEntity(entity);
		NoticeEvent notice = new NoticeEvent();
		notice.setFlag(NoticeEvent.NOTICE93);
		EventBus.getDefault().post(notice);
	}
}
