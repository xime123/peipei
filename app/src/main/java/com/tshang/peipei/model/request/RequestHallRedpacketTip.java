package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetBroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetBroadcastRedPacketInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestHallRedpacketTip.java 
 *
 * @Description: 请求大厅红包提示 
 *
 * @author Administrator  
 *
 * @date 2016-1-18 下午6:25:41 
 *
 * @version V1.0   
 */
public class RequestHallRedpacketTip extends AsnBase implements ISocketMsgCallBack {

	private GetHallRedpacketTipCallBack callBack;
	private int type;
	public void requestHallRedpacketTip(byte[] auth, int ver, int uid, int type, GetHallRedpacketTipCallBack callBack) {
		this.type = type;
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetBroadcastRedPacketInfo req = new ReqGetBroadcastRedPacketInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETBROADCASTREDPACKETINFO_CID;
		goGirlPkt.reqgetbroadcastredpacketinfo = req;
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
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetBroadcastRedPacketInfo rsp = pkt.rspgetbroadcastredpacketinfo;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			String tip = new String(rsp.tip);
			if(type == 0){
				callBack.getHallRedpacketTipOnSuccess(retCode, retMsg, tip);
			}else{
				callBack.getHallRedpacketHelpOnSuccess(retCode, retMsg, tip);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getHallRedpacketTipOnError(resultCode);
	}
	
	public interface GetHallRedpacketTipCallBack {
		public void getHallRedpacketTipOnSuccess(int code, String retMsg, Object obj);
		public void getHallRedpacketHelpOnSuccess(int code, String retMsg, Object obj);
		public void getHallRedpacketTipOnError(int code);
	}

}
