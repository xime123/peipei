package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSystemDynamicsReplyInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSystemDynamicsReplyInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDynamicAll.java 
 *
 * @Description: 获取动态列表
 *
 * @author Aaron  
 *
 * @date 2015-8-17 下午7:47:03 
 *
 * @version V1.0   
 */
public class RequestDynamicOfficial extends AsnBase implements ISocketMsgCallBack {

	private GetDynamicOfficialCallBack callBack;

	public void requestDynamicAll(byte[] auth, int ver, int uid, int topicid, int start, int num, int type, GetDynamicOfficialCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSystemDynamicsReplyInfo req = new ReqGetSystemDynamicsReplyInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.topicid = BigInteger.valueOf(topicid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSYSTEMDYNAMICSREPLYINFO_CID;
		goGirlPkt.reqgetsystemdynamicsreplyinfo = req;
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
			RspGetSystemDynamicsReplyInfo rsp = pkt.rspgetsystemdynamicsreplyinfo;
			if (rsp != null) {
				int retCode = rsp.retcode.intValue();
				DynamicsInfoList info = rsp.dynamicsinfolist;
				callBack.onSuccess(retCode, info);
			} else {
				callBack.onError(-978);
			}

		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onError(resultCode);
	}

	public interface GetDynamicOfficialCallBack {
		public void onSuccess(int code, Object obj);

		public void onError(int code);
	}

}
