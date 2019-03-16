package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.util.Log;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqJudgeRedPacketAvail;
import com.tshang.peipei.protocol.asn.gogirl.RspJudgeRedPacketAvail;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestHallRedpacketAvail.java 
 *
 * @Description: 请求是否有有效的红包 
 *
 * @author DYH  
 *
 * @date 2016-1-21 上午11:57:25 
 *
 * @version V1.0   
 */
public class RequestHallRedpacketAvail extends AsnBase implements ISocketMsgCallBack {

	private GetHallRedpacketAvailCallBack callBack;
	private int type;
	public void requestHallRedpacketAvail(byte[] auth, int ver, int uid, int type, GetHallRedpacketAvailCallBack callBack) {
		this.type = type;
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqJudgeRedPacketAvail req = new ReqJudgeRedPacketAvail();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQJUDGEREDPACKETAVAIL_CID;
		goGirlPkt.reqjudgeredpacketavail = req;
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
		Log.d("Aaron", "ReqJudgeRedPacketAvail onsuccuess");
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspJudgeRedPacketAvail rsp = pkt.rspjudgeredpacketavail;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			int obj = rsp.result.intValue();
			if(type == 0){
				callBack.getHallSolitaireRedpacketAvailOnSuccess(retCode, obj, retMsg);
			}else{
				callBack.getHallRedpacketAvailOnSuccess(retCode, obj, retMsg);
			}
			
		}
	}

	@Override
	public void error(int resultCode) {
		Log.d("Aaron", "ReqJudgeRedPacketAvail error");
		callBack.getHallRedpacketAvailOnError(resultCode);
	}
	
	public interface GetHallRedpacketAvailCallBack {
		public void getHallRedpacketAvailOnSuccess(int code, int result, String retMsg);
		public void getHallSolitaireRedpacketAvailOnSuccess(int code, int result, String retMsg);
		public void getHallRedpacketAvailOnError(int code);
	}

}
