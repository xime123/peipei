package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqTipoffSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspTipoffSkill;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddReply.java 
 *
 * @Description: 回复
 *
 * @author vactor
 *
 * @date 2014-4-30 下午2:21:11 
 *
 * @version V1.0   
 */
public class RequestTipoffSkill extends AsnBase implements ISocketMsgCallBack {

	ITipoffSkill iDelReplyCallBack;

	@SuppressWarnings("unchecked")
	public void tipoffSkill(byte[] auth, int ver, int actuid, int reasonid, String reason, int skillid, int uid, ITipoffSkill callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqTipoffSkill req = new ReqTipoffSkill();
		req.actuid = BigInteger.valueOf(actuid);
		req.reasonid = BigInteger.valueOf(reasonid);
		req.reasonother = reason.getBytes();
		req.skillid = BigInteger.valueOf(skillid);
		req.skilluid = BigInteger.valueOf(skillid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQTIPOFFSKILL_CID;
		goGirlPkt.reqtipoffskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDelReplyCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iDelReplyCallBack) {
			RspTipoffSkill rsp = pkt.rsptipoffskill;
			String retMst = new String(rsp.retmsg);
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode)) {
				iDelReplyCallBack.delSkillCallBack(retCode, retMst);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDelReplyCallBack) {
			iDelReplyCallBack.delSkillCallBack(resultCode, "举报失败");
		}

	}

	public interface ITipoffSkill {
		public void delSkillCallBack(int retCode, String msg);
	}

}
