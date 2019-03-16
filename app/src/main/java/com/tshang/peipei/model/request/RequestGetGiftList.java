package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GiftInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGiftList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGiftList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequsetGetGiftList.java 
 *
 * @Description: 获取礼物列表接口组包，解包 
 *
 * @author allen  
 *
 * @date 2014-4-12 上午11:07:22 
 *
 * @version V1.0   
 */
public class RequestGetGiftList extends AsnBase implements ISocketMsgCallBack {

	private IGetGiftListCallBack mGiftListCallBack;

	public void getGiftList(byte[] auth, int ver, int uid, IGetGiftListCallBack giftListCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetGiftList req = new ReqGetGiftList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.ZERO;
		req.num = BigInteger.valueOf(50);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGIFTLIST_CID;
		goGirlPkt.reqgetgiftlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGiftListCallBack = giftListCallBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGiftListCallBack) {
			RspGetGiftList rspGetGiftList = pkt.rspgetgiftlist;
			mGiftListCallBack.getGiftListCallBack(rspGetGiftList.retcode.intValue(), rspGetGiftList.giftlist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGiftListCallBack) {
			mGiftListCallBack.getGiftListCallBack(resultCode, null);
		}
	}

	public interface IGetGiftListCallBack {
		public void getGiftListCallBack(int retCode, GiftInfoList giftInfoList);
	}

}
