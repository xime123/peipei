package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqReportAppInfoV2;
import com.tshang.peipei.protocol.asn.gogirl.RspReportAppInfoV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestReportAppInfo.java 
 *
 * @Description: 上报信息
 *
 * @author allen  
 *
 * @date 2014-4-30 上午10:43:24 
 *
 * @version V1.0   
 */
public class RequestReportAppInfo extends AsnBase implements ISocketMsgCallBack {

	IReportAppInfo iReport;

	@SuppressWarnings("deprecation")
	public void reportInfo(byte[] auth, int ver, int uid, String token, IReportAppInfo callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqReportAppInfoV2 req = new ReqReportAppInfoV2();
		req.uid = BigInteger.valueOf(uid);
		req.appver = BigInteger.valueOf(ver);
		req.phoneos = BAConstants.RECHARGE_OS_ANDROID;
		req.phonebrand = (android.os.Build.BRAND + " " + android.os.Build.MODEL).getBytes();
		req.osver = android.os.Build.VERSION.SDK.getBytes();
		req.token = token.getBytes();
		req.channel = BAApplication.Channel.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQREPORTAPPINFOV2_CID;
		goGirlPkt.reqreportappinfov2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iReport = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	public interface IReportAppInfo {
		public void reportAppInfo(int retCode);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iReport) {
			RspReportAppInfoV2 rsp = pkt.rspreportappinfov2;
			int retCode = rsp.retcode.intValue();
			iReport.reportAppInfo(retCode);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iReport) {
			iReport.reportAppInfo(resultCode);
		}

	}

}
