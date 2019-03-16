package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlIntList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSendBroadcast;
import com.tshang.peipei.protocol.asn.gogirl.RspSendBroadcast;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 
 * @author Jeff
 * 发送广播
 */
public class RequestSendBroadcast extends AsnBase implements ISocketMsgCallBack {

	ISendBroadCallBack iSendBroadCastCallBack;

	public void sendBroadCast(byte[] auth, int ver, String content, int type, GoGirlIntList gogirlIntList, int uid, ISendBroadCallBack callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSendBroadcast req = new ReqSendBroadcast();
		req.broadcasttxt = content.getBytes();
		req.broadcasttype = BigInteger.valueOf(type);
		req.isusegoldcoin = BigInteger.valueOf(0);
		req.touids = gogirlIntList;
		req.uid = BigInteger.valueOf(uid);
		req.revint = BigInteger.valueOf(0);
		req.revstr = "".getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSENDBROADCAST_CID;
		goGirlPkt.reqsendbroadcast = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iSendBroadCastCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iSendBroadCastCallBack) {
			RspSendBroadcast rsp = pkt.rspsendbroadcast;
			int retCode = rsp.retcode.intValue();
			String str = new String(rsp.retmsg);
			if (checkRetCode(retCode))
				iSendBroadCastCallBack.sendBraodCallBack(retCode, str);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iSendBroadCastCallBack) {
			iSendBroadCastCallBack.sendBraodCallBack(resultCode, "加载失败");
		}

	}

	public interface ISendBroadCallBack {
		public void sendBraodCallBack(int retCode, String str);
	}

}
