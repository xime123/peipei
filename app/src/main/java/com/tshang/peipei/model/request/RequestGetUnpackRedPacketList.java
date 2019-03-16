package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetUnpackRedPacketList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetUnpackRedPacketList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetDeliverRedPacketList.java 
 *
 * @Description: 我收到的红包
 *
 * @author Jeff  
 *
 * @date 2014-10-25 下午16:16:54 
 *
 * @version V1.4.0   
 */
public class RequestGetUnpackRedPacketList extends AsnBase implements ISocketMsgCallBack {

	private IGetUnpackRedPacketList callBack;

	public void getUnpackRedPacketList(byte[] auth, int ver, int uid, int redpacketuid, int start, int num, IGetUnpackRedPacketList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetUnpackRedPacketList req = new ReqGetUnpackRedPacketList();
		req.selfuid = BigInteger.valueOf(uid);
		req.uid = BigInteger.valueOf(redpacketuid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETUNPACKREDPACKETLIST_CID;
		goGirlPkt.reqgetunpackredpacketlist = req;
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
			RspGetUnpackRedPacketList rsp = pkt.rspgetunpackredpacketlist;
			int retCode = rsp.retcode.intValue();
			int isend = rsp.isend.intValue();
			RedPacketInfoList list = rsp.redpacketlist;
			if (checkRetCode(retCode))
				callBack.getUnpackRedPacketList(retCode, isend, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callBack) {
			callBack.getUnpackRedPacketList(resultCode, -1, null);
		}

	}

	public interface IGetUnpackRedPacketList {
		public void getUnpackRedPacketList(int retCode, int isend, RedPacketInfoList list);
	}

}
