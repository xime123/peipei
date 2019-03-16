package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUserListByUidV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUserListByUidV2;
import com.tshang.peipei.protocol.asn.gogirl.UserAndSkillInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetOnlineFreshUserList.java 
 *
 * @Description: 大厅新人在线 
 *
 * @author allen  
 *
 * @date 2014-9-20 上午11:58:03 
 *
 * @version V1.0   
 */
public class RequestGetOnlineFreshUserList extends AsnBase implements ISocketMsgCallBack {

	public iGetOnlineFresh getFresh;

	public void getOnlineFreshUserList(byte[] auth, int ver, int uid, int sex, int startuid, int num, iGetOnlineFresh callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetUserListByUidV2 req = new ReqGetUserListByUidV2();
		req.selfuid = BigInteger.valueOf(uid);
		req.sex = BigInteger.valueOf(sex);
		req.num = BigInteger.valueOf(num);
		req.startuid = BigInteger.valueOf(startuid);
		req.step = BigInteger.valueOf(-1);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUSERLISTBYUIDV2_CID;
		goGirlPkt.reqgetuserlistbyuidv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getFresh = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != getFresh) {
			RspGetUserListByUidV2 rsp = pkt.rspgetuserlistbyuidv2;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getFresh.getFreshList(retCode, rsp.isend.intValue(), rsp.userandskilllist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getFresh) {
			getFresh.getFreshList(resultCode, 0, null);
		}
	}

	public interface iGetOnlineFresh {
		public void getFreshList(int retCode, int isend, UserAndSkillInfoList userlist);
	}
}
