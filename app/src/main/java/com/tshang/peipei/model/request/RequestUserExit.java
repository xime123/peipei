package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqLoginOut;
import com.tshang.peipei.protocol.asn.gogirl.RspLoginOut;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: ReqUserExit.java 
 *
 * @Description: 用户退出
 *
 * @author vactor
 *
 * @date 2014-4-9 上午10:17:06 
 *
 * @version V1.0   
 */
public class RequestUserExit extends AsnBase implements ISocketMsgCallBack {

	private IUserExit mUserExitCallBack;

	public void exit(byte[] auth, int ver, int uid, IUserExit userExitCallBack) {

		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqLoginOut reqLoginOut = new ReqLoginOut();
		reqLoginOut.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQLOGINOUT_CID;
		goGirlPkt.reqloginout = reqLoginOut;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mUserExitCallBack = userExitCallBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {

		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		RspLoginOut rsp = pkt.rsploginout;
		if (mUserExitCallBack != null) {
			mUserExitCallBack.userExitCallBack(rsp.retcode.intValue());
		}
	}

	@Override
	public void error(int resultCode) {
		mUserExitCallBack.userExitCallBack(resultCode);
	}

	public interface IUserExit {
		public void userExitCallBack(int resultCode);
	}
}
