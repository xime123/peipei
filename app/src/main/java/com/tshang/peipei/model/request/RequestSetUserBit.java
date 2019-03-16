package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqSetUserBit;
import com.tshang.peipei.protocol.asn.gogirl.RspSetUserBit;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestSetUserBit.java 
 *
 * @Description: 设置界面各种开关
 *
 * @author vactor
 *
 * @date 2014-4-21 下午1:36:54 
 *
 * @version V1.0   
 */
public class RequestSetUserBit extends AsnBase implements ISocketMsgCallBack {

	private ISwitchPush iSwitchPush;

	public void setUserBit(byte[] auth, int ver, int uid, int acttion, int type, ISwitchPush callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqSetUserBit req = new ReqSetUserBit();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.act = BigInteger.valueOf(acttion);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQSETUSERBIT_CID;
		goGirlPkt.reqsetuserbit = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iSwitchPush = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspSetUserBit rsp = pkt.rspsetuserbit;
		int retCode = -1;
		if (null != iSwitchPush) {
			retCode = rsp.retcode.intValue();
			iSwitchPush.switchPushCallBack(retCode, rsp.userstatus.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iSwitchPush) {
			iSwitchPush.switchPushCallBack(resultCode, 0);
		}
	}

	public interface ISwitchPush {
		public void switchPushCallBack(int retCode, int status);
	}

}
