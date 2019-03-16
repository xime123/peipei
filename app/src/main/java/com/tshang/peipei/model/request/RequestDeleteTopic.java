package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDeleteTopic;
import com.tshang.peipei.protocol.asn.gogirl.RspDeleteTopic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDeleteTopic.java 
 *
 * @Description: 删除贴子
 *
 * @author vactor
 *
 * @date 2014-4-23 下午5:27:25 
 *
 * @version V1.0   
 */
public class RequestDeleteTopic extends AsnBase implements ISocketMsgCallBack {

	IDeleteTopic iDeleteTopic;

	public void delteTopic(byte[] auth, int ver, int topicId, int topicUid, int uid, IDeleteTopic callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDeleteTopic req = new ReqDeleteTopic();
		req.topicid = BigInteger.valueOf(topicId);
		req.topicuid = BigInteger.valueOf(topicUid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELETETOPIC_CID;
		goGirlPkt.reqdeletetopic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDeleteTopic = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iDeleteTopic) {
			RspDeleteTopic rsp = pkt.rspdeletetopic;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iDeleteTopic.deleteTopicCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDeleteTopic) {
			iDeleteTopic.deleteTopicCallBack(resultCode);
		}
	}

	public interface IDeleteTopic {
		public void deleteTopicCallBack(int retCode);
	}

}
