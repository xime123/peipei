package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlIntList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSendBroadcastVoice;
import com.tshang.peipei.protocol.asn.gogirl.RspSendBroadcastVoice;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 发送广播语音
 * @author Jeff
 *
 */
public class RequestSendBroadcastVoice extends AsnBase implements ISocketMsgCallBack {
	private BizCallBackSendBroadcastVoice callback;

	public void sendVoiceBroadcast(byte[] auth, int ver, int broadcasttype, GoGirlIntList touids, int uid, byte[] voicedata, int voicelen,
			BizCallBackSendBroadcastVoice callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSendBroadcastVoice req = new ReqSendBroadcastVoice();
		req.broadcasttype = BigInteger.valueOf(broadcasttype);
		req.touids = touids;
		req.uid = BigInteger.valueOf(uid);
		req.voicedata = voicedata;
		req.voicelen = BigInteger.valueOf(voicelen);

		this.callback = callback;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSENDBROADCASTVOICE_CID;
		goGirlPkt.reqsendbroadcastvoice = req;
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
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspSendBroadcastVoice rsp = pkt.rspsendbroadcastvoice;
			int retCode = rsp.retcode.intValue();
			BroadcastInfo info = rsp.broadcast;
			if (checkRetCode(retCode))
				callback.sendCallBack(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.sendCallBack(resultCode, null);
		}

	}

	public interface BizCallBackSendBroadcastVoice {
		public void sendCallBack(int retCode, BroadcastInfo info);
	}

}
