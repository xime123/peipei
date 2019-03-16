package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUserProperty;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUserProperty;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetUserProperty.java 
 *
 * @Description: 获取账号钱数
 *
 * @author allen
 *
 * @date 2014-4-18 上午11:42:02 
 *
 * @version V1.0   
 */
public class RequestGetUserProperty extends AsnBase implements ISocketMsgCallBack {

	private IGetUserPropertyCallBack callback;

	public void getUserProperty(byte[] auth, int ver, int uid, IGetUserPropertyCallBack callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetUserProperty req = new ReqGetUserProperty();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUSERPROPERTY_CID;
		goGirlPkt.reqgetuserproperty = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IGetUserPropertyCallBack {
		public void getGetUserPropertyCallBack(int retCode, UserPropertyInfo userPropertyInfo);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {
			RspGetUserProperty rspDeliverGift = pkt.rspgetuserproperty;
			if (checkRetCode(rspDeliverGift.retcode.intValue()))
				callback.getGetUserPropertyCallBack(rspDeliverGift.retcode.intValue(), rspDeliverGift.userproperty);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getGetUserPropertyCallBack(resultCode, null);
		}
	}

}
