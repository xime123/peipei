package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDaybookInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetDaybookList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetDaybookList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestDaybookRecord.java 
 *
 * @Description: 获取记录接口
 *
 * @author allen  
 *
 * @date 2014-4-24 上午11:44:31 
 *
 * @version V1.0   
 */
public class RequestDaybookRecord extends AsnBase implements ISocketMsgCallBack {

	private IDaybookRecord callback;

	public void getDaybookRecord(byte[] auth, int ver, int uid, int selfuid, int type, int start, int num, IDaybookRecord callback) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetDaybookList req = new ReqGetDaybookList();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		req.selfuid = BigInteger.valueOf(selfuid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETDAYBOOKLIST_CID;
		goGirlPkt.reqgetdaybooklist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.callback = callback;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	public interface IDaybookRecord {
		public void getDaybook(int retCode, GoGirlDaybookInfoList list, int total);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspGetDaybookList rspGetDaybookList = pkt.rspgetdaybooklist;

		if (null != callback)

			if (checkRetCode(rspGetDaybookList.retcode.intValue()))
				callback.getDaybook(rspGetDaybookList.retcode.intValue(), rspGetDaybookList.daybooklist, rspGetDaybookList.total.intValue());
	}

	@Override
	public void error(int resultCode) {
		if (null != callback)
			callback.getDaybook(resultCode, null, 0);
	}

}
