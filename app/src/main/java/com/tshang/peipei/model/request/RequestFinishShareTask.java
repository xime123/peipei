package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqFinishShareTask;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestFinishShareTask.java 
 *
 * @Description: 分享成功 
 *
 * @author allen  
 *
 * @date 2014-8-16 下午5:45:26 
 *
 * @version V1.0   
 */
public class RequestFinishShareTask extends AsnBase implements ISocketMsgCallBack {

	public void finishShareTask(byte[] auth, int ver, int uid, int shareto) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqFinishShareTask req = new ReqFinishShareTask();
		req.uid = BigInteger.valueOf(uid);
		req.shareto = BigInteger.valueOf(shareto);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQFINISHSHARETASK_CID;
		goGirlPkt.reqfinishsharetask = req;
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
