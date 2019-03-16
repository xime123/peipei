package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDeliverGiftV2;
import com.tshang.peipei.protocol.asn.gogirl.RspDeliverGiftV2;
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
public class RequestDeliverGift extends AsnBase implements ISocketMsgCallBack {

	private IDeliverGiftCallBack callBack;

	public void getDeliverGift(byte[] auth, int ver, int uid, int id, int fuid, int num, int isanonymous, IDeliverGiftCallBack callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqDeliverGiftV2 req = new ReqDeliverGiftV2();
		req.fromuid = BigInteger.valueOf(uid);
		req.giftid = BigInteger.valueOf(id);
		req.touid = BigInteger.valueOf(fuid);
		req.giftnum = BigInteger.valueOf(num);
		req.isanonymous = BigInteger.valueOf(isanonymous);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDELIVERGIFTV2_CID;
		goGirlPkt.reqdelivergiftv2 = req;
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

	public interface IDeliverGiftCallBack {
		public void getDeliverGiftCallBack(int retCode);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspDeliverGiftV2 rspDeliverGift = pkt.rspdelivergiftv2;
			if (checkRetCode(rspDeliverGift.retcode.intValue()))
				callBack.getDeliverGiftCallBack(rspDeliverGift.retcode.intValue());
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.getDeliverGiftCallBack(resultCode);
		}
	}

}
