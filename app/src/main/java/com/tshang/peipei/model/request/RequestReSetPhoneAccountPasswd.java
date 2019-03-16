package com.tshang.peipei.model.request;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqReSetPhoneAccountPasswdV2;
import com.tshang.peipei.protocol.asn.gogirl.RspReSetPhoneAccountPasswdV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestReSetPhoneAccountPasswd.java 
 *
 * @Description: 手机找回密码
 *
 * @author allen  
 *
 * @date 2014-12-3 下午3:33:31 
 *
 * @version V1.0   
 */
public class RequestReSetPhoneAccountPasswd extends AsnBase implements ISocketMsgCallBack {

	private iReSetPhoneAccountPasswd callback;
	private static String EASECHAT_USER_INFO_DES_KEY = "y3m&c*B#";

	public void reSetPhoneAccountPasswd(byte[] auth, int ver, String phoneno, String msgcode, String phonePwd, iReSetPhoneAccountPasswd callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqReSetPhoneAccountPasswdV2 req = new ReqReSetPhoneAccountPasswdV2();
		req.phoneno = phoneno.getBytes();
		req.msgcode = msgcode.getBytes();

		// 加密密码
		byte[] pwdTemp = BaseTools.getMergeArray(BaseTools.getMD5Str(phonePwd.getBytes()), "qazwsxed".getBytes());
		byte[] pwdByte = BaseDes.encrypt(pwdTemp, EASECHAT_USER_INFO_DES_KEY.getBytes());
		byte[] checksumByte = BaseTools.getMD5Str(BaseTools.getMergeArray(phoneno.getBytes(), pwdTemp));
		req.newpwd = pwdByte;
		req.checksum = checksumByte;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQRESETPHONEACCOUNTPASSWDV2_CID;
		goGirlPkt.reqresetphoneaccountpasswdv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {
			RspReSetPhoneAccountPasswdV2 rsp = pkt.rspresetphoneaccountpasswdv2;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callback.resultSetPhoneAccountPasswd(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.resultSetPhoneAccountPasswd(resultCode);
		}
	}

	public interface iReSetPhoneAccountPasswd {
		public void resultSetPhoneAccountPasswd(int retCode);
	}
}
