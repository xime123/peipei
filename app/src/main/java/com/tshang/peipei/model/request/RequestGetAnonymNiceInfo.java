package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAnonymNickInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAnonymNickInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardPrivateChat.java 
 *
 * @Description: 获取Nick
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestGetAnonymNiceInfo extends AsnBase implements ISocketMsgCallBack {

	private GetAnonyNickInfoCallBack callBack;

	public void requestJoinReward(byte[] auth, int ver, int uid, GetAnonyNickInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAnonymNickInfo req = new ReqGetAnonymNickInfo();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETANONYMNICKINFO_CID;
		goGirlPkt.reqgetanonymnickinfo = req;
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
			RspGetAnonymNickInfo rsp = pkt.rspgetanonymnickinfo;
			int retCode = rsp.retcode.intValue();
			String message = new String(rsp.retmsg);
			int id = rsp.id.intValue();
			String nick = new String(rsp.nick);
			callBack.onGetAnonyNickInfoSuccess(retCode, message, id, nick);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGetAnonyNickInfoError(resultCode);
	}

	public interface GetAnonyNickInfoCallBack {
		public void onGetAnonyNickInfoSuccess(int code, String msg, int id, String nick);

		public void onGetAnonyNickInfoError(int code);
	}

}
