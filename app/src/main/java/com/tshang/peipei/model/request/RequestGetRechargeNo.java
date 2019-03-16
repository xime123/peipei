package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRechargeNoV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRechargeNoV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRechargeNo.java 
 *
 * @Description: 获取订单编解码
 *
 * @author allen  
 *
 * @date 2014-4-19 上午10:55:06 
 *
 * @version V1.0   
 */
public class RequestGetRechargeNo extends AsnBase implements ISocketMsgCallBack {

	private IGetRechargeNoCallBack callBack;

	public void getRechargeNo(byte[] auth, int ver, int uid, BigInteger rechargeid, int anonymity, IGetRechargeNoCallBack callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetRechargeNoV2 req = new ReqGetRechargeNoV2();
		req.uid = BigInteger.valueOf(uid);
		req.fromos = BAConstants.RECHARGE_OS_ANDROID;
		req.rechargeid = rechargeid;
		req.isanonymous = BigInteger.valueOf(anonymity);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRECHARGENOV2_CID;
		goGirlPkt.reqgetrechargenov2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IGetRechargeNoCallBack {
		public void getRechargeNoCallBack(int retCode, RspGetRechargeNoV2 data);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetRechargeNoV2 rspGetRechargeNo = pkt.rspgetrechargenov2;
			if (checkRetCode(rspGetRechargeNo.retcode.intValue()))
				callBack.getRechargeNoCallBack(rspGetRechargeNo.retcode.intValue(), rspGetRechargeNo);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.getRechargeNoCallBack(resultCode, null);
		}
	}

}
