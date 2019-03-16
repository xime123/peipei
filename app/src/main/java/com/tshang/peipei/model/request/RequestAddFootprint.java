package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddFootprint;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddFootprint.java 
 *
 * @Description: 我看过谁
 *
 * @author allen  
 *
 * @date 2014-8-8 上午11:18:23 
 *
 * @version V1.0   
 */
public class RequestAddFootprint extends AsnBase implements ISocketMsgCallBack {

	public void addFootPrint(byte[] auth, int ver, int uid, int hostuid) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddFootprint req = new ReqAddFootprint();
		req.selfuid = BigInteger.valueOf(uid);
		req.hostuid = BigInteger.valueOf(hostuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDFOOTPRINT_CID;
		goGirlPkt.reqaddfootprint = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {}

	@Override
	public void error(int resultCode) {}

}
