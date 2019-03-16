package com.tshang.peipei.model.request;

import java.math.BigInteger;

import android.util.Log;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRedPacketInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRedPacketInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSolitaireRedPacketMoney.java 
 *
 * @Description: 请求可以发送的红包接龙信息
 *
 * @author DYH  
 *
 * @date 2015-12-8 下午7:19:24 
 *
 * @version V1.0   
 */
public class RequestSolitaireRedPacketMoney extends AsnBase implements ISocketMsgCallBack {
	private GetSolitaireRedPacketCallBack callBack;

	public void requestSolitaireRedPacketMoney(byte[] auth, int ver, int uid, int type, GetSolitaireRedPacketCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRedPacketInfo req = new ReqGetRedPacketInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETREDPACKETINFO_CID;
		goGirlPkt.reqgetredpacketinfo = req;
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
		Log.d("Aaron","ReqGetRedPacketInfo onsuccess");
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetRedPacketInfo rsp = pkt.rspgetredpacketinfo;
			int retCode = rsp.retcode.intValue();
			RedPacketBetInfoList obj = rsp.redPacketBetInfoList;
			int isOpen = rsp.isopenredpacket.intValue();
			callBack.onSolitaireRedPacketMoneySuccess(retCode, isOpen, obj);
		}
	}

	@Override
	public void error(int resultCode) {
		Log.d("Aaron","ReqGetRedPacketInfo error");
		callBack.onSolitaireRedPacketMoneyError(resultCode);
	}

	public interface GetSolitaireRedPacketCallBack {
		public void onSolitaireRedPacketMoneySuccess(int code, int isOpen, Object obj);

		public void onSolitaireRedPacketMoneyError(int code);
	}

}
