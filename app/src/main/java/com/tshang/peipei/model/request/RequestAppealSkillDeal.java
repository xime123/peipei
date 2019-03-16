package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqAppealSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.RspAppealSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAppealSkillDeal.java 
 *
 * @Description: 申诉
 *
 * @author allen  
 *
 * @date 2014-10-27 上午10:16:20 
 *
 * @version V1.0   
 */
public class RequestAppealSkillDeal extends AsnBase implements ISocketMsgCallBack {

	private iAppealSkillDeal callback;

	public void appealSkillDeal(byte[] auth, int ver, int uid, int skilldealid, iAppealSkillDeal callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqAppealSkillDeal req = new ReqAppealSkillDeal();
		req.selfuid = BigInteger.valueOf(uid);
		req.skilldealid = BigInteger.valueOf(skilldealid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQAPPEALSKILLDEAL_CID;
		goGirlPkt.reqappealskilldeal = req;
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
			RspAppealSkillDeal rsp = pkt.rspappealskilldeal;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultAppealSkillDeal(retCode, rsp.skilldeal);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultAppealSkillDeal(resultCode, null);
		}

	}

	public interface iAppealSkillDeal {
		public void resultAppealSkillDeal(int retCode, SkillDealInfo skilldeal);
	}

}
