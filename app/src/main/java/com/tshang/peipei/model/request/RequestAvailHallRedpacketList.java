package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastRedPacketInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAvailRedpacketList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAvailRedpacketList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAvailRedpacketListV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestAvailHallRedpacketList.java 
 *
 * @Description: 请求有效的红包列表
 *
 * @author DYH  
 *
 * @date 2016-1-21 下午2:18:07 
 *
 * @version V1.0   
 */
public class RequestAvailHallRedpacketList extends AsnBase implements ISocketMsgCallBack {

	private GetAvailHallRedpacketListCallBack callBack;

	public void requestAvailHallRedpacketList(byte[] auth, int ver, int uid, int type, GetAvailHallRedpacketListCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAvailRedpacketList req = new ReqGetAvailRedpacketList();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETAVAILREDPACKETLIST_CID;
		goGirlPkt.reqgetavailredpacketlist = req;
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
			if (pkt.choiceId == GoGirlPkt.RSPGETAVAILREDPACKETLIST_CID) {
				RspGetAvailRedpacketList rsp = pkt.rspgetavailredpacketlist;
				int retCode = rsp.retcode.intValue();
				String retMsg = new String(rsp.retmsg);
				BroadcastRedPacketInfoList obj = rsp.broadcastRedPacketInfoList;
				callBack.getAvailHallRedpacketListOnSuccess(retCode, obj, retMsg);
			}else{
				RspGetAvailRedpacketListV2 rsp = pkt.rspgetavailredpacketlistv2;
				int retCode = rsp.retcode.intValue();
				String retMsg = new String(rsp.retmsg);
				RedPacketBetCreateInfoList obj = rsp.redPacketBetCreateInfoList;
				callBack.getAvailHallSolitaireRedpacketListOnSuccess(retCode, obj, retMsg);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.getAvailHallRedpacketListOnError(resultCode);
	}

	public interface GetAvailHallRedpacketListCallBack {
		public void getAvailHallRedpacketListOnSuccess(int code, Object obj, String retMsg);
		public void getAvailHallSolitaireRedpacketListOnSuccess(int code, Object obj, String retMsg);
		public void getAvailHallRedpacketListOnError(int code);
	}

}
