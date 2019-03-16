package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GiftDealInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGiftDealList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGiftDealList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants.USER_ACT;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestShowGiftToday.java 
 *
 * @Description: 展示今天收到的礼物列表接口组包，解包 
 *
 * @author allen  
 *
 * @date 2014-4-12 下午2:42:11 
 *
 * @version V1.0   
 */
public class RequestShowGiftToday extends AsnBase implements ISocketMsgCallBack {

	private IShowGiftTodayCallBack showGiftCallBack;
	private int mCurrPage = 0;

	public void showGiftListToday(byte[] auth, int ver, int uid, int selfuid, int start, int num, int mCurrPage,
			IShowGiftTodayCallBack showGiftCallBack) {
		int type = 0;
		if (mCurrPage == 0) {
			type = USER_ACT.DELIVER_GIFT.getValue();
		} else {
			type = USER_ACT.RECEIVE_GIFT.getValue();
		}
		this.mCurrPage = mCurrPage;

		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetGiftDealList req = new ReqGetGiftDealList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		req.selfuid = BigInteger.valueOf(selfuid);
		req.type = BigInteger.valueOf(type);
		req.dealtime = BigInteger.valueOf(0);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGIFTDEALLIST_CID;
		goGirlPkt.reqgetgiftdeallist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.showGiftCallBack = showGiftCallBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IShowGiftTodayCallBack {
		public void showGiftTodayCallBack(int retCode, int total, int mCurrPage, GiftDealInfoList giftInfoList);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != showGiftCallBack) {
			RspGetGiftDealList rspGetGiftList = pkt.rspgetgiftdeallist;
			showGiftCallBack.showGiftTodayCallBack(rspGetGiftList.retcode.intValue(), rspGetGiftList.total.intValue(), mCurrPage,
					rspGetGiftList.giftdeallist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != showGiftCallBack) {
			showGiftCallBack.showGiftTodayCallBack(resultCode, 0, mCurrPage, null);
		}
	}

}
