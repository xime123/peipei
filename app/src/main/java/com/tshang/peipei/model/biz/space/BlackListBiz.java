package com.tshang.peipei.model.biz.space;

import android.os.Message;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.request.RequestGetBlackList;
import com.tshang.peipei.model.request.RequestGetBlackList.iGetBlackList;
import com.tshang.peipei.model.request.RequestRemoveBlacklist;
import com.tshang.peipei.model.request.RequestRemoveBlacklist.iRemoveBlacklist;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfoList;

/**
 * @Title: BlackListBiz.java 
 *
 * @Description: 黑名单操作
 *
 * @author allen  
 *
 * @date 2014-10-16 下午1:45:51 
 *
 * @version V1.0   
 */
public class BlackListBiz implements iGetBlackList, iRemoveBlacklist {
	private BAHandler handler;

	public void getBlackList(byte[] auth, int ver, int uid, int start, int num, BAHandler handler) {
		RequestGetBlackList req = new RequestGetBlackList();
		this.handler = handler;
		req.getBlackList(auth, ver, uid, start, num, this);
	}

	@Override
	public void resultBlackList(int code, int end, RetRelevantPeopleInfoList list) {
		if (handler != null) {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.BLACK_LIST_SUCCESS;
			msg.arg1 = code;
			msg.arg2 = end;
			msg.obj = list;
			handler.sendMessage(msg);
		}

	}

	public void removeBlackList(byte[] auth, int ver, int uid, int blackuid, BAHandler handler) {
		RequestRemoveBlacklist req = new RequestRemoveBlacklist();
		this.handler = handler;
		req.removeBlacklist(auth, ver, uid, blackuid, this);
	}

	@Override
	public void resultBlacklist(int retCode, int blackid) {
		if (handler != null) {
			Message msg = handler.obtainMessage();
			msg.what = HandlerValue.BLACK_LIST_REMOVE;
			msg.arg1 = retCode;
			msg.arg2 = blackid;
			handler.sendMessage(msg);
		}
	}
}
