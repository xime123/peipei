package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUpdateAlias;
import com.tshang.peipei.protocol.asn.gogirl.RspUpdateAlias;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestUpdateAlias.java 
 *
 * @Description: 修改备注
 *
 * @author allen  
 *
 * @date 2014-12-1 下午8:09:42 
 *
 * @version V1.0   
 */
public class RequestUpdateAlias extends AsnBase implements ISocketMsgCallBack {

	iUpdateAlias callback;
	int followuid;
	String alias;

	public void updateAlias(byte[] auth, int ver, int uid, int followuid,int type, String alias, iUpdateAlias callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqUpdateAlias req = new ReqUpdateAlias();
		req.alias = alias.getBytes();
		req.aliasuid = BigInteger.valueOf(followuid);
		req.type = BigInteger.valueOf(type);
		req.selfuid = BigInteger.valueOf(uid);
		this.followuid = followuid;
		this.alias = alias;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPDATEALIAS_CID;
		goGirlPkt.requpdatealias = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface iUpdateAlias {
		public void resultAlias(int retCode, int followuid, String alias);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {
			RspUpdateAlias rsp = pkt.rspupdatealias;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultAlias(retCode, followuid, alias);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			if (checkRetCode(resultCode))
				callback.resultAlias(resultCode, followuid, alias);
		}
	}
}
