package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillTipInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqSndSkillInvite;
import com.tshang.peipei.protocol.asn.gogirl.RspPublicationAwardV2;
import com.tshang.peipei.protocol.asn.gogirl.RspSndSkillInvite;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSndSkillInvite.java 
 *
 * @Description: 发送女神技能邀请 
 *
 * @author DYH  
 *
 * @date 2015-11-11 下午4:30:17 
 *
 * @version V1.0   
 */
public class RequestSndSkillInvite extends AsnBase implements ISocketMsgCallBack {
	private SendSkillInviteCallBack callBack;

	public void requestSendSkillInvite(byte[] auth, int ver, int uid, int touid, int skillid, int giftid, String additionalword, SendSkillInviteCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSndSkillInvite req = new ReqSndSkillInvite();
		req.inviteuid = BigInteger.valueOf(uid);
		req.touid = BigInteger.valueOf(touid);
		req.skillid = BigInteger.valueOf(skillid);
		req.giftid = BigInteger.valueOf(giftid);
		req.additionalword = additionalword.getBytes();
		req.type = BigInteger.valueOf(0);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSNDSKILLINVITE_CID;
		goGirlPkt.reqsndskillinvite = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspSndSkillInvite rsp = pkt.rspsndskillinvite;
			int retCode = rsp.retcode.intValue();
			String meeage = new String(rsp.retmsg);
			callBack.sendSkillInviteOnSuccess(retCode, meeage);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.sendSkillInviteOnError(resultCode);
	}

	public interface SendSkillInviteCallBack {
		public void sendSkillInviteOnSuccess(int resCode, Object object);

		public void sendSkillInviteOnError(int resCode);
	}

}
