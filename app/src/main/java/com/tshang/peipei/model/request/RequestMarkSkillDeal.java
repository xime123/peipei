package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqMarkSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.RspMarkSkillDeal;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestMarkSkillDeal.java 
 *
 * @Description: 订单点评
 *
 * @author allen  
 *
 * @date 2014-10-27 下午3:19:34 
 *
 * @version V1.0   
 */
public class RequestMarkSkillDeal extends AsnBase implements ISocketMsgCallBack {

	private IMarkSkillDeal callback;

	public void markSkillDeal(byte[] auth, int ver, int uid, int skilldealid, int point, IMarkSkillDeal callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqMarkSkillDeal req = new ReqMarkSkillDeal();
		req.point = BigInteger.valueOf(point);
		req.selfuid = BigInteger.valueOf(uid);
		req.skilldealid = BigInteger.valueOf(skilldealid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQMARKSKILLDEAL_CID;
		goGirlPkt.reqmarkskilldeal = req;
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
			RspMarkSkillDeal rsp = pkt.rspmarkskilldeal;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultMarkSkillDeal(retCode, rsp.skilldeal);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultMarkSkillDeal(resultCode, null);
		}

	}

	public interface IMarkSkillDeal {
		public void resultMarkSkillDeal(int retCode, SkillDealInfo skilldeal);
	}

}
