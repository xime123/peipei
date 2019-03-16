package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqTipoffTopic;
import com.tshang.peipei.protocol.asn.gogirl.RspTipoffTopic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestTipoffTopic.java 
 *
 * @Description: 举报动态详情 
 *
 * @author allen  
 *
 * @date 2014-7-5 上午10:13:56 
 *
 * @version V1.0   
 */
public class RequestTipoffTopic extends AsnBase implements ISocketMsgCallBack {

	public iTIPOFFTOPIC callback;

	public void tipoffTopic(byte[] auth, int ver, int uid, int topicuid, int topicid, int reasonid, iTIPOFFTOPIC callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqTipoffTopic req = new ReqTipoffTopic();
		req.actuid = BigInteger.valueOf(uid);
		req.tipoffuid = BigInteger.valueOf(topicuid);
		req.tipofftopicid = BigInteger.valueOf(topicid);
		req.tipoffcommentid = BigInteger.valueOf(-1);
		req.tipoffreplyid = BigInteger.valueOf(-1);
		req.reasonid = BigInteger.valueOf(reasonid);
		req.reasonother = "".getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQTIPOFFTOPIC_CID;
		goGirlPkt.reqtipofftopic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {
			RspTipoffTopic rsp = pkt.rsptipofftopic;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.tipoffback(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.tipoffback(resultCode);
		}
	}

	public interface iTIPOFFTOPIC {
		public void tipoffback(int retCode);
	}

}
