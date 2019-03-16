package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlRegister;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlRegister;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.babase.BAConstants;
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
public class RequestUserRegist extends AsnBase implements ISocketMsgCallBack {

	private IRegisterUser mRegisterUserCallBack;
	private static String EASECHAT_USER_INFO_DES_KEY = "y3m&c*B#";
	private int gender;

	/**
	 *  注册
	 * @param auth
	 * @param ver
	 * @param userAccount
	 * @param userPwd
	 * @param msgcode
	 * @param userNick
	 * @param gender
	 * @param registerUserCallBack
	 */
	public void registerUser(byte[] auth, int ver, String userAccount, String userPwd, String msgcode, String userNick, int gender, long inviteuid,
			String imei, IRegisterUser registerUserCallBack) {
		this.gender = gender;
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGoGirlRegister req = new ReqGoGirlRegister();
		req.appver = BigInteger.valueOf(ver);
		req.msgcode = msgcode.getBytes();
		req.from = BAConstants.RECHARGE_OS_ANDROID;
		req.imei = imei.getBytes();
		req.nickname = userNick.getBytes();
		req.osver = "".getBytes();
		req.inviteuid = BigInteger.valueOf(inviteuid);
		// 加密密码
		byte[] pwdTemp = BaseTools.getMergeArray(BaseTools.getMD5Str(userPwd.getBytes()), "qazwsxed".getBytes());
		byte[] pwdByte = BaseDes.encrypt(pwdTemp, EASECHAT_USER_INFO_DES_KEY.getBytes());
		byte[] checksumByte = BaseTools.getMD5Str(BaseTools.getMergeArray(userAccount.getBytes(), pwdTemp));
		req.passwd = pwdByte;
		req.checksum = checksumByte;
		req.phonebrand = "".getBytes();
		req.sex = BigInteger.valueOf(gender);
		req.username = userAccount.getBytes();
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLREGISTER_CID;
		goGirlPkt.reqgogirlregister = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mRegisterUserCallBack = registerUserCallBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		int retCode = -1;
		RspGoGirlRegister registerPacket = pkt.rspgogirlregister;
		retCode = registerPacket.retcode.intValue();
		GoGirlUserInfo userInfo = new GoGirlUserInfo();
		userInfo.auth = registerPacket.auth;
		userInfo.nick = registerPacket.nick;
		userInfo.username = registerPacket.username;
		userInfo.uid = registerPacket.uid;
		userInfo.sex = BigInteger.valueOf(gender);
		userInfo.birthday = BigInteger.valueOf(0);
		userInfo.appver = BigInteger.valueOf(0);
		userInfo.authexpiretime = BigInteger.valueOf(0);
		userInfo.channel = "".getBytes();
		userInfo.chatthreshold = BigInteger.valueOf(0);
		userInfo.createtime = BigInteger.valueOf(System.currentTimeMillis());
		userInfo.city = "".getBytes();
		userInfo.email = "".getBytes();
		userInfo.forbidtime = BigInteger.valueOf(0);
		userInfo.gradeinfo = "".getBytes();
		userInfo.headpickey = "".getBytes();
		userInfo.imei = "".getBytes();
		userInfo.lastlogtime = BigInteger.valueOf(0);
		userInfo.verifycode = "".getBytes();
		userInfo.inviteuid = BigInteger.valueOf(0);
		userInfo.userstatus = BigInteger.valueOf(0);
		userInfo.ranknum = BigInteger.valueOf(0);
		userInfo.islocale = BigInteger.valueOf(0);
		userInfo.nobreaklogs = BigInteger.valueOf(0);
		userInfo.phone = registerPacket.username;
		userInfo.phonebrand = "".getBytes();
		userInfo.osver = "".getBytes();
		userInfo.phoneos = BigInteger.valueOf(0);
		userInfo.pwd = "".getBytes();
		userInfo.lognum = BigInteger.valueOf(0);
		userInfo.token = "".getBytes();
		userInfo.type = BigInteger.valueOf(5);
		userInfo.mailcode = "".getBytes();
		userInfo.showpickey = "".getBytes();

		if (null != mRegisterUserCallBack)
			mRegisterUserCallBack.registerUserCallBack(retCode, new String(registerPacket.retmsg), userInfo);
	}

	@Override
	public void error(int resultCode) {
		if (null != mRegisterUserCallBack)
			mRegisterUserCallBack.registerUserCallBack(resultCode, "", null);
	}

	public interface IRegisterUser {

		public void registerUserCallBack(int retCode, String errorMsg, GoGirlUserInfo userInfo);

	}
}
