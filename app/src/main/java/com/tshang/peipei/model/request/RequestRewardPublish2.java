package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPublicationAwardV2;
import com.tshang.peipei.protocol.asn.gogirl.RspPublicationAward;
import com.tshang.peipei.protocol.asn.gogirl.RspPublicationAwardV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardPublish.java 
 *
 * @Description: 发布悬赏
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardPublish2 extends AsnBase implements ISocketMsgCallBack {

	private publishRewardCallBack callBack;

	public void requestRewardPublish(byte[] auth, int ver, int uid, int type, String skillName, String skillDesc, int awardid, int giftid,
			int isanonymous, publishRewardCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqPublicationAwardV2 req = new ReqPublicationAwardV2();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.awardid = BigInteger.valueOf(awardid);
		req.skillname = skillName.getBytes();
		req.skilldesc = skillDesc.getBytes();
		req.giftid = BigInteger.valueOf(giftid);
		req.isanonymous = BigInteger.valueOf(isanonymous);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPUBLICATIONAWARDV2_CID;
		goGirlPkt.reqpublicationawardv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspPublicationAwardV2 rsp = pkt.rsppublicationawardv2;
			int retCode = rsp.retcode.intValue();
			String meeage = new String(rsp.retmsg);
			callBack.publishOnSuccess(retCode, meeage);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.publishOnError(resultCode);
	}

	public interface publishRewardCallBack {
		public void publishOnSuccess(int code, String msg);

		public void publishOnError(int code);
	}
}
