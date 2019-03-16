package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUseAnonymNickInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUseAnonymNickInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardPrivateChat.java 
 *
 * @Description: 使用匿名Nick
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestUserAnonymNiceInfo extends AsnBase implements ISocketMsgCallBack {

	private GetUserAnonyNickCallBack callBack;

	public void requestJoinReward(byte[] auth, int ver, int uid, int id, GetUserAnonyNickCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetUseAnonymNickInfo req = new ReqGetUseAnonymNickInfo();
		req.uid = BigInteger.valueOf(uid);
		req.id = BigInteger.valueOf(id);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUSEANONYMNICKINFO_CID;
		goGirlPkt.reqgetuseanonymnickinfo = req;
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
			RspGetUseAnonymNickInfo rsp = pkt.rspgetuseanonymnickinfo;
			int retCode = rsp.retcode.intValue();
			String message = new String(rsp.retmsg);
			callBack.onGetUserAnonyNickSuccess(retCode, message);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGetUserAnonyNickError(resultCode);
	}

	public interface GetUserAnonyNickCallBack {
		public void onGetUserAnonyNickSuccess(int code, String msg);

		public void onGetUserAnonyNickError(int code);
	}

}
