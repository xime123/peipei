package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPrivateChat;
import com.tshang.peipei.protocol.asn.gogirl.ReqPrivateChatV2;
import com.tshang.peipei.protocol.asn.gogirl.RspPrivateChat;
import com.tshang.peipei.protocol.asn.gogirl.RspPrivateChatV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestRewardChat.java 
 *
 * @Description: 悬赏私聊
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestRewardChatV2 extends AsnBase implements ISocketMsgCallBack {

	private GetRewardChatCallBackV2 callBack;

	public void requestRewardChatV2(byte[] auth, int ver, int uid, int type, int awarduid, int selfuid, int awardid,int anonym, GetRewardChatCallBackV2 callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqPrivateChatV2 req = new ReqPrivateChatV2();
		req.type = BigInteger.valueOf(type);
		req.awarduid = BigInteger.valueOf(awarduid);
		req.selfuid = BigInteger.valueOf(selfuid);
		req.awardid = BigInteger.valueOf(awardid);
		req.isanonymous=BigInteger.valueOf(anonym);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPRIVATECHATV2_CID;
		goGirlPkt.reqprivatechatv2 = req;
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
			RspPrivateChatV2 rsp = pkt.rspprivatechatv2;
			int retCode = rsp.retcode.intValue();
			callBack.onChatSuccess(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onChatError(resultCode);
	}

	public interface GetRewardChatCallBackV2 {
		public void onChatSuccess(int code);

		public void onChatError(int code);
	}

}
