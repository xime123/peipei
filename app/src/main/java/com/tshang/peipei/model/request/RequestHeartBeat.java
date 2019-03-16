package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlHeartBeat;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestHeartBeat.java 
 *
 * @Description: 心跳回包 
 *
 * @author allen  
 *
 * @date 2014-4-29 上午11:37:01 
 *
 * @version V1.0   
 */
public class RequestHeartBeat extends AsnBase {

	public void sentHeartBeat(byte[] auth, int ver, int retcode, int seq, ISocketMsgCallBack callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver, seq);
		// 组建包体
		RspGoGirlHeartBeat req = new RspGoGirlHeartBeat();
		req.retcode = BigInteger.valueOf(retcode);
		req.retmsg = "".getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.RSPGOGIRLHEARTBEAT_CID;
		goGirlPkt.rspgogirlheartbeat = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, callback, true);
		AppQueueManager.getInstance().addRequest(request);
	}
}
