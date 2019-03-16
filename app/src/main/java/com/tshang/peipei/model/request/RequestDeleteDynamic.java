package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.DynamicsInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDeleteDynamics;
import com.tshang.peipei.protocol.asn.gogirl.RspDeleteDynamics;
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
public class RequestDeleteDynamic extends AsnBase implements ISocketMsgCallBack {

	private DeleteDynamicCallBack callBack;

	public void deleteDynamic(byte[] auth, int ver, int uid, int topicuid, int topicid, int systemtopicid,int type, DeleteDynamicCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqDeleteDynamics req = new ReqDeleteDynamics();
		req.uid = BigInteger.valueOf(uid);
		req.topicid = BigInteger.valueOf(topicid);
		req.topicuid = BigInteger.valueOf(topicuid);
		req.type = BigInteger.valueOf(type);
		req.systemtopicid=BigInteger.valueOf(systemtopicid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELETEDYNAMICS_CID;
		goGirlPkt.reqdeletedynamics = req;
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
			RspDeleteDynamics rsp = pkt.rspdeletedynamics;
			int retCode = rsp.retcode.intValue();
			callBack.onSuccessDelete(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onErrorDelete(resultCode);
	}

	public interface DeleteDynamicCallBack {
		public void onSuccessDelete(int code);

		public void onErrorDelete(int code);
	}

}
