package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUpdateGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspUpdateGroupInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddReply.java 
 *
 * @Description: 回复
 *
 * @author vactor
 *
 * @date 2014-4-30 下午2:21:11 
 *
 * @version V1.0   
 */
public class RequestUpdateGroupInfo extends AsnBase implements ISocketMsgCallBack {

	IUpdateGroupInfo callback;

	@SuppressWarnings("unchecked")
	public void updateGroupInfo(byte[] auth, int ver, byte[] badgepic, int groupid, String name, String notice, int uid, IUpdateGroupInfo callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqUpdateGroupInfo req = new ReqUpdateGroupInfo();
		req.badgepic = badgepic;
		req.groupid = BigInteger.valueOf(groupid);
		req.name = name.getBytes();
		req.notice = notice.getBytes();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPDATEGROUPINFO_CID;
		goGirlPkt.requpdategroupinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspUpdateGroupInfo rsp = pkt.rspupdategroupinfo;
			int retCode = rsp.retcode.intValue();
			int groupid = rsp.groupid.intValue();
			if (checkRetCode(retCode))
				callback.updateGroupInfo(retCode, groupid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.updateGroupInfo(resultCode, -1);
		}

	}

	public interface IUpdateGroupInfo {
		public void updateGroupInfo(int retCode, int groupId);
	}

}
