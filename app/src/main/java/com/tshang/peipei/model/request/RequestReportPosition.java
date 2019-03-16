package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqReportPosition;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestReportPosition.java 
 *
 * @Description: 上报地理信息
 *
 * @author allen  
 *
 * @date 2014-9-1 下午6:21:48 
 *
 * @version V1.0   
 */
public class RequestReportPosition extends AsnBase implements ISocketMsgCallBack {

	public void reportPostion(byte[] auth, int ver, int uid, int la, int lo) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqReportPosition req = new ReqReportPosition();
		req.uid = BigInteger.valueOf(uid);
		req.la = BigInteger.valueOf(la);
		req.lo = BigInteger.valueOf(lo);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQREPORTPOSITION_CID;
		goGirlPkt.reqreportposition = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {}

	@Override
	public void error(int resultCode) {}
}
