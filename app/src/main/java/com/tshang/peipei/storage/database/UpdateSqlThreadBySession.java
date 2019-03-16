package com.tshang.peipei.storage.database;

import java.util.ArrayList;
import java.util.List;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.request.RequestGetUserInfo;
import com.tshang.peipei.model.request.RequestGetUserInfo.IGetUserInfo;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;

/**
 * @Title: UpdateSqlThreadBySession.java 
 *
 * @Description: 升级数据库数据线程 
 *
 * @author allen  
 *
 * @date 2014-7-23 下午3:35:08 
 *
 * @version V1.0   
 */
public class UpdateSqlThreadBySession extends Thread implements IGetUserInfo {

	List<SessionDatabaseEntity> list = new ArrayList<SessionDatabaseEntity>();

	public UpdateSqlThreadBySession(List<SessionDatabaseEntity> list) {
		this.list = list;
	}

	public void run() {
		for (int i = 0; i < list.size(); i++) {
			RequestGetUserInfo req = new RequestGetUserInfo();
			req.getUserInfo("".getBytes(), BAApplication.app_version_code, list.get(i).getUserID(), this);
		}

	}

	@Override
	public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo) {
		if (retCode == 0) {
			PeipeiSessionOperate peipeiSessionOperate = PeipeiSessionOperate.getInstance(BAApplication.getInstance().getApplicationContext());
			if (peipeiSessionOperate != null)
				peipeiSessionOperate.updateNickAndSex(userinfo.uid.intValue(), new String(userinfo.nick), userinfo.sex.intValue(), 0);
			peipeiSessionOperate = null;
		}
	}
}
