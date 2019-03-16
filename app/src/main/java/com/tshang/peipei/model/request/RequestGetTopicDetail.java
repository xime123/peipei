package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.CommentInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetTopicDetail;
import com.tshang.peipei.protocol.asn.gogirl.RspGetTopicDetail;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetTopicDetail.java 
 *
 * @Description:  动态详情
 *
 * @author vactor
 *
 * @date 2014-4-19 下午2:09:23 
 *
 * @version V1.0   
 */
public class RequestGetTopicDetail extends AsnBase implements ISocketMsgCallBack {

	private IGetTopicDetail getTopicDetailCallBack;

	public void getTopicDetail(byte[] auth, int ver, int uid, int topicUid, int topicid, int isGetTopic, int start, int num, IGetTopicDetail callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetTopicDetail req = new ReqGetTopicDetail();
		req.isgettopic = BigInteger.valueOf(isGetTopic);
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicUid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETTOPICDETAIL_CID;
		goGirlPkt.reqgettopicdetail = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getTopicDetailCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getTopicDetailCallBack) {
			RspGetTopicDetail rsp = pkt.rspgettopicdetail;
			int retCode = rsp.retcode.intValue();
			TopicInfo topicInfo = rsp.topicinfo;
			CommentInfoList commentList = rsp.commentlist;
			int commmentTotal = rsp.commenttotal.intValue();
			getTopicDetailCallBack.getTopicDetailCallBack(retCode, topicInfo, commentList, commmentTotal);
		}

	}

	@Override
	public void error(int resultCode) {

		if (null != getTopicDetailCallBack) {
			getTopicDetailCallBack.getTopicDetailCallBack(resultCode, null, null, -1);
		}
	}

	public interface IGetTopicDetail {
		public void getTopicDetailCallBack(int retCode, TopicInfo topicInfo, CommentInfoList commentList, int commentTotal);

	}

}
