package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AwardTextInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAwardInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAwardInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardSkillInfo.java 
 *
 * @Description: 获取悬赏技能列表
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardSkillInfo extends AsnBase implements ISocketMsgCallBack {

	private GetRewardSkillInfoCallBack callBack;

	public void requestRewardSkillInfo(byte[] auth, int ver, int uid, int sex, GetRewardSkillInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAwardInfo req = new ReqGetAwardInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(sex);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAWARDINFO_CID;
		goGirlPkt.reqgetawardinfo = req;
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
			RspGetAwardInfo rsp = pkt.rspgetawardinfo;
			int retCode = rsp.retcode.intValue();
			AwardTextInfoList info = rsp.awardTextInfoList;
			callBack.onSkillSuccess(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onSillError(resultCode);
	}

	public interface GetRewardSkillInfoCallBack {
		public void onSkillSuccess(int code, Object obj);

		public void onSillError(int code);
	}

}
