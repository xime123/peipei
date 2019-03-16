package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUserInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestChangePassword.java 
 *
 * @Description: 取得用户信息
 *
 * @author allen  
 *
 * @date 2014-4-29 下午2:12:27 
 *
 * @version V1.0   
 */
public class RequestGetUserInfo extends AsnBase implements ISocketMsgCallBack {

	IGetUserInfo iGetUserInfo;

	public void getUserInfo(byte[] auth, int ver, int uid, IGetUserInfo callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetUserInfo req = new ReqGetUserInfo();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUSERINFO_CID;
		goGirlPkt.reqgetuserinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iGetUserInfo = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iGetUserInfo) {
			RspGetUserInfo rsp = pkt.rspgetuserinfo;
			GoGirlUserInfo userInfo = rsp.userinfo;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode)) {
				if (BAApplication.mLocalUserInfo != null) {
					userInfo.auth = BAApplication.mLocalUserInfo.auth;
				}
				iGetUserInfo.getUserInfoCallBack(retCode, userInfo);
			}
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iGetUserInfo) {
			iGetUserInfo.getUserInfoCallBack(resultCode, null);
		}
	}

	public interface IGetUserInfo {
		public void getUserInfoCallBack(int retCode, GoGirlUserInfo userinfo);

	}

}
