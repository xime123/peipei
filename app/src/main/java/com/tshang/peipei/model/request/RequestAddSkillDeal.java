package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAddSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.RspAddSkillDeal;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddSkillDeal.java 
 *
 * @Description: 技能下单
 *
 * @author Jeff  
 *
 * @date 2014-8-8 上午10:59:54 
 *
 * @version V1.4.0   
 */
public class RequestAddSkillDeal extends AsnBase implements ISocketMsgCallBack {

	private IAddSkillDeal addblack;

	public void addSkillDeal(byte[] auth, int ver, int uid, int skillid, int skilluid, int participateuid, IAddSkillDeal callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAddSkillDeal req = new ReqAddSkillDeal();
		req.skillid = BigInteger.valueOf(skillid);
		req.skilluid = BigInteger.valueOf(skilluid);
		req.selfuid = BigInteger.valueOf(uid);
		req.participateuid = BigInteger.valueOf(participateuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQADDSKILLDEAL_CID;
		goGirlPkt.reqaddskilldeal = req;
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
			RspAddSkillDeal rsp = pkt.rspaddskilldeal;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.reqAddSkillDeal(retCode);
		}
	}

	@Override
	public void error(int resultCode) {

		if (null != addblack) {
			addblack.reqAddSkillDeal(resultCode);
		}

	}

	public interface IAddSkillDeal {
		public void reqAddSkillDeal(int retCode);
	}

}
