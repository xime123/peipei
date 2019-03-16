package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqConfirmSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.RspConfirmSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestConfirmSkillDeal.java 
 *
 * @Description: 订单操作
 *
 * @author allen  
 *
 * @date 2014-10-24 上午10:18:10 
 *
 * @version V1.0   
 */
public class RequestConfirmSkillDeal extends AsnBase implements ISocketMsgCallBack {

	private iConfirmSkillDeal callback;

	public void confirmSkillDeal(byte[] auth, int ver, int uid, String act, int skillid, iConfirmSkillDeal callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqConfirmSkillDeal req = new ReqConfirmSkillDeal();
		req.selfuid = BigInteger.valueOf(uid);
		req.act = act.getBytes();
		req.skilldealid = BigInteger.valueOf(skillid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCONFIRMSKILLDEAL_CID;
		goGirlPkt.reqconfirmskilldeal = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspConfirmSkillDeal rsp = pkt.rspconfirmskilldeal;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultConfirmSkillDeal(retCode, rsp.skilldeal);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultConfirmSkillDeal(resultCode, null);
		}
	}

	public interface iConfirmSkillDeal {
		public void resultConfirmSkillDeal(int retCode, SkillDealInfo info);
	}
}
