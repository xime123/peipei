package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqApplyJoinGroup;
import com.tshang.peipei.protocol.asn.gogirl.RspApplyJoinGroup;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestApplyJoinGroup.java 
 *
 * @Description: 申请加入后宫
 *
 * @author jeff  
 *
 * @date 2014-9-20 上午15:18:54 
 *
 * @version V1.3.0   
 */
public class RequestApplyJoinGroup extends AsnBase implements ISocketMsgCallBack {

	private IApplyJoinGroup addblack;

	public void applyJoinGroup(byte[] auth, int ver, int uid, int groupid, byte[] introduce, byte[] secret, IApplyJoinGroup callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqApplyJoinGroup req = new ReqApplyJoinGroup();
		req.uid = BigInteger.valueOf(uid);
		req.groupid = BigInteger.valueOf(groupid);
		req.introduce = introduce;
		req.secret = secret;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQAPPLYJOINGROUP_CID;
		goGirlPkt.reqapplyjoingroup = req;
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
			RspApplyJoinGroup rsp = pkt.rspapplyjoingroup;
			int applytime = rsp.applytime.intValue();
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				addblack.applyJoinGroup(retCode, applytime);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.applyJoinGroup(resultCode, 0);
		}

	}

	public interface IApplyJoinGroup {
		public void applyJoinGroup(int retCode, int applytime);
	}

}
