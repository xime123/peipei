package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqThirdPartyLogin;
import com.tshang.peipei.protocol.asn.gogirl.RspThirdPartyLogin;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestThirdLogin.java 
 *
 * @Description: 第三方登录
 *
 * @author allen  
 *
 * @date 2014-4-14 下午3:28:52 
 *
 * @version V1.0   
 */
public class RequestThirdLogin extends AsnBase implements ISocketMsgCallBack {

	private IThirdLogin mCallBack;

	/**
	 * 登录
	 * @param auth
	 * @param ver
	 * @param userAccount
	 * @param userPwd
	 * @param callBack
	 */
	@SuppressWarnings("deprecation")
	public void login(byte[] auth, int ver, String nick, int sex, int type, String userAccount, int inviteuid, String imei, IThirdLogin callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqThirdPartyLogin req = new ReqThirdPartyLogin();
		req.appver = BigInteger.valueOf(ver);
		req.imei = imei.getBytes();
		req.phonebrand = "".getBytes();
		req.phoneos = BAConstants.RECHARGE_OS_ANDROID;
		req.sdkver = android.os.Build.VERSION.SDK.getBytes();
		req.username = userAccount.getBytes();
		req.inviteuid = BigInteger.valueOf(inviteuid);
		req.nick = nick.getBytes();
		req.sex = BigInteger.valueOf(sex);
		req.type = BigInteger.valueOf(type);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQTHIRDPARTYLOGIN_CID;
		goGirlPkt.reqthirdpartylogin = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspThirdPartyLogin rsp = pkt.rspthirdpartylogin;
		int retCode = -1;
		if (null != mCallBack) {
			retCode = rsp.retcode.intValue();
			rsp.userinfo.auth = rsp.auth;
			String errorMsg = new String(rsp.retmsg);
			mCallBack.thirdLoginCallBack(retCode, errorMsg, rsp.userinfo);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != mCallBack)
			mCallBack.thirdLoginCallBack(resultCode, "", null);

	}

	public interface IThirdLogin {

		public void thirdLoginCallBack(int retcode, String errorMsg, GoGirlUserInfo user);
	}

}
