package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRelevantGroupList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRelevantGroupList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRelevantGroupList.java 
 *
 * @Description: 获取
 *
 * @author jeff  
 *
 * @date 2014-9-17 上午10:59:54 
 *
 * @version V1.3.0   
 */
public class RequestGetRelevantGroupList extends AsnBase implements ISocketMsgCallBack {

	private IGetRelevantGroupList callback;
	private BAHandler handler;

	public void getRelevantGroupList(byte[] auth, int ver, int uid, int selfuid, IGetRelevantGroupList callback, BAHandler handler) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRelevantGroupList req = new ReqGetRelevantGroupList();
		req.selfuid = BigInteger.valueOf(selfuid);
		req.uid = BigInteger.valueOf(uid);
		this.handler = handler;
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRELEVANTGROUPLIST_CID;
		goGirlPkt.reqgetrelevantgrouplist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != callback) {
			RspGetRelevantGroupList rsp = pkt.rspgetrelevantgrouplist;
			int retCode = rsp.retcode.intValue();
			GroupInfoList info = rsp.grouplist;
			int isEnd = rsp.isend.intValue();
			if (checkRetCode(retCode))
				callback.getRelevantGroupList(retCode, isEnd, info, handler);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getRelevantGroupList(resultCode, -1, null, handler);
		}

	}

	public interface IGetRelevantGroupList {
		public void getRelevantGroupList(int retCode, int end, GroupInfoList info, BAHandler hander);
	}

}
