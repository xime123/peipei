package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoListV2;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRankGroupListV2;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRankGroupListV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 获取后宫排名列表
 *
 * @author jeff  
 *
 * @date 2014-11-21 上午10:59:54 
 *
 * @version V1.5.0   
 */
public class RequestGetRankGroupListV2 extends AsnBase implements ISocketMsgCallBack {

	private IGetRankGroupListV2 callback;

	public void getRankGroupListV2(byte[] auth, int ver, int uid, int num, int start, IGetRankGroupListV2 callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRankGroupListV2 req = new ReqGetRankGroupListV2();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRANKGROUPLISTV2_CID;
		goGirlPkt.reqgetrankgrouplistv2 = req;
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
			RspGetRankGroupListV2 rsp = pkt.rspgetrankgrouplistv2;
			GroupInfoListV2 infoList = rsp.grouplist;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			int total = rsp.total.intValue();

			if (checkRetCode(retCode))
				callback.getGroupInfoListV2(retCode, infoList, isEnd, total);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getGroupInfoListV2(resultCode, null, 1, 0);
		}

	}

	public interface IGetRankGroupListV2 {
		public void getGroupInfoListV2(int retCode, GroupInfoListV2 infoList, int isEnd, int total);
	}

}
