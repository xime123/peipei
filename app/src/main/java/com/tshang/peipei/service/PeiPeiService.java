package com.tshang.peipei.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.android.pushservice.PushManager;

/**
 * 陪陪后台服务
 * 
 */
public class PeiPeiService extends Service {

	public final static String WHISPER_SERVICE_ACTION = "com.tshang.peipei.Service.PeiPeiService";

	public final static int START_DETECT_APPINFO_FLAG = 0x0;
	public final static int START_LONG_SOCKET_FLAG = 0x1;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		try {
			if (intent != null) {
				final int flag = intent.getIntExtra("flag", 0x0);

				switch (flag) {
				case START_DETECT_APPINFO_FLAG:
					startDetectAppInfo();
					break;
				case START_LONG_SOCKET_FLAG:
					startSocketConn();
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
		//	PushManager.stopWork(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startDetectAppInfo() {}

	/**
	 * 启动后台长连接
	 */
	private void startSocketConn() {

	}

}
