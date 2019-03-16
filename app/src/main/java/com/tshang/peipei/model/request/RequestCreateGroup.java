package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqCreateGroup;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateGroup;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestCreateGroup.java 
 *
 * @Description: 创建后宫 
 *
 * @author Jeff  
 *
 * @date 2014-9-17 上午10:59:54 
 *
 * @version V1.3.0   
 */
public class RequestCreateGroup extends AsnBase implements ISocketMsgCallBack {

	private ICreateGroup iCreateGroup;

	public void reqCreateGroup(byte[] auth, int ver, byte[] badgepic, String name, String notice, int uid, ICreateGroup callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqCreateGroup req = new ReqCreateGroup();
		req.badgepic = badgepic;
		req.name = name.getBytes();
		req.notice = notice.getBytes();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCREATEGROUP_CID;
		goGirlPkt.reqcreategroup = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iCreateGroup = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iCreateGroup) {
			RspCreateGroup rsp = pkt.rspcreategroup;
			int retCode = rsp.retcode.intValue();
			int groupid = rsp.groupid.intValue();
			if (checkRetCode(retCode))
				iCreateGroup.createGroup(retCode, groupid);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iCreateGroup) {
			iCreateGroup.createGroup(resultCode, -1);
		}

	}

	public interface ICreateGroup {
		public void createGroup(int retCode, int groupid);
	}

}
