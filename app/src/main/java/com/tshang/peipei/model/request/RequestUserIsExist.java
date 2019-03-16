package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqIsExistUser;
import com.tshang.peipei.protocol.asn.gogirl.RspIsExistUser;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/*
 *类        名 : RequestUserIsExist.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 上午10:52:54
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RequestUserIsExist extends AsnBase implements ISocketMsgCallBack {

	IUserIsExist callBack;

	public void isUserExist(byte[] auth, int ver, int type, String name, IUserIsExist callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqIsExistUser req = new ReqIsExistUser();
		req.username = name.getBytes();
		req.type = BigInteger.valueOf(type);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQISEXISTUSER_CID;
		goGirlPkt.reqisexistuser = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		int retCode = -1;
		RspIsExistUser userPacket = pkt.rspisexistuser;
		retCode = userPacket.retcode.intValue();
		if (null != callBack)
			callBack.isUserExistCallBack(retCode, userPacket.userinfo);
	}

	@Override
	public void error(int resultCode) {
		if (null != callBack)
			callBack.isUserExistCallBack(resultCode, null);
	}

	public interface IUserIsExist {

		public void isUserExistCallBack(int retCode, GoGirlUserInfo info);
	}

}
