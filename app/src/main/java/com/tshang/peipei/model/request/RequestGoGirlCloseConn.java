package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlCloseConn;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGoGirlCloseConn.java 
 *
 * @Description: 关闭长连接
 *
 * @author allen  
 *
 * @date 2014-6-27 下午4:49:40 
 *
 * @version V1.0   
 */
public class RequestGoGirlCloseConn extends AsnBase implements ISocketMsgCallBack {

	public void closeConn(byte[] auth, int ver, int uid) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqGoGirlCloseConn req = new ReqGoGirlCloseConn();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLCLOSECONN_CID;
		goGirlPkt.reqgogirlcloseconn = req;
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
