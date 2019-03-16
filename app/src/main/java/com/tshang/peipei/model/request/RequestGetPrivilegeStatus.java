package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetPrivilegeStatus;
import com.tshang.peipei.protocol.asn.gogirl.RspGetPrivilegeStatus;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 获取权限是否可以使用特殊颜色的广播颜色和圣旨 
 *
 * @author jeff  
 *
 * @date 2014-11-18 上午10:59:54 
 *
 * @version V1.5.0   
 */
public class RequestGetPrivilegeStatus extends AsnBase implements ISocketMsgCallBack {
	public static final int BROADCAST_COLOR_TYPE = 0;
	public static final int BROADCAST_DECREE_TYPE = 1;

	private IGetPrivilegeStatus addblack;
	private int type;

	public void getPrivilegeStatus(byte[] auth, int ver, int type, int uid, IGetPrivilegeStatus callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetPrivilegeStatus req = new ReqGetPrivilegeStatus();
		req.type = BigInteger.valueOf(type);
		req.uid = BigInteger.valueOf(uid);
		this.type = type;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETPRIVILEGESTATUS_CID;
		goGirlPkt.reqgetprivilegestatus = req;
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
			RspGetPrivilegeStatus rsp = pkt.rspgetprivilegestatus;
			int retCode = rsp.retcode.intValue();
			int curnum = rsp.curnum.intValue();
			int maxnum = rsp.maxnum.intValue();
			if (checkRetCode(retCode))
				addblack.getPrivilegeStatus(retCode,type, curnum, maxnum);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.getPrivilegeStatus(resultCode,type, 0, 0);
		}

	}

	public interface IGetPrivilegeStatus {
		public void getPrivilegeStatus(int retCode,int type, int curnum, int maxnum);
	}

}
