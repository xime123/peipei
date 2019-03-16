package com.tshang.peipei.model.showrooms;

import android.content.Context;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestCloseShow;
import com.tshang.peipei.model.request.RequestCloseShow.ICloseShow;
import com.tshang.peipei.model.request.RequestInOutRoom;
import com.tshang.peipei.model.request.RequestInOutRoom.IInOutRoom;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 * @Title: RoomsPublicBiz.java 
 *
 * @Description: 秀场一些公用方法 
 *
 * @author allen  
 *
 * @date 2015-2-7 下午7:39:06 
 *
 * @version V1.0   
 */
public class RoomsPublicBiz implements IInOutRoom, ICloseShow {

	public void InOutRooms(Context activity, int act, int showRoomId, int roomUid) {
		RequestInOutRoom req = new RequestInOutRoom();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		req.InOutRoom(auth, BAApplication.app_version_code, act, showRoomId, uid, roomUid, this);
	}

	@Override
	public void resultInOut(int retCode, int act, int role) {}

	public void finishShow(Context activity, int act) {
		RequestCloseShow req = new RequestCloseShow();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		byte[] auth = "".getBytes();
		int uid = 0;
		if (info != null) {
			auth = info.auth;
			uid = info.uid.intValue();
		}
		if (BAApplication.showRoomInfo != null) {
			req.closeShow(auth, BAApplication.app_version_code, BAApplication.showRoomInfo.getRoomid(), uid, act, this);
		}
	}

	@Override
	public void resultCloseShow(int retCode) {}
}
