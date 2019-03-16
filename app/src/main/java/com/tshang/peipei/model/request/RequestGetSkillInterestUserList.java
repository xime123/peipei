package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillInterestUserList;
import com.tshang.peipei.protocol.asn.gogirl.RetParticipantInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillInterestUserList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 拉黑 
 *
 * @author allen  
 *
 * @date 2014-8-8 上午10:59:54 
 *
 * @version V1.0   
 */
public class RequestGetSkillInterestUserList extends AsnBase implements ISocketMsgCallBack {

	private IGetSkillInterestUserList addblack;

	public void getSkillInterestUserlist(byte[] auth, int ver, int uid, int skillid, int skilluid, int start, int num,
			IGetSkillInterestUserList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillInterestUserList req = new ReqGetSkillInterestUserList();
		req.selfuid = BigInteger.valueOf(uid);
		req.skillid = BigInteger.valueOf(skillid);
		req.skilluid = BigInteger.valueOf(skilluid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLINTERESTUSERLIST_CID;
		goGirlPkt.reqgetskillinterestuserlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {

			RspGetSkillInterestUserList rsp = pkt.rspgetskillinterestuserlist;
			RetParticipantInfoList list = rsp.participantlist;
			int isend = rsp.isend.intValue();
			int total = rsp.total.intValue();
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.reqGetSkillInterestUserList(retCode, isend, total, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.reqGetSkillInterestUserList(resultCode, 0, 0, null);
		}

	}

	public interface IGetSkillInterestUserList {
		public void reqGetSkillInterestUserList(int retCode, int isend, int total, RetParticipantInfoList list);
	}

}
