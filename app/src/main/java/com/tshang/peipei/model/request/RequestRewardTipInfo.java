package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAwardTipInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAwardTipInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardTipInfo.java 
 *
 * @Description: 获取悬赏提示语
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardTipInfo extends AsnBase implements ISocketMsgCallBack {

	private GetRewardTipInfoCallBack callBack;

	public void requestRewardTipInfo(byte[] auth, int ver, int uid, int sex, GetRewardTipInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAwardTipInfo req = new ReqGetAwardTipInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(sex);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAWARDTIPINFO_CID;
		goGirlPkt.reqgetawardtipinfo = req;
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
			RspGetAwardTipInfo rsp = pkt.rspgetawardtipinfo;
			int retCode = rsp.retcode.intValue();
			GoGirlDataInfoList info = rsp.awardTipInfo;
			callBack.onTipSuccess(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onTipError(resultCode);
	}

	public interface GetRewardTipInfoCallBack {
		public void onTipSuccess(int code, Object obj);

		public void onTipError(int code);
	}

}
