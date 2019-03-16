package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqMarkSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspMarkSkill;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 技能评分 
 */
public class RequestMarkSkill extends AsnBase implements ISocketMsgCallBack {

	BizCallBackMarkSkill iMarkSkill;

	public void markSkill(byte[] auth, int ver, int uid, int otherUid, int point, int skillId, BizCallBackMarkSkill callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqMarkSkill req = new ReqMarkSkill();
		req.peeruid = BigInteger.valueOf(otherUid);
		req.selfuid = BigInteger.valueOf(uid);
		req.point = BigInteger.valueOf(point);
		req.skillid = BigInteger.valueOf(skillId);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQMARKSKILL_CID;
		goGirlPkt.reqmarkskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iMarkSkill = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iMarkSkill) {
			RspMarkSkill rsp = pkt.rspmarkskill;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iMarkSkill.markSkillCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iMarkSkill) {
			iMarkSkill.markSkillCallBack(resultCode);
		}
	}

	public interface BizCallBackMarkSkill {
		public void markSkillCallBack(int retCode);
	}

}
