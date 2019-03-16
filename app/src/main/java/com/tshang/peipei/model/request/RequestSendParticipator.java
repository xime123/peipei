package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSndAwardV2;
import com.tshang.peipei.protocol.asn.gogirl.RspSndAwardV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSendParticipator.java 
 *
 * @Description: 获取悬赏提示语
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestSendParticipator extends AsnBase implements ISocketMsgCallBack {

	private GetSendRewardParticipatorCallBack callBack;

	public void requestSendRewardParticipator(byte[] auth, int ver, int uid, int type, int awarduid, int otheruid, int awardid, int awardType,int anonym,
			GetSendRewardParticipatorCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqSndAwardV2 req = new ReqSndAwardV2();
		req.type = BigInteger.valueOf(type);
		req.awarduid = BigInteger.valueOf(awarduid);
		req.awardid = BigInteger.valueOf(awardid);
		req.otheruid = BigInteger.valueOf(otheruid);
		req.awardtype = BigInteger.valueOf(awardType);
		req.isanonymous=BigInteger.valueOf(anonym);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSNDAWARDV2_CID;
		goGirlPkt.reqsndawardv2 = req;
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
			RspSndAwardV2 rsp = pkt.rspsndawardv2;
			int retCode = rsp.retcode.intValue();
			callBack.onSendParticipatorSuccess(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onSendParticipatorError(resultCode);
	}

	public interface GetSendRewardParticipatorCallBack {
		public void onSendParticipatorSuccess(int code);

		public void onSendParticipatorError(int code);
	}

}
