package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RechargeRatioInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRechargeRatioList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRechargeRatioList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRechargeRatioList.java 
 *
 * @Description: 获取充值列表编解码
 *
 * @author allen  
 *
 * @date 2014-4-19 上午9:46:00 
 *
 * @version V1.0   
 */
public class RequestGetRechargeRatioList extends AsnBase implements ISocketMsgCallBack {

	private IGetRechargeRatioListCallBack callBack;

	public void getRechargeRatioList(byte[] auth, int ver, int uid, IGetRechargeRatioListCallBack callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetRechargeRatioList req = new ReqGetRechargeRatioList();
		req.uid = BigInteger.valueOf(uid);
		req.fromos = BAConstants.RECHARGE_OS_ANDROID;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRECHARGERATIOLIST_CID;
		goGirlPkt.reqgetrechargeratiolist = req;
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

	public interface IGetRechargeRatioListCallBack {
		public void getGetRechargeRatioListCallBack(int retCode, RechargeRatioInfoList rechargeRatioInfoList);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetRechargeRatioList rspGetRechargeRatioList = pkt.rspgetrechargeratiolist;
			if (checkRetCode(rspGetRechargeRatioList.retcode.intValue()))
				callBack.getGetRechargeRatioListCallBack(rspGetRechargeRatioList.retcode.intValue(), rspGetRechargeRatioList.rechargeratiolist);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.getGetRechargeRatioListCallBack(resultCode, null);
		}
	}

}
