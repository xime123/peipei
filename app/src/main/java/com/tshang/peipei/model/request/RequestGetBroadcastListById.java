package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetBroadcastListById;
import com.tshang.peipei.protocol.asn.gogirl.RspGetBroadcastListById;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.ILog;
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
public class RequestGetBroadcastListById extends AsnBase implements ISocketMsgCallBack {

	private IgetBroadcastListById mGetBroadcastList;
	private int type = -1;
	private boolean isRefresh = true;

	public void getBroadcastList(byte[] auth, int ver, int uid, int type, int startid, int step, int num, boolean isRefresh,
			IgetBroadcastListById getUserTopicList) {
		this.type = type;
		this.isRefresh = isRefresh;
		//头部信息
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		//包体
		ReqGetBroadcastListById req = new ReqGetBroadcastListById();
		req.num = BigInteger.valueOf(num);
		req.startid = BigInteger.valueOf(startid);
		req.type = BigInteger.valueOf(type);
		req.step = BigInteger.valueOf(step);
		req.selfuid = BigInteger.valueOf(uid);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETBROADCASTLISTBYID_CID;
		goGirlPkt.reqgetbroadcastlistbyid = req;
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
		ILog.d("Aaron", "ReqGetBroadcastListById succuess");
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetBroadcastList) {
			RspGetBroadcastListById rsp = pkt.rspgetbroadcastlistbyid;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			//			int total = rsp.total.intValue();
			BroadcastInfoList list = rsp.broadcastlist;
			mGetBroadcastList.getBroadcastList(retCode, isEnd, type, isRefresh, list);
		}
	}

	@Override
	public void error(int resultCode) {
		ILog.d("Aaron", "ReqGetBroadcastListById error");
		if (null != mGetBroadcastList) {
			mGetBroadcastList.getBroadcastList(resultCode, -1, type, isRefresh, null);
		}
	}

	public interface IgetBroadcastListById {
		public void getBroadcastList(int retCode, int isEnd, int type, boolean isRefresh, BroadcastInfoList list);
	}

}
