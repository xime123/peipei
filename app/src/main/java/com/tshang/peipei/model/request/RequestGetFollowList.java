package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetFollowList;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetFollowList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRelasionship.java 
 *
 * @Description: 关注列表
 *
 * @author vactor
 *
 * @date 2014-5-4 上午10:10:44 
 *
 * @version V1.0   
 */
public class RequestGetFollowList extends AsnBase implements ISocketMsgCallBack {

	IGetFollowList mGetFollowList;

	public void getfollowList(byte[] auth, int ver, int uid, int start, int num, IGetFollowList callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetFollowList req = new ReqGetFollowList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETFOLLOWLIST_CID;
		goGirlPkt.reqgetfollowlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGetFollowList = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetFollowList) {
			RspGetFollowList rsp = pkt.rspgetfollowlist;
			int retCode = rsp.retcode.intValue();
			RetFollowInfoList list = rsp.followlist;
			if (checkRetCode(retCode))
				mGetFollowList.getFollowListCallBack(retCode, list, rsp.isend.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGetFollowList) {
			mGetFollowList.getFollowListCallBack(resultCode, null, 0);
		}
	}

	public interface IGetFollowList {
		public void getFollowListCallBack(int retCode, RetFollowInfoList list, int isend);
	}

}
