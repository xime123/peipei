package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetMainList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetMainList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseLog;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/*
 *类        名 : RequestUserRegist.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 上午11:43:53
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RequestMainHallHot extends AsnBase implements ISocketMsgCallBack {

	private IMainHot mMainHotCallBack;

	public void getMainHallList(byte[] auth, int ver, int uid, int start, int num, IMainHot mainHotCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetMainList req = new ReqGetMainList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETMAINLIST_CID;
		goGirlPkt.reqgetmainlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mMainHotCallBack = mainHotCallBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	/* (non-Javadoc)
	 * @see com.tshang.peipei.model.interfaces.ISocketMsgCallBack#succuess(byte[])
	 */
	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mMainHotCallBack) {
			RspGetMainList rspGetMainList = pkt.rspgetmainlist;
			int isEnd = rspGetMainList.isend.intValue();
			BaseLog.i("vactor_log", "isend:" + isEnd);
			mMainHotCallBack.getMainHotCallBack(rspGetMainList.retcode.intValue(), isEnd, rspGetMainList.userlist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mMainHotCallBack) {
			mMainHotCallBack.getMainHotCallBack(resultCode, -1, null);
		}
	}

	public interface IMainHot {
		public void getMainHotCallBack(int retCode, int isEnd, GoGirlUserInfoList userInfoList);
	}

}
