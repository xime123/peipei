package com.tshang.peipei.model.biz;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.bizcallback.BizCallBackPersist;
import com.tshang.peipei.model.harem.CreateHarem;
import com.tshang.peipei.model.request.RequestGoGirlCloseConn;
import com.tshang.peipei.model.request.RequestPersist;
import com.tshang.peipei.model.request.RequestPersist.IPersistSer;

/**
 * @Title: PeiPeiPersistBiz.java 
 *
 * @Description: 长连接操作类
 *
 * @author allen  
 *
 * @date 2014-4-15 下午4:57:24 
 *
 * @version V1.0   
 */
public class PeiPeiPersistBiz implements IPersistSer {

	private static PeiPeiPersistBiz instance = null;

	public static PeiPeiPersistBiz getInstance() {
		if (instance == null) {
			synchronized (CreateHarem.class) {
				if (instance == null) {
					instance = new PeiPeiPersistBiz();
				}
			}
		}
		return instance;
	}

	public BizCallBackPersist mCallBack;

	public void openPersist(byte[] auth, int ver, int uid, BizCallBackPersist callBack) {
		mCallBack = callBack;
		if (!BAApplication.isCreateLongConnectedSuccess) {
			RequestPersist.getReqRessist().openPersist(auth, ver, uid, this);
		}

	}

	@Override
	public void getPersistSer(int retCode, Object obj, int seq) {
		mCallBack.openPersistSer(retCode, obj, seq);
	}

	public void closePersist(Activity activity) {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(activity);
		if (userInfo == null) {
			return;
		}
		RequestGoGirlCloseConn req = new RequestGoGirlCloseConn();
		req.closeConn(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue());
		RequestPersist.getReqRessist().closePersist();
	}
}
