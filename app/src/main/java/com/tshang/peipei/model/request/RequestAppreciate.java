package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAppreciateTopic;
import com.tshang.peipei.protocol.asn.gogirl.RspAppreciateTopic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAppreciate.java 
 *
 * @Description: 赞操作
 *
 * @author vactor
 *
 * @date 2014-4-23 上午10:26:41 
 *
 * @version V1.0   
 */
public class RequestAppreciate extends AsnBase implements ISocketMsgCallBack {

	private IAppreciate iAppreciate;

	public void appreciate(byte[] auth, int ver, int topicId, int topicUid, int uid, int act, IAppreciate callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAppreciateTopic req = new ReqAppreciateTopic();
		req.act = BigInteger.valueOf(act);
		req.topicid = BigInteger.valueOf(topicId);
		req.topicuid = BigInteger.valueOf(topicUid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQAPPRECIATETOPIC_CID;
		goGirlPkt.reqappreciatetopic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iAppreciate = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iAppreciate) {
			RspAppreciateTopic rsp = pkt.rspappreciatetopic;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iAppreciate.appreciateCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iAppreciate) {
			iAppreciate.appreciateCallBack(resultCode);
		}
	}

	public interface IAppreciate {
		public void appreciateCallBack(int retCode);
	}

}
