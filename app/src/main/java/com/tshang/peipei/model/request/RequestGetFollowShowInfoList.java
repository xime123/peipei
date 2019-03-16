package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetFollowShowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowShowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetFollowShowInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetFollowShowInfoList.java 
 *
 * @Description: 男的所关注女神动态列表
 *
 * @author vactor
 *
 * @date 2014-5-14 下午2:17:41 
 *
 * @version V1.0   
 */
public class RequestGetFollowShowInfoList extends AsnBase implements ISocketMsgCallBack {
	IGetFollowShow iGetFollowShow;

	public void getFollowShowInfoList(byte[] auth, int ver, int uid, int start, int num, IGetFollowShow callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetFollowShowInfoList req = new ReqGetFollowShowInfoList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETFOLLOWSHOWINFOLIST_CID;
		goGirlPkt.reqgetfollowshowinfolist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iGetFollowShow = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iGetFollowShow) {
			RspGetFollowShowInfoList rsp = pkt.rspgetfollowshowinfolist;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			RetFollowShowInfoList list = rsp.followshowlist;
			if (checkRetCode(retCode))
				iGetFollowShow.getFollowShowListCallBack(retCode, isEnd, list);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iGetFollowShow) {
			iGetFollowShow.getFollowShowListCallBack(resultCode, -1, null);
		}
	}

	public interface IGetFollowShow {
		public void getFollowShowListCallBack(int retCode, int isEnd, RetFollowShowInfoList list);
	}

}
