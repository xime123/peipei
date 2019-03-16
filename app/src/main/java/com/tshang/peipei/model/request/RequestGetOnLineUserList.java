package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetOnlineUserListV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetOnlineUserListV2;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetOnLineUserList.java 
 *
 * @Description: 最新在线
 *
 * @author vactor
 *
 * @date 2014-5-13 上午11:40:11 
 *
 * @version V1.0   
 */
public class RequestGetOnLineUserList extends AsnBase implements ISocketMsgCallBack {

	IGetOnlineUserList getOnlineUserList;

	public void getUserOnLineList(byte[] auth, int ver, int uid, int sex, int start, int num, int dis, int la, int lo, IGetOnlineUserList callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetOnlineUserListV2 req = new ReqGetOnlineUserListV2();
		req.num = BigInteger.valueOf(num);
		req.sex = BigInteger.valueOf(sex);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETONLINEUSERLISTV2_CID;
		goGirlPkt.reqgetonlineuserlistv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getOnlineUserList = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getOnlineUserList) {
			RspGetOnlineUserListV2 rsp = pkt.rspgetonlineuserlistv2;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			UserAndSkillInfoList list = rsp.userandskilllist;
			getOnlineUserList.getOnlineUserListCallBack(retCode, isEnd, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getOnlineUserList) {
			getOnlineUserList.getOnlineUserListCallBack(resultCode, -1, null);
		}
	}

	public interface IGetOnlineUserList {
		public void getOnlineUserListCallBack(int retCode, int isEnd, UserAndSkillInfoList list);
	}

}
