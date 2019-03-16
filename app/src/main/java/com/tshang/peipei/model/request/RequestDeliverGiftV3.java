package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDeliverGiftV3;
import com.tshang.peipei.protocol.asn.gogirl.RspDeliverGiftV3;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestDeliverGift.java 
 *
 * @Description: 赠送礼物
 *
 * @author allen  
 *
 * @date 2014-4-17 下午10:10:05 
 *
 * @version V1.0   
 */
public class RequestDeliverGiftV3 extends AsnBase implements ISocketMsgCallBack {

	private IDeliverGiftCallBackV3 callBack;

	public void getDeliverGift(byte[] auth, int ver, int uid, int id, int fuid, int num, int isanonymous, IDeliverGiftCallBackV3 callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqDeliverGiftV3 req = new ReqDeliverGiftV3();
		req.fromuid = BigInteger.valueOf(uid);
		req.giftid = BigInteger.valueOf(id);
		req.touid = BigInteger.valueOf(fuid);
		req.giftnum = BigInteger.valueOf(num);
		req.isanonymous = BigInteger.valueOf(isanonymous);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELIVERGIFTV3_CID;
		goGirlPkt.reqdelivergiftv3 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IDeliverGiftCallBackV3 {
		public void getDeliverGiftCallBackV3(int retCode);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspDeliverGiftV3 rspDeliverGift = pkt.rspdelivergiftv3;
			if (checkRetCode(rspDeliverGift.retcode.intValue()))
				callBack.getDeliverGiftCallBackV3(rspDeliverGift.retcode.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.getDeliverGiftCallBackV3(resultCode);
		}
	}
}
