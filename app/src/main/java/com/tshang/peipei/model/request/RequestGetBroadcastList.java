package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetBroadcastList77;
import com.tshang.peipei.protocol.asn.gogirl.RspGetBroadcastList77;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetUserTopicList.java 
 *
 * @Description: 获取广播列表
 *
 * @author Jeff
 *
 * @date 2014-6-25 下午5:23:15 
 *
 * @version V1.1   
 */
public class RequestGetBroadcastList extends AsnBase implements ISocketMsgCallBack {

	private IgetBroadcastList mGetBroadcastList;
	private int type = -1;

	public void getBroadcastList(byte[] auth, int ver, int uid, int type, int start, int num, IgetBroadcastList getUserTopicList) {
		this.type = type;
		//头部信息
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		//包体
		ReqGetBroadcastList77 req = new ReqGetBroadcastList77();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETBROADCASTLIST77_CID;
		goGirlPkt.reqgetbroadcastlist77 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGetBroadcastList = getUserTopicList;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetBroadcastList) {
			RspGetBroadcastList77 rsp = pkt.rspgetbroadcastlist77;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			//			int total = rsp.total.intValue();
			BroadcastInfoList list = rsp.broadcastlist;
			mGetBroadcastList.getBroadcastList(retCode, isEnd, type, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGetBroadcastList) {
			mGetBroadcastList.getBroadcastList(resultCode, -1, type, null);
		}
	}

	public interface IgetBroadcastList {
		public void getBroadcastList(int retCode, int isEnd, int type, BroadcastInfoList list);
	}

}
