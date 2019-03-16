package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetGroupInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetGroupInfo;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 请求群信息
 *
 * @author Jeff 
 *
 * @date 2014-8-8 上午10:59:54 
 *
 * @version V1.0   
 */
public class RequestGetGroupInfo extends AsnBase implements ISocketMsgCallBack {

	private IGetGroupInfo callblack;
	private int uid;

	public void reqgetGroupInfo(byte[] auth, int ver, int uid, int groupid, IGetGroupInfo callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetGroupInfo req = new ReqGetGroupInfo();
		req.groupid = BigInteger.valueOf(groupid);
		req.uid = BigInteger.valueOf(uid);
		this.uid = uid;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETGROUPINFO_CID;
		goGirlPkt.reqgetgroupinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callblack) {
			RspGetGroupInfo rsp = pkt.rspgetgroupinfo;

			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				callblack.getGroupInfo(retCode, uid, rsp.groupinfo);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callblack) {
			callblack.getGroupInfo(resultCode, uid, null);
		}

	}

	public interface IGetGroupInfo {
		public void getGroupInfo(int retCode, int uid, GroupInfo info);
	}

}
