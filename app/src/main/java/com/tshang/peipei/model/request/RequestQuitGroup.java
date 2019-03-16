package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqQuitGroup;
import com.tshang.peipei.protocol.asn.gogirl.RspQuitGroup;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestQuitGroup.java 
 *
 * @Description:踢人或者退出群
 *
 * @author jeff  
 *
 * @date 2014-9-23 上午10:59:13 
 *
 * @version V1.3.0   
 */
public class RequestQuitGroup extends AsnBase implements ISocketMsgCallBack {

	private IQuitGroup addblack;

	public void reqQuitGroup(byte[] auth, int ver, int uid, int actuid, int groupid, IQuitGroup callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqQuitGroup req = new ReqQuitGroup();
		req.actuid = BigInteger.valueOf(actuid);
		req.quituid = BigInteger.valueOf(uid);
		req.groupid = BigInteger.valueOf(groupid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQQUITGROUP_CID;
		goGirlPkt.reqquitgroup = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != addblack) {
			RspQuitGroup rsp = pkt.rspquitgroup;
			int quituid = rsp.quituid.intValue();
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.callbackQuitGroup(retCode, quituid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.callbackQuitGroup(resultCode, -1);
		}

	}

	public interface IQuitGroup {
		public void callbackQuitGroup(int retCode, int quitid);
	}

}
