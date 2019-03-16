package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetBindAnonymNickEx;
import com.tshang.peipei.protocol.asn.gogirl.RspGetBindAnonymNickEx;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardPrivateChat.java 
 *
 * @Description: 匿名Nick绑定检测
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestBindAnonymNice extends AsnBase implements ISocketMsgCallBack {

	private BindAnonymNickCallBack callBack;

	public void requestJoinReward(byte[] auth, int ver, int uid, BindAnonymNickCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetBindAnonymNickEx req = new ReqGetBindAnonymNickEx();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETBINDANONYMNICKEX_CID;
		goGirlPkt.reqgetbindanonymnickex = req;
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
			RspGetBindAnonymNickEx rsp = pkt.rspgetbindanonymnickex;
			int retCode = rsp.retcode.intValue();
			String message = new String(rsp.retmsg);
			int id = rsp.id.intValue();
			String nick = new String(rsp.nick);
			int status = rsp.status.intValue();
			callBack.onBindAnonymNickSuccess(retCode, message, id, nick, status);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onBindAnonymNickError(resultCode);
	}

	public interface BindAnonymNickCallBack {
		public void onBindAnonymNickSuccess(int code, String msg, int id, String nick, int status);

		public void onBindAnonymNickError(int code);
	}
}
