package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqReclainGift;
import com.tshang.peipei.protocol.asn.gogirl.RspReclainGift;
import com.tshang.peipei.protocol.asn.gogirl.SkillDealInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestReclainGift.java 
 *
 * @Description: 退礼物 
 *
 * @author allen  
 *
 * @date 2014-10-24 下午2:38:29 
 *
 * @version V1.0   
 */
public class RequestReclainGift extends AsnBase implements ISocketMsgCallBack {
	private iReclainGift callback;

	public void reclainGift(byte[] auth, int ver, int uid, int skillid, iReclainGift callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqReclainGift req = new ReqReclainGift();
		req.selfuid = BigInteger.valueOf(uid);
		req.skilldealid = BigInteger.valueOf(skillid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQRECLAINGIFT_CID;
		goGirlPkt.reqreclaingift = req;
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
			RspReclainGift rsp = pkt.rspreclaingift;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultReclainGift(retCode, rsp.skilldeal);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultReclainGift(resultCode, null);
		}
	}

	public interface iReclainGift {
		public void resultReclainGift(int retCode, SkillDealInfo info);
	}

}
