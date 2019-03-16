package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetTopicCounter;
import com.tshang.peipei.protocol.asn.gogirl.RspGetTopicCounter;
import com.tshang.peipei.protocol.asn.gogirl.TopicCounterInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetTopicCounter.java 
 *
 * @Description: 单条计数
 *
 * @author vactor
 *
 * @date 2014-4-22 下午5:36:01 
 *
 * @version V1.0   
 */
public class RequestGetTopicCounter extends AsnBase implements ISocketMsgCallBack {

	private IGetTopicCounter getTopicCounter;

	public void getTopicCounter(byte[] auth, int ver, int topicUid, int topicId, int isGetCommentNum, IGetTopicCounter callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetTopicCounter req = new ReqGetTopicCounter();
		req.topicid = BigInteger.valueOf(topicId);
		req.topicuid = BigInteger.valueOf(topicUid);
		req.isgetcommentnum = BigInteger.valueOf(isGetCommentNum);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETTOPICCOUNTER_CID;
		goGirlPkt.reqgettopiccounter = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getTopicCounter = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getTopicCounter) {
			RspGetTopicCounter rsp = pkt.rspgettopiccounter;
			TopicCounterInfo info = rsp.topiccounter;
			int retCode = rsp.retcode.intValue();
			getTopicCounter.getTopicCounterCallBack(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getTopicCounter) {
			getTopicCounter.getTopicCounterCallBack(resultCode, null);
		}
	}

	public interface IGetTopicCounter {
		public void getTopicCounterCallBack(int retCode, TopicCounterInfo toicCountInfo);
	}

}
