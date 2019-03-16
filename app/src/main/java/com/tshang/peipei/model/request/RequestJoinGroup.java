package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqJoinGroup;
import com.tshang.peipei.protocol.asn.gogirl.RspJoinGroup;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestJoinGroup.java 
 *
 * @Description: 申请加入后宫 
 *
 * @author Jeff 
 *
 * @date 2014-9-19 上午10:59:54 
 *
 * @version V1.3.0   
 */
public class RequestJoinGroup extends AsnBase implements ISocketMsgCallBack {

	private IJoinGroup callBack;

	public void rJoinGroup(byte[] auth, int ver, int uid, int adminuid, int groupid, byte[] introduce, byte[] secret, IJoinGroup callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqJoinGroup req = new ReqJoinGroup();
		req.introduce = introduce;
		req.secret = secret;
		req.uid = BigInteger.valueOf(uid);
		req.groupid = BigInteger.valueOf(groupid);
		req.adminuid = BigInteger.valueOf(adminuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQJOINGROUP_CID;
		goGirlPkt.reqjoingroup = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callBack) {
			RspJoinGroup rsp = pkt.rspjoingroup;
			int retCode = rsp.retcode.intValue();
			int memberid = rsp.memberid.intValue();
			if (checkRetCode(retCode))
				callBack.joinHarem(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.joinHarem(resultCode);
		}

	}

	public interface IJoinGroup {
		public void joinHarem(int retCode);
	}

}
