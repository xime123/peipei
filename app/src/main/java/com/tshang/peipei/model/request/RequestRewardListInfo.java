package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AwardDetailList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAwardDetailV3;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAwardDetailV3;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardListInfo.java 
 *
 * @Description: 获取悬赏提示语
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardListInfo extends AsnBase implements ISocketMsgCallBack {

	private GetRewardListInfoCallBack callBack;

	public void requestRewardListInfo(byte[] auth, int ver, int uid, int anonymNickId,int type, int start, int num, GetRewardListInfoCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAwardDetailV3 req = new ReqGetAwardDetailV3();
		req.uid = BigInteger.valueOf(uid);
		req.nickid=BigInteger.valueOf(anonymNickId);
		req.type = BigInteger.valueOf(type);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAWARDDETAILV3_CID;
		goGirlPkt.reqgetawarddetailv3 = req;
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
			RspGetAwardDetailV3 rsp = pkt.rspgetawarddetailv3;
			int retCode = rsp.retcode.intValue();
			int isend = rsp.isend.intValue();
			AwardDetailList info = rsp.awardDetailList;
			callBack.onGetRewardListInfoSuccess(retCode, isend, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGetRewardListInfoError(resultCode);
	}

	public interface GetRewardListInfoCallBack {
		public void onGetRewardListInfoSuccess(int code, int isend, Object obj);

		public void onGetRewardListInfoError(int code);
	}

}
