package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.base.ILog;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqTruthAnswer;
import com.tshang.peipei.protocol.asn.gogirl.RspTruthAnswer;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestTruth.java 
 *
 * @Description: 真心话回答请求
 *
 * @author Aaron  
 *
 * @date 2015-6-17 上午10:41:27 
 *
 * @version V1.0   
 */
public class RequestTruthAnswer extends AsnBase implements ISocketMsgCallBack {

	private final String TAG = "Aaron";

	private IAnswerCallBack callBack;

	private String truthid;

	/**
	 * 发送大冒险答案
	 * @param auth
	 * @param version
	 * @param fromuid
	 * @param touid
	 * @param answerid
	 * @param truthid
	 * @param callBack
	 */
	public void requestAnswer(byte[] auth, int version, int fromuid, int touid, int answerid, String truthid, IAnswerCallBack callBack) {
		if (callBack != null) {
			this.callBack = callBack;
		}
		this.truthid = truthid;
		YdmxMsg ydmxMsg = super.createYdmx(auth, version);
		ReqTruthAnswer answer = new ReqTruthAnswer();
		answer.touid = BigInteger.valueOf(touid);
		answer.answerid = BigInteger.valueOf(answerid);
		answer.fromuid = BigInteger.valueOf(fromuid);
		answer.truthid = truthid.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQTRUTHANSWER_CID;
		goGirlPkt.reqtruthanswer = answer;
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
			RspTruthAnswer truth = pkt.rsptruthanswer;
			int retCode = truth.retcode.intValue();
			ILog.d(TAG, "succuess=" + retCode);
			if (checkRetCode(retCode)) {
				callBack.resultAnswer(retCode, truthid);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != null) {
			callBack.resultAnswer(resultCode, truthid);
		}
	}

	public interface IAnswerCallBack {
		public void resultAnswer(int code, String truthid);
	}
}
