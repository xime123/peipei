package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqUnpackRedPacketBet;
import com.tshang.peipei.protocol.asn.gogirl.RspUnpackRedPacketBet;
import com.tshang.peipei.protocol.asn.gogirl.RspUnpackRedPacketBetV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGrapSolitaireRedpacket.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-12-10 下午5:17:09 
 *
 * @version V1.0   
 */
public class RequestGrapSolitaireRedpacket extends AsnBase implements ISocketMsgCallBack {
	private GetGrapSolitaireRedPacketCallBack callBack;

	public void requestGrapSolitaireRedPacket(byte[] auth, int ver, int uid, int redpacketuid, int redpacketid, int type,
			GetGrapSolitaireRedPacketCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqUnpackRedPacketBet req = new ReqUnpackRedPacketBet();
		req.selfuid = BigInteger.valueOf(uid);
		req.redpacketuid = BigInteger.valueOf(redpacketuid);
		req.redpacketid = BigInteger.valueOf(redpacketid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUNPACKREDPACKETBET_CID;
		goGirlPkt.requnpackredpacketbet = req;
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
			if(pkt.choiceId == GoGirlPkt.RSPUNPACKREDPACKETBETV2_CID){//大厅抢接龙红包
				RspUnpackRedPacketBetV2 rsp = pkt.rspunpackredpacketbetv2;
				int retCode = rsp.retcode.intValue();
				BroadcastInfo obj = rsp.broadcastInfo;
				callBack.onGrapHallSolitaireRedpacketSuccee(retCode, obj);
			}else{
				RspUnpackRedPacketBet rsp = pkt.rspunpackredpacketbet;
				int retCode = rsp.retcode.intValue();
				RedPacketBetCreateInfo obj = rsp.redPacketBetCreateInfo;
				callBack.onGrapSolitaireRedPacketSuccess(retCode, obj);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGrapSolitaireRedPacketError(resultCode);
	}

	public interface GetGrapSolitaireRedPacketCallBack {
		public void onGrapSolitaireRedPacketSuccess(int code, Object obj);
		public void onGrapHallSolitaireRedpacketSuccee(int code, Object obj);
		public void onGrapSolitaireRedPacketError(int code);
	}

}
