package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqParticipate;
import com.tshang.peipei.protocol.asn.gogirl.RspParticipate;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDelFollow.java 
 *
 * @Description: 取消关注
 *
 * @author vactor
 *
 * @date 2014-5-12 下午4:15:29 
 *
 * @version V1.0   
 */
public class RequestParticipate extends AsnBase implements ISocketMsgCallBack {

	IParticipate iParticipate;

	public void participate(byte[] auth, int ver, int uid, int otherUid, int skillId, IParticipate callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqParticipate req = new ReqParticipate();
		req.peeruid = BigInteger.valueOf(otherUid);
		req.skillid = BigInteger.valueOf(skillId);
		req.selfuid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPARTICIPATE_CID;
		goGirlPkt.reqparticipate = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iParticipate = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iParticipate) {
			RspParticipate rsp = pkt.rspparticipate;
			int retCode = rsp.retcode.intValue();
			String retMst = new String(rsp.retmsg);
			if (checkRetCode(retCode))
				iParticipate.participateCallBack(retCode, retMst);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iParticipate) {
			iParticipate.participateCallBack(resultCode, "送礼失败");
		}
	}

	public interface IParticipate {
		public void participateCallBack(int retCode, String retMsg);
	}

}
