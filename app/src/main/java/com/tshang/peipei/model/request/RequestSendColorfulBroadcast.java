package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlIntList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSendColorfulBroadcast;
import com.tshang.peipei.protocol.asn.gogirl.RspSendColorfulBroadcast;
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
public class RequestSendColorfulBroadcast extends AsnBase implements ISocketMsgCallBack {

	ISendColorBroadCallBack iSendBroadCastCallBack;

	public void sendColorBroadCast(byte[] auth, int ver, int type, GoGirlDataInfoList dataInfoList, GoGirlIntList gogirlIntList, int uid,
			ISendColorBroadCallBack callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSendColorfulBroadcast req = new ReqSendColorfulBroadcast();
		req.broadcasttype = BigInteger.valueOf(type);
		req.touids = gogirlIntList;
		req.uid = BigInteger.valueOf(uid);
		req.datalist = dataInfoList;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSENDCOLORFULBROADCAST_CID;
		goGirlPkt.reqsendcolorfulbroadcast = req;
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
			RspSendColorfulBroadcast rsp = pkt.rspsendcolorfulbroadcast;
			int retCode = rsp.retcode.intValue();
			String str = new String(rsp.retmsg);
			if (checkRetCode(retCode))
				iSendBroadCastCallBack.sendBraodCallBack(retCode, str);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iSendBroadCastCallBack) {
			iSendBroadCastCallBack.sendBraodCallBack(resultCode, "发送失败");
		}

	}

	public interface ISendColorBroadCallBack {
		public void sendBraodCallBack(int retCode, String str);
	}

}
