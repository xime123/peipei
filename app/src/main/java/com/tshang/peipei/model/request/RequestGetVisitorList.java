package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetVisitorList;
import com.tshang.peipei.protocol.asn.gogirl.RetRelevantPeopleInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetVisitorList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetVisitorList.java 
 *
 * @Description: 看过我的人
 *
 * @author allen  
 *
 * @date 2014-8-11 下午7:38:11 
 *
 * @version V1.0   
 */
public class RequestGetVisitorList extends AsnBase implements ISocketMsgCallBack {

	private IGetVisitorList getVisitor;

	public void getVisitorList(byte[] auth, int ver, int uid, int hostuid, int start, int num, IGetVisitorList callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetVisitorList req = new ReqGetVisitorList();
		req.selfuid = BigInteger.valueOf(uid);
		req.hostuid = BigInteger.valueOf(hostuid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETVISITORLIST_CID;
		goGirlPkt.reqgetvisitorlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getVisitor = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != ydmxMsg) {
			RspGetVisitorList rsp = pkt.rspgetvisitorlist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getVisitor.getVisitorListCallBack(retCode, rsp.isend.intValue(), rsp.visitorlist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getVisitor) {
			getVisitor.getVisitorListCallBack(resultCode, 1, null);
		}
	}

	public interface IGetVisitorList {
		public void getVisitorListCallBack(int retCode, int isend, RetRelevantPeopleInfoList list);
	}
}
