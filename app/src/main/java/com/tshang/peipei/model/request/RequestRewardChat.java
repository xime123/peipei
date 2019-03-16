package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqPrivateChat;
import com.tshang.peipei.protocol.asn.gogirl.RspPrivateChat;
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
public class RequestRewardChat extends AsnBase implements ISocketMsgCallBack {

	private GetRewardChatCallBack callBack;

	public void requestJoinReward(byte[] auth, int ver, int uid, int type, int awarduid, int selfuid, int awardid, GetRewardChatCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqPrivateChat req = new ReqPrivateChat();
		req.type = BigInteger.valueOf(type);
		req.awarduid = BigInteger.valueOf(awarduid);
		req.selfuid = BigInteger.valueOf(selfuid);
		req.awardid = BigInteger.valueOf(awardid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQPRIVATECHAT_CID;
		goGirlPkt.reqprivatechat = req;
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
			RspPrivateChat rsp = pkt.rspprivatechat;
			int retCode = rsp.retcode.intValue();
			callBack.onChatSuccess(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onChatError(resultCode);
	}

	public interface GetRewardChatCallBack {
		public void onChatSuccess(int code);

		public void onChatError(int code);
	}

}
