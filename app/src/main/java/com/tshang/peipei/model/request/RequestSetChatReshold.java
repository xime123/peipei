package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSetChatThreshold;
import com.tshang.peipei.protocol.asn.gogirl.RspSetChatThreshold;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestSetChatReshold.java 
 *
 * @Description: 私聊门槛
 *
 * @author vactor
 *
 * @date 2014-5-9 下午1:40:12 
 *
 * @version V1.0   
 */
public class RequestSetChatReshold extends AsnBase implements ISocketMsgCallBack {
	ISetChatReshold setChatReshold;

	public void setChatReshold(byte[] auth, int ver, int uid, int value, ISetChatReshold callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSetChatThreshold req = new ReqSetChatThreshold();
		req.threshold = BigInteger.valueOf(value);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSETCHATTHRESHOLD_CID;
		goGirlPkt.reqsetchatthreshold = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		this.setChatReshold = callBack;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != setChatReshold) {
			RspSetChatThreshold rsp = pkt.rspsetchatthreshold;
			int retCode = rsp.retcode.intValue();
			setChatReshold.setChatResholdCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != setChatReshold) {
			setChatReshold.setChatResholdCallBack(resultCode);
		}
	}

	public interface ISetChatReshold {
		public void setChatResholdCallBack(int retCode);
	}

}
