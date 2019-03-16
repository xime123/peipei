package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqInterestInSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspInterestInSkill;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 感兴趣技能
 * @author Jeff
 *
 */
public class RequestInterestInSkill extends AsnBase implements ISocketMsgCallBack {

	IInterestInSkill iInterestInSkill;

	@SuppressWarnings("unchecked")
	public void interestInSkill(byte[] auth, int ver, String introduce, int selfuid, int skillId, int skilluid, IInterestInSkill callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqInterestInSkill req = new ReqInterestInSkill();
		req.introduce = introduce.getBytes();
		req.selfuid = BigInteger.valueOf(selfuid);
		req.skillid = BigInteger.valueOf(skillId);
		req.skilluid = BigInteger.valueOf(skilluid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQINTERESTINSKILL_CID;
		goGirlPkt.reqinterestinskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iInterestInSkill = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		RspInterestInSkill rsp = pkt.rspinterestinskill;
		int retCode = rsp.retcode.intValue();
		String retmsg = new String(rsp.retmsg);
		if (checkRetCode(retCode)) {
			iInterestInSkill.interestInSkillCallBack(retCode, retmsg);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iInterestInSkill) {
			iInterestInSkill.interestInSkillCallBack(resultCode, "");
		}

	}

	public interface IInterestInSkill {
		public void interestInSkillCallBack(int retCode, String msg);
	}

}
