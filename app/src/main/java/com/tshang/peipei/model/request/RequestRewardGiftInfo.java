package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AwardGiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAwardGiftList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAwardGiftList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardGiftInfo.java 
 *
 * @Description: 获取悬赏礼物列表
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardGiftInfo extends AsnBase implements ISocketMsgCallBack {

	private GetRewardGiftInfoCallBack callBack;

	public void requestRewardGiftInfo(byte[] auth, int ver, int uid, int sex, GetRewardGiftInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAwardGiftList req = new ReqGetAwardGiftList();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(sex);
		//
		//		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAWARDGIFTLIST_CID;
		goGirlPkt.reqgetawardgiftlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetAwardGiftList rsp = pkt.rspgetawardgiftlist;
			int retCode = rsp.retcode.intValue();
			AwardGiftInfoList list = rsp.awardgiftlist;
			callBack.onGiftSuccess(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGiftError(resultCode);
	}

	public interface GetRewardGiftInfoCallBack {
		public void onGiftSuccess(int code, Object obj);

		public void onGiftError(int code);
	}

}
