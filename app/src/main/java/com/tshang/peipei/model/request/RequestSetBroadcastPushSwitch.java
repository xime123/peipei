package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSetBroadcastPushSwitch;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 广播是否主动推送开关
 * @author Jeff
 *
 */
public class RequestSetBroadcastPushSwitch extends AsnBase implements ISocketMsgCallBack {

	public void setBraodcastPushSwitch(byte[] auth, int ver, int act, int type, int uid) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqSetBroadcastPushSwitch req = new ReqSetBroadcastPushSwitch();
		req.act = BigInteger.valueOf(act);
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSETBROADCASTPUSHSWITCH_CID;
		goGirlPkt.reqsetbroadcastpushswitch = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);

		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		//		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		//		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		//
		//		RspSetBroadcastPushSwitch rsp = pkt.rspsetbroadcastpushswitch;
		//		int retCode = rsp.retcode.intValue();
	}

	@Override
	public void error(int resultCode) {

	}

}
