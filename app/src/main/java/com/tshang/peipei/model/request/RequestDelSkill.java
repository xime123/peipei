package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDelSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspDelSkill;
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
public class RequestDelSkill extends AsnBase implements ISocketMsgCallBack {

	IDelSKill iDelSKill;

	@SuppressWarnings("unchecked")
	public void delSkill(byte[] auth, int ver, int skillid, int uid, IDelSKill callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDelSkill req = new ReqDelSkill();
		req.skillid = BigInteger.valueOf(skillid);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELSKILL_CID;
		goGirlPkt.reqdelskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iDelSKill = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iDelSKill) {
			RspDelSkill rsp = pkt.rspdelskill;
			String retMst = new String(rsp.retmsg);
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode)) {
				iDelSKill.delSkillCallBack(retCode, retMst);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iDelSKill) {
			iDelSKill.delSkillCallBack(resultCode, "删除失败");
		}

	}

	public interface IDelSKill {
		public void delSkillCallBack(int retCode, String msg);
	}

}
