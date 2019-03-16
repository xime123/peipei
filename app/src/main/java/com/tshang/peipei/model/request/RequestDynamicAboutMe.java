package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AboutMeReplyInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAboutMeReplyInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetAboutMeReplyInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDynamicAll.java 
 *
 * @Description: 获取关于我动态列表
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestDynamicAboutMe extends AsnBase implements ISocketMsgCallBack {

	private GetDynamicAboutMeCallBack callBack;

	public void requestDynamicAboutMe(byte[] auth, int ver, int uid, int type, int start, int num, GetDynamicAboutMeCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetAboutMeReplyInfo req = new ReqGetAboutMeReplyInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETABOUTMEREPLYINFO_CID;
		goGirlPkt.reqgetaboutmereplyinfo = req;
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
			RspGetAboutMeReplyInfo rsp = pkt.rspgetaboutmereplyinfo;
			int retCode = rsp.retcode.intValue();
			AboutMeReplyInfoList info = rsp.aboutMeReplyInfoList;
			callBack.onSuccess(retCode, info);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onError(resultCode);
	}

	public interface GetDynamicAboutMeCallBack {
		public void onSuccess(int code, Object obj);

		public void onError(int code);
	}

}
