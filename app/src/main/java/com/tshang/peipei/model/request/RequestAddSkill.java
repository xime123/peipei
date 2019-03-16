package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspAddSkill;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * 添加技能
 * @author Jeff
 *
 */
public class RequestAddSkill extends AsnBase implements ISocketMsgCallBack {

	IAddSkill iAddSkill;

	@SuppressWarnings("unchecked")
	public void addSkill(byte[] auth, int ver, String desc, int giftId, int giftNum, int uid, String title, IAddSkill callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddSkill req = new ReqAddSkill();
		req.desc = desc.getBytes();
		req.giftid = BigInteger.valueOf(giftId);
		req.giftnum = BigInteger.valueOf(giftNum);
		req.title = title.getBytes();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDSKILL_CID;
		goGirlPkt.reqaddskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iAddSkill = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		RspAddSkill rsp = pkt.rspaddskill;
		int retCode = rsp.retcode.intValue();
		int skillId = rsp.skillid.intValue();
		String retmsg = new String(rsp.retmsg);
		if (checkRetCode(retCode)) {
			iAddSkill.addSkillCallBack(retCode, skillId, retmsg);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iAddSkill) {
			iAddSkill.addSkillCallBack(resultCode, -1, "");
		}

	}

	public interface IAddSkill {
		public void addSkillCallBack(int retCode, int skillId, String msg);
	}

}
