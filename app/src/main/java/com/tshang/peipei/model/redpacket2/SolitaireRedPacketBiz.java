package com.tshang.peipei.model.redpacket2;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.model.request.RequestAvailHallRedpacketList;
import com.tshang.peipei.model.request.RequestCheckHallRedpacketStatus;
import com.tshang.peipei.model.request.RequestCheckRedpacketState;
import com.tshang.peipei.model.request.RequestGrabHallRedpacket;
import com.tshang.peipei.model.request.RequestGrapSolitaireRedpacket;
import com.tshang.peipei.model.request.RequestHallRedpacketAvail;
import com.tshang.peipei.model.request.RequestHallRedpacketTip;
import com.tshang.peipei.model.request.RequestAvailHallRedpacketList.GetAvailHallRedpacketListCallBack;
import com.tshang.peipei.model.request.RequestCheckHallRedpacketStatus.CheckHallRedpacketStatusCallBack;
import com.tshang.peipei.model.request.RequestCheckRedpacketState.CheckRedpacketStateCallBack;
import com.tshang.peipei.model.request.RequestGrabHallRedpacket.GrabHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestGrapSolitaireRedpacket.GetGrapSolitaireRedPacketCallBack;
import com.tshang.peipei.model.request.RequestHallRedpacketAvail.GetHallRedpacketAvailCallBack;
import com.tshang.peipei.model.request.RequestHallRedpacketTip.GetHallRedpacketTipCallBack;
import com.tshang.peipei.model.request.RequestIsSendHallRedpacket.GetIsSendHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestIsSendHallRedpacket;
import com.tshang.peipei.model.request.RequestSendHallRedpacket;
import com.tshang.peipei.model.request.RequestSendHallRedpacket.SendHallRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSendSolitaireRedpacket;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney;
import com.tshang.peipei.model.request.RequestSendSolitaireRedpacket.GetSendSolitaireRedpacketCallBack;
import com.tshang.peipei.model.request.RequestSolitaireRedPacketMoney.GetSolitaireRedPacketCallBack;

/**
 * @Title: SolitaireRedPacketBiz.java 
 *
 * @Description: 接龙红包的逻辑类
 *
 * @author DYH  
 *
 * @date 2015-12-8 下午7:16:47 
 *
 * @version V1.0   
 */
public class SolitaireRedPacketBiz {

	public void reqeustSolitaireRedPacketMoney(int type, GetSolitaireRedPacketCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestSolitaireRedPacketMoney req = new RequestSolitaireRedPacketMoney();
		req.requestSolitaireRedPacketMoney(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, callBack);
	}

	public void requestSendSolitaireRedPacket(int redPacketId, int togroupid, int type, GetSendSolitaireRedpacketCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestSendSolitaireRedpacket req = new RequestSendSolitaireRedpacket();
		req.requestSendSolitaireRedpacket(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), redPacketId, togroupid, type, callBack);
	}

	public void requestGrapSolitaireRedPacket(int redpacketuid, int redpacketid, int type, GetGrapSolitaireRedPacketCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestGrapSolitaireRedpacket req = new RequestGrapSolitaireRedpacket();
		req.requestGrapSolitaireRedPacket(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), redpacketuid, redpacketid, type, callBack);
	}

	public void requestCheckRekpackeState(int redPacketId, int type, CheckRedpacketStateCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestCheckRedpacketState req = new RequestCheckRedpacketState();
		req.requestCheckRedpacketState(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), redPacketId, type, callBack);
	}

	public void requestHallRedpacketTip(int type, GetHallRedpacketTipCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestHallRedpacketTip req = new RequestHallRedpacketTip();
		req.requestHallRedpacketTip(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				type, callBack);
	}

	public void requestSendHallRedpacket(int type, int sndtype, long totalgoldcoin, long totalsilvercoin, int portionnum, String desc,
			SendHallRedpacketCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestSendHallRedpacket req = new RequestSendHallRedpacket();
		req.requestSendHallRedpacket(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				type, sndtype, totalgoldcoin, totalsilvercoin, portionnum, desc, callBack);
	}

	public void requestGrabHallRedpacket(int redpacketuid, int redpacketid, GrabHallRedpacketCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestGrabHallRedpacket req = new RequestGrabHallRedpacket();
		req.requestGrabHallRedpacket(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code, BAApplication.mLocalUserInfo.uid.intValue(),
				redpacketuid, redpacketid, callBack);
	}

	public void requestCheckHallRecpacket(int redpacketid, int type, CheckHallRedpacketStatusCallBack callBack) {
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}

		RequestCheckHallRedpacketStatus req = new RequestCheckHallRedpacketStatus();
		req.requestCheckHallRedpacketStatus(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), redpacketid, type, callBack);
	}
	
	public void requestCurrentRedpacketAvail(int type, GetHallRedpacketAvailCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		
		RequestHallRedpacketAvail req = new RequestHallRedpacketAvail();
		req.requestHallRedpacketAvail(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, callBack);
	}
	
	public void requestGetAvailRedpacketList(int type, GetAvailHallRedpacketListCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		
		RequestAvailHallRedpacketList req = new RequestAvailHallRedpacketList();
		req.requestAvailHallRedpacketList(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, callBack);
	}
	
	public void requestIsSendHallRedpacket(int type, GetIsSendHallRedpacketCallBack callBack){
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		
		RequestIsSendHallRedpacket req = new RequestIsSendHallRedpacket();
		req.requestIsSendHallRedpacket(BAApplication.mLocalUserInfo.auth, BAApplication.app_version_code,
				BAApplication.mLocalUserInfo.uid.intValue(), type, callBack);
	}
}
