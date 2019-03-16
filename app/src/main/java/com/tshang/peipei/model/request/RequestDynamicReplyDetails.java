package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetCommentReplyInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetCommentReplyInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDynamicAll.java 
 *
 * @Description: 获取回复详情
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestDynamicReplyDetails extends AsnBase implements ISocketMsgCallBack {

	private GetDynamicReplyDetails callBack;

	public void requestDynamicReplyDetails(byte[] auth, int ver, int uid, int topicid, int topicuid,int relativeid, int start, int num, int type,
			GetDynamicReplyDetails callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetCommentReplyInfo req = new ReqGetCommentReplyInfo();
		req.uid = BigInteger.valueOf(uid);
		req.relativeid=BigInteger.valueOf(relativeid);
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		req.type = BigInteger.valueOf(type);
		
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETCOMMENTREPLYINFO_CID;
		goGirlPkt.reqgetcommentreplyinfo = req;
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
			RspGetCommentReplyInfo info = pkt.rspgetcommentreplyinfo;
			int retCode = info.retcode.intValue();
			callBack.onSuccess(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onError(resultCode);
	}

	public interface GetDynamicReplyDetails {
		public void onSuccess(int code, Object obj);

		public void onError(int code);
	}

}
