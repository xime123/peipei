package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRedPacketStatus;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRedPacketStatus;
import com.tshang.peipei.protocol.asn.gogirl.RspUnpackRedPacketV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestCheckHallRedpacketStatus.java 
 *
 * @Description: 检查大厅红包状态 
 *
 * @author DYH  
 *
 * @date 2016-1-19 下午1:54:09 
 *
 * @version V1.0   
 */
public class RequestCheckHallRedpacketStatus extends AsnBase implements ISocketMsgCallBack {

	private CheckHallRedpacketStatusCallBack callBack;
	
	public void requestCheckHallRedpacketStatus(byte[] auth, int ver, int uid, int redpacketid, int type, CheckHallRedpacketStatusCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRedPacketStatus req = new ReqGetRedPacketStatus();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.redPacketId = BigInteger.valueOf(redpacketid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETREDPACKETSTATUS_CID;
		goGirlPkt.reqgetredpacketstatus = req;
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
			RspGetRedPacketStatus rsp = pkt.rspgetredpacketstatus;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			BroadcastInfo obj = rsp.broadcastInfo;
			callBack.checkHallRedpacketStatusOnSuccess(retCode, retMsg, obj);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.checkHallRedpacketStatusOnError(resultCode);
	}
	
	public interface CheckHallRedpacketStatusCallBack {
		public void checkHallRedpacketStatusOnSuccess(int code, String retMsg, Object obj);
		public void checkHallRedpacketStatusOnError(int code);
	}

}
