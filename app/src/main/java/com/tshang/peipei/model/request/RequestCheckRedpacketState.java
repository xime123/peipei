package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRedPacketBetStatus;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRedPacketBetStatus;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRedPacketBetStatusV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestCheckRedpacketState.java 
 *
 * @Description: 检查红包状态
 *
 * @author DYH  
 *
 * @date 2015-12-11 上午11:43:07 
 *
 * @version V1.0   
 */
public class RequestCheckRedpacketState extends AsnBase implements ISocketMsgCallBack {

	private CheckRedpacketStateCallBack callBack;

	public void requestCheckRedpacketState(byte[] auth, int ver, int uid, int redPacketId, int type, CheckRedpacketStateCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRedPacketBetStatus req = new ReqGetRedPacketBetStatus();
		req.uid = BigInteger.valueOf(uid);
		req.redPacketId = BigInteger.valueOf(redPacketId);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETREDPACKETBETSTATUS_CID;
		goGirlPkt.reqgetredpacketbetstatus = req;
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
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			if(pkt.choiceId == GoGirlPkt.RSPGETREDPACKETBETSTATUSV2_CID){
				RspGetRedPacketBetStatusV2 rsp = pkt.rspgetredpacketbetstatusv2;
				int retCode = rsp.retcode.intValue();
				BroadcastInfo obj = rsp.broadcastInfo;
				callBack.checkHallRedpacketStateOnSuccess(retCode, obj);
			}else{
				RspGetRedPacketBetStatus rsp = pkt.rspgetredpacketbetstatus;
				int retCode = rsp.retcode.intValue();
				RedPacketBetCreateInfo obj = rsp.redPacketBetCreateInfo;
				callBack.checkRedpacketStateOnSuccess(retCode, obj);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.checkRedpacketStateOnError(resultCode);
	}

	public interface CheckRedpacketStateCallBack {
		public void checkRedpacketStateOnSuccess(int code, Object obj);
		public void checkHallRedpacketStateOnSuccess(int code, Object obj);
		public void checkRedpacketStateOnError(int code);
	}

}
