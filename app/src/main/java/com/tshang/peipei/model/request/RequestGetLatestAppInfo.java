package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetLatestAppInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetLatestAppInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetLatestAppInfo.java 
 *
 * @Description: 检测升级 
 *
 * @author allen  
 *
 * @date 2014-4-29 上午11:49:54 
 *
 * @version V1.0   
 */
public class RequestGetLatestAppInfo extends AsnBase implements ISocketMsgCallBack {

	private ILatestAppInfo appInfo;

	public void getAppInfo(byte[] auth, int ver, int uid, ILatestAppInfo appinfo) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqGetLatestAppInfo req = new ReqGetLatestAppInfo();
		req.uid = BigInteger.valueOf(uid);
		req.curappver = BigInteger.valueOf(ver);
		req.phoneos = BAConstants.RECHARGE_OS_ANDROID;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETLATESTAPPINFO_CID;
		goGirlPkt.reqgetlatestappinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.appInfo = appinfo;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface ILatestAppInfo {
		public void getLatestAppInfo(int retCode, RspGetLatestAppInfo rsp);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != appInfo) {
			RspGetLatestAppInfo rsp = pkt.rspgetlatestappinfo;
			int retCode = rsp.retcode.intValue();
			appInfo.getLatestAppInfo(retCode, rsp);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != appInfo) {
			appInfo.getLatestAppInfo(resultCode, null);
		}

	}

}
