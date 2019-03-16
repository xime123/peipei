package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqChangePasswd;
import com.tshang.peipei.protocol.asn.gogirl.RspChangePasswd;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.BaseDes;
import com.tshang.peipei.base.BaseTools;
import com.tshang.peipei.base.babase.BATools;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestChangePassword.java 
 *
 * @Description: 修改密码
 *
 * @author vactor  
 *
 * @date 2014-4-29 下午2:12:27 
 *
 * @version V1.0   
 */
public class RequestChangePassword extends AsnBase implements ISocketMsgCallBack {

	IChangePwd iChangePwd;

	public void changePassword(byte[] auth, int ver, int uid, String oldPwd, String newPwd, IChangePwd callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqChangePasswd req = new ReqChangePasswd();
		req.uid = BigInteger.valueOf(uid);

		byte[] tmpOld = BATools.getMergeArray(BaseTools.getMD5Str(BATools.intToByteArray1(uid)), "qazwsxed".getBytes());
		byte[] key = BaseTools.getMD5Str_8(oldPwd.getBytes());
		byte[] pwdByteold = BaseDes.encrypt(tmpOld, key);

		byte[] tmpNew = BaseTools.getMergeArray(BaseTools.getMD5Str(newPwd.getBytes()), "qazwsxed".getBytes());
		byte[] pwdBytenew = BaseDes.encrypt(tmpNew, key);

		req.newpwdverifystr = pwdBytenew;
		req.oldpwdverifystr = pwdByteold;
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCHANGEPASSWD_CID;
		goGirlPkt.reqchangepasswd = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iChangePwd = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iChangePwd) {
			RspChangePasswd rsp = pkt.rspchangepasswd;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iChangePwd.changePwdCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iChangePwd) {
			iChangePwd.changePwdCallBack(resultCode);
		}

	}

	public interface IChangePwd {
		public void changePwdCallBack(int retCode);
	}

}
