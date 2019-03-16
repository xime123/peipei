package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqCreateRedPacketBet;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateRedPacketBet;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateRedPacketBetV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSendSolitaireRedpacket.java 
 *
 * @Description: 发送后宫红包接龙
 *
 * @author DYH  
 *
 * @date 2015-12-9 下午4:45:24 
 *
 * @version V1.0   
 */
public class RequestSendSolitaireRedpacket extends AsnBase implements ISocketMsgCallBack {

	private GetSendSolitaireRedpacketCallBack callBack;

	public void requestSendSolitaireRedpacket(byte[] auth, int ver, int uid, int redPacketId, int togroupid, int type,
			GetSendSolitaireRedpacketCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqCreateRedPacketBet req = new ReqCreateRedPacketBet();
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);
		req.redPacketId = BigInteger.valueOf(redPacketId);
		req.togroupid = BigInteger.valueOf(togroupid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCREATEREDPACKETBET_CID;
		goGirlPkt.reqcreateredpacketbet = req;
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
			if(pkt.choiceId == GoGirlPkt.RSPCREATEREDPACKETBETV2_CID){
				RspCreateRedPacketBetV2 rsp = pkt.rspcreateredpacketbetv2;
				int retCode = rsp.retcode.intValue();
				String errMsg = new String(rsp.retmsg);
				BroadcastInfo obj = rsp.broadcastInfo;
				callBack.onSendHallSolitaireRedpacketSuccess(retCode, obj, errMsg);
			}else{
				RspCreateRedPacketBet rsp = pkt.rspcreateredpacketbet;
				int retCode = rsp.retcode.intValue();
				RedPacketBetCreateInfo obj = rsp.redPacketBetCreateInfo;
				callBack.onSendSolitaireRedpacketSuccess(retCode, obj);
			}
			
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onSendSolitaireRedpacketError(resultCode);
	}

	public interface GetSendSolitaireRedpacketCallBack {
		public void onSendSolitaireRedpacketSuccess(int code, Object obj);
		public void onSendHallSolitaireRedpacketSuccess(int code, Object obj, String errMsg);
		public void onSendSolitaireRedpacketError(int code);
	}

}
