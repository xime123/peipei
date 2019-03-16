package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUpvoteDynamics;
import com.tshang.peipei.protocol.asn.gogirl.RspUpvoteDynamics;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDynamicPraiseNumber.java 
 *
 * @Description: 点赞请求
 *
 * @author Administrator  
 *
 * @date 2015-8-21 下午2:58:37 
 *
 * @version V1.0   
 */
public class RequestDynamicPraiseNumber extends AsnBase implements ISocketMsgCallBack {

	private AppPariseCallBack callBack;
	private int topicuid;
	private int topicid;

	/**
	 * 
	 * @author Aaron
	 *
	 * @param auth
	 * @param ver
	 * @param fromuid 点赞人id
	 * @param topicid 动态id
	 * @param topicuid 发布动态人id
	
	 * @param upvotenum 1:点赞,-1取消点赞
	 * @param callBack
	 */
	public void requestPriaseNumber(byte[] auth, int ver, int fromuid, int topicid, int topicuid, int type, int upvotenum, int systemid,
			AppPariseCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqUpvoteDynamics req = new ReqUpvoteDynamics();
		req.topicuid = BigInteger.valueOf(topicuid);
		req.topicid = BigInteger.valueOf(topicid);
		req.appreciateinc = BigInteger.valueOf(upvotenum);
		req.fromuid = BigInteger.valueOf(fromuid);
		req.systemtopicid = BigInteger.valueOf(systemid);
		req.type = BigInteger.valueOf(type);
		req.revint0 = BigInteger.valueOf(0);
		req.revint1 = BigInteger.valueOf(0);
		req.revint2 = BigInteger.valueOf(0);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPVOTEDYNAMICS_CID;
		goGirlPkt.requpvotedynamics = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		this.topicid = topicid;
		this.topicuid = topicuid;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspUpvoteDynamics rsp = pkt.rspupvotedynamics;
			int retCode = rsp.retcode.intValue();
			callBack.onSuccess(retCode, topicuid, topicid);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onError(resultCode);
	}

	public interface AppPariseCallBack {
		public void onSuccess(int code, int topicuid, int topicid);

		public void onError(int code);
	}

}
