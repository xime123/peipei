package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqFeedbackKiss;
import com.tshang.peipei.protocol.asn.gogirl.RspFeedbackKiss;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestFeedbackKiss.java 
 *
 * @Description: 回赠飞吻
 *
 * @author allen  
 *
 * @date 2014-6-27 下午2:58:55 
 *
 * @version V1.0   
 */
public class RequestFeedbackKiss extends AsnBase implements ISocketMsgCallBack {

	private iFeedBack callback;

	public void feedbackKiss(byte[] auth, int ver, int uid, int fuid, int dealid, iFeedBack callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqFeedbackKiss req = new ReqFeedbackKiss();
		req.fromuid = BigInteger.valueOf(uid);
		req.touid = BigInteger.valueOf(fuid);
		req.giftdealid = BigInteger.valueOf(dealid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQFEEDBACKKISS_CID;
		goGirlPkt.reqfeedbackkiss = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.callback = callback;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspFeedbackKiss rsp = pkt.rspfeedbackkiss;

		if (null != callback) {
			if (checkRetCode(rsp.retcode.intValue())) {
				callback.feedback(rsp.retcode.intValue());
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.feedback(resultCode);
		}

	}

	public interface iFeedBack {
		public void feedback(int resultCode);
	}

}
