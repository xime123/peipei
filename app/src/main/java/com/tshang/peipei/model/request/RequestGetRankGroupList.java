package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GroupInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRankGroupList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRankGroupList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

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
public class RequestGetRankGroupList extends AsnBase implements ISocketMsgCallBack {

	private IGetRankGroupList callback;

	public void getRankGroupList(byte[] auth, int ver, int uid, int num, int start, IGetRankGroupList callback) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRankGroupList req = new ReqGetRankGroupList();
		req.num = BigInteger.valueOf(num);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRANKGROUPLIST_CID;
		goGirlPkt.reqgetrankgrouplist = req;
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
			RspGetRankGroupList rsp = pkt.rspgetrankgrouplist;
			GroupInfoList infoList = rsp.grouplist;
			int retCode = rsp.retcode.intValue();
			int isEnd = rsp.isend.intValue();
			int total = rsp.total.intValue();

			if (checkRetCode(retCode))
				callback.getGroupInfoList(retCode, infoList, isEnd, total);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getGroupInfoList(resultCode, null, 1, 0);
		}

	}

	public interface IGetRankGroupList {
		public void getGroupInfoList(int retCode, GroupInfoList infoList, int isEnd, int total);
	}

}
