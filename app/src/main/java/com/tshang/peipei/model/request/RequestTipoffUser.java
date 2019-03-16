package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqTipoffUser;
import com.tshang.peipei.protocol.asn.gogirl.RspTipoffUser;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestTipoffUser.java 
 *
 * @Description: 举报
 *
 * @author allen   
 *
 * @date 2014-5-13 上午9:48:14 
 *
 * @version V1.0   
 */
public class RequestTipoffUser extends AsnBase implements ISocketMsgCallBack {

	private ITipoffUser tipoffUser;

	public void tipoffUser(byte[] auth, int ver, int uid, int tipuid, int reasonid, ITipoffUser callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqTipoffUser req = new ReqTipoffUser();
		req.tipoffuid = BigInteger.valueOf(tipuid);
		req.actuid = BigInteger.valueOf(uid);
		req.reasonid = BigInteger.valueOf(reasonid);
		req.reasonother = "".getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQTIPOFFUSER_CID;
		goGirlPkt.reqtipoffuser = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.tipoffUser = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface ITipoffUser {
		public void tipoffUserCallBack(int retCode);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != tipoffUser) {
			RspTipoffUser rsp = pkt.rsptipoffuser;
			int retCode = rsp.retcode.intValue();
			tipoffUser.tipoffUserCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (tipoffUser != null) {
			tipoffUser.tipoffUserCallBack(resultCode);
		}

	}

}
