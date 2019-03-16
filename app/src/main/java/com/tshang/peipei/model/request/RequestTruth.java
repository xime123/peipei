package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.util.Log;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPlayTruth;
import com.tshang.peipei.protocol.asn.gogirl.RspPlayTruth;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestTruth.java 
 *
 * @Description: 发起真心话 
 *
 * @author Aaron  
 *
 * @date 2015-6-17 上午10:41:27 
 *
 * @version V1.0   
 */
public class RequestTruth extends AsnBase implements ISocketMsgCallBack {

	private final String TAG = "Aaron";

	private ITruthCallBack callBack;

	/**
	 * 
	 * @param auth
	 * @param version
	 * @param fromuid
	 * @param touid
	 * @param answerid
	 * @param truthid
	 * @param callBack
	 */
	public void requestTruth(byte[] auth, int version, int fromuid, int touid, int answerid, String truthid, ITruthCallBack callBack) {
		if (callBack != null) {
			this.callBack = callBack;
		}
		Log.w("Aaron", "fromId==" + fromuid + ", touid==" + touid);
		YdmxMsg ydmxMsg = super.createYdmx(auth, version);
		ReqPlayTruth truth = new ReqPlayTruth();
		truth.touid = BigInteger.valueOf(touid);
		truth.answerid = BigInteger.valueOf(answerid);
		truth.fromuid = BigInteger.valueOf(fromuid);
		truth.truthid = truthid.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPLAYTRUTH_CID;
		goGirlPkt.reqplaytruth = truth;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (pkt != null) {
			RspPlayTruth truth = pkt.rspplaytruth;
			int retCode = truth.retcode.intValue();
			Log.d(TAG, "succuess=" + retCode);
			if (checkRetCode(retCode)) {
				callBack.resultTruth(retCode);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != null) {
			callBack.resultTruth(resultCode);
		}
	}

	public interface ITruthCallBack {
		public void resultTruth(int code);
	}
}
