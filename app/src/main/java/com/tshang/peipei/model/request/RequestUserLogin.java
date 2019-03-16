package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlLogin;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlLogin;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/*
 *类        名 : RequestUserLogin.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 下午1:42:58
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RequestUserLogin extends AsnBase implements ISocketMsgCallBack {

	ILogin mCallBack;
	private String userName;

	/**
	 * 登录
	 * @param auth
	 * @param ver
	 * @param userAccount
	 * @param userPwd
	 * @param callBack
	 */
	@SuppressWarnings("deprecation")
	public void login(byte[] auth, int ver, String userAccount, String userPwd, String imei, ILogin callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGoGirlLogin req = new ReqGoGirlLogin();
		req.appver = BigInteger.valueOf(ver);
		req.imei = imei.getBytes();
		req.phonebrand = "".getBytes();
		req.phoneos = BAConstants.RECHARGE_OS_ANDROID;
		req.sdkver = android.os.Build.VERSION.SDK.getBytes();
		req.username = userAccount.getBytes();
		userName = userAccount;
		// 加密密码
		byte[] pwd = BaseTools.getMD5Str(userPwd.getBytes());
		byte[] key = new byte[8];
		System.arraycopy(pwd, 8, key, 0, 8);
		byte[] pp;
		int i = req.username.length % 8;
		pp = BaseTools.getMergeArray(req.username, new byte[8 - i]);
		byte[] pwdByte = BaseDes.encrypt(pp, key);

		req.verifystr = pwdByte;
		req.checksum = BaseTools.getMD5Str(BaseTools.getMergeArray(req.username, req.verifystr));

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLLOGIN_CID;
		goGirlPkt.reqgogirllogin = req;
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
		RspGoGirlLogin loginPacket = pkt.rspgogirllogin;
		int retCode = loginPacket.retcode.intValue();
		String message = new String(loginPacket.retmsg);
		loginPacket.userinfo.auth = loginPacket.auth;

		if (null != mCallBack) {
			mCallBack.loginCallBack(retCode, message, loginPacket.userinfo, userName);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != mCallBack)
			mCallBack.loginCallBack(resultCode, "", null, userName);
	}

	public interface ILogin {

		public void loginCallBack(int retcode, String msg, GoGirlUserInfo user, String userName);
	}

}
