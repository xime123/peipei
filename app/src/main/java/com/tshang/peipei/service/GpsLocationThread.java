package com.tshang.peipei.service;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.request.RequestReportPosition;
import com.tshang.peipei.storage.SharedPreferencesTools;

/**
 * @Title: GpsLocationThread.java 
 *
 * @Description: 定位
 *
 * @author allen  
 *
 * @date 2014-8-29 上午11:49:12 
 *
 * @version V1.0   
 */
public class GpsLocationThread extends Thread implements BDLocationListener {

	private final String TAG = this.getClass().getSimpleName();
	//定位
	public LocationClient mLocationClient;

	private Context context;

	public GpsLocationThread(Context context) {
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(this);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000 * 60 * 10;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\ndirection : ");
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append(location.getDirection());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			//运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
		}

		mLocationClient.stop();

		if (BAApplication.mLocalUserInfo != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
			RequestReportPosition requestReportPosition = new RequestReportPosition();
			requestReportPosition.reportPostion(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
					BAApplication.mLocalUserInfo.uid.intValue(), (int) (location.getLatitude() * 100000), (int) (location.getLongitude() * 100000));
		}

		SharedPreferencesTools.getInstance(BAApplication.getInstance().getApplicationContext()).saveIntKeyValue(
				(int) (location.getLatitude() * 100000), BAConstants.PEIPEI_GPS_LA);
		SharedPreferencesTools.getInstance(BAApplication.getInstance().getApplicationContext()).saveIntKeyValue(
				(int) (location.getLongitude() * 100000), BAConstants.PEIPEI_GPS_LO);

		ILog.d(TAG, "str===" + sb.toString());
	}

	public void run() {
		mLocationClient.start();
	}
}
