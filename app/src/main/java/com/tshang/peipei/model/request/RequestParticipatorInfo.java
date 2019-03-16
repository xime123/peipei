package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ParticipateInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetParticipateInfoV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetParticipateInfoV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestParticipatorInfo.java 
 *
 * @Description: 获取悬赏提示语
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestParticipatorInfo extends AsnBase implements ISocketMsgCallBack {

	private GetRewardParticipatorCallBack callBack;

	public void requestRewardParticipatorInfo(byte[] auth, int ver, int uid, int type, int awarduid, int awardid, int anonym,
			GetRewardParticipatorCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetParticipateInfoV2 req = new ReqGetParticipateInfoV2();
		req.type = BigInteger.valueOf(type);
		req.awarduid = BigInteger.valueOf(awarduid);
		req.awardid = BigInteger.valueOf(awardid);
		req.isanonymous = BigInteger.valueOf(anonym);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETPARTICIPATEINFOV2_CID;
		goGirlPkt.reqgetparticipateinfov2 = req;
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
			RspGetParticipateInfoV2 rsp = pkt.rspgetparticipateinfov2;
			int retCode = rsp.retcode.intValue();
			ParticipateInfoList info = rsp.participateInfoList;
			int issndaward = rsp.issndaward.intValue();
			callBack.onParticipatorSuccess(retCode, issndaward, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onParticipatorError(resultCode);
	}

	public interface GetRewardParticipatorCallBack {
		public void onParticipatorSuccess(int code, int issndaward, Object obj);

		public void onParticipatorError(int code);
	}

}
