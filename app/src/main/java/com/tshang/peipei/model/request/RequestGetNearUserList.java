package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetNearbyUserListV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetNearbyUserListV2;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetOnLineUserList.java 
 *
 * @Description: 附近用户
 *
 * @author Jeff
 *
 * @date 2014-10-20 下午20:40:11 
 *
 * @version V1.4.0   
 */
public class RequestGetNearUserList extends AsnBase implements ISocketMsgCallBack {

	IGetNearUserList getOnlineUserList;

	public void getUserNearList(byte[] auth, int ver, int uid, int sex, int start, int num, int dis, int la, int lo, IGetNearUserList callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetNearbyUserListV2 req = new ReqGetNearbyUserListV2();
		req.num = BigInteger.valueOf(num);
		req.sex = BigInteger.valueOf(sex);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);
		req.dis = BigInteger.valueOf(dis);
		req.la = BigInteger.valueOf(la);
		req.lo = BigInteger.valueOf(lo);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETNEARBYUSERLISTV2_CID;
		goGirlPkt.reqgetnearbyuserlistv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getOnlineUserList = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	private int nextdis;
	private int nextstart;

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getOnlineUserList) {
			RspGetNearbyUserListV2 rsp = pkt.rspgetnearbyuserlistv2;
			int retCode = rsp.retcode.intValue();
			int nextdis = rsp.nextdis.intValue();
			int nextstart = rsp.nextstart.intValue();
			this.nextdis = nextdis;
			this.nextstart = nextstart;
			//						int isEnd = rsp.isend.intValue();
			UserAndSkillInfoList list = rsp.userandskilllist;
			getOnlineUserList.getNearUserListCallBack(retCode, -1, nextstart, nextdis, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getOnlineUserList) {
			getOnlineUserList.getNearUserListCallBack(resultCode, -1, nextstart, nextdis, null);
		}
	}

	public interface IGetNearUserList {
		public void getNearUserListCallBack(int retCode, int isEnd, int nextstart, int nextdis, UserAndSkillInfoList list);
	}

}
