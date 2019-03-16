package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqIsSndRedpacket;
import com.tshang.peipei.protocol.asn.gogirl.RspIsSndRedpacket;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestAvailHallRedpacketList.java 
 *
 * @Description: 请求是否可以发红包
 *
 * @author DYH  
 *
 * @date 2016-1-21 下午2:18:07 
 *
 * @version V1.0   
 */
public class RequestIsSendHallRedpacket extends AsnBase implements ISocketMsgCallBack {

	private GetIsSendHallRedpacketCallBack callBack;
	private int type;
	public void requestIsSendHallRedpacket(byte[] auth, int ver, int uid, int type, GetIsSendHallRedpacketCallBack callBack) {
		this.type = type;
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqIsSndRedpacket req = new ReqIsSndRedpacket();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQISSNDREDPACKET_CID;
		goGirlPkt.reqissndredpacket = req;
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
			RspIsSndRedpacket rsp = pkt.rspissndredpacket;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			int obj = rsp.result.intValue();
			if(type == 0){
				callBack.getIsSendHallSolitaireRedpacketOnSuccess(retCode, obj, retMsg);
			}else{
				callBack.getIsSendHallRedpacketOnSuccess(retCode, obj, retMsg);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getIsSendHallRedpacketOnError(resultCode);
	}

	public interface GetIsSendHallRedpacketCallBack {
		public void getIsSendHallRedpacketOnSuccess(int code, int obj, String retMsg);
		public void getIsSendHallSolitaireRedpacketOnSuccess(int code, int obj, String retMsg);
		public void getIsSendHallRedpacketOnError(int code);
	}

}
