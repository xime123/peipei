package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRelevantCounter;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRelevantCounter;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetRelevantCounter.java 
 *
 * @Description: 获取访问量
 *
 * @author jeff  
 *
 * @date 2014-9-20 上午15:18:54 
 *
 * @version V1.3.0   
 */
public class RequestGetRelevantCounter extends AsnBase implements ISocketMsgCallBack {

	private IGetRelevantCounter addblack;

	public void getRelevantCounter(byte[] auth, int ver, int uid, IGetRelevantCounter callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRelevantCounter req = new ReqGetRelevantCounter();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRELEVANTCOUNTER_CID;
		goGirlPkt.reqgetrelevantcounter = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {
			RspGetRelevantCounter rsp = pkt.rspgetrelevantcounter;
			int fanscounter = rsp.fanscounter.intValue();
			int visitecounter = rsp.visitecounter.intValue();
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.getRelevantCounter(retCode, fanscounter, visitecounter);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.getRelevantCounter(resultCode, 0, 0);
		}

	}

	public interface IGetRelevantCounter {
		public void getRelevantCounter(int retCode, int fanscounter, int visitecounter);
	}

}
