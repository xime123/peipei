package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUserTopicList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUserTopicList;
import com.tshang.peipei.protocol.asn.gogirl.TopicInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetUserTopicList.java 
 *
 * @Description: 获取到动态列表
 *
 * @author vactor
 *
 * @date 2014-4-12 下午5:23:15 
 *
 * @version V1.0   
 */
public class RequestGetUserTopicList extends AsnBase implements ISocketMsgCallBack {

	private IgetUserTopicList mGetUserTopicList;
	private int code;

	public void getUserTopicList(byte[] auth, int ver, int uid, int selfUid, int type, int start, int num, int code,
			IgetUserTopicList getUserTopicList) {
		//头部信息
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		//包体
		ReqGetUserTopicList req = new ReqGetUserTopicList();
		req.num = BigInteger.valueOf(num);
		req.selfuid = BigInteger.valueOf(selfUid);
		req.start = BigInteger.valueOf(start);
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUSERTOPICLIST_CID;
		goGirlPkt.reqgetusertopiclist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		this.code = code;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGetUserTopicList = getUserTopicList;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetUserTopicList) {
			RspGetUserTopicList rsp = pkt.rspgetusertopiclist;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			int total = rsp.total.intValue();
			TopicInfoList list = rsp.topiclist;
			mGetUserTopicList.getUserTopicListCallBack(retCode, isEnd, total, code, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGetUserTopicList) {
			mGetUserTopicList.getUserTopicListCallBack(resultCode, -1, -1, code, null);
		}
	}

	public interface IgetUserTopicList {
		public void getUserTopicListCallBack(int retCode, int isEnd, int total, int code, TopicInfoList list);
	}

}
