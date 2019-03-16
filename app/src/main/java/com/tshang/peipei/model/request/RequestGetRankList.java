package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRankDataList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRankDataList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRankList.java 
 *
 * @Description: 拉取排行
 *
 * @author vactor
 *
 * @date 2014-4-24 上午11:21:53 
 *
 * @version V1.0   
 */
public class RequestGetRankList extends AsnBase implements ISocketMsgCallBack {

	private IGetRankList iGetRankList;
	private int type;

	public void getRanklist(byte[] auth, int ver, int uid, int rankId, int start, int num, IGetRankList callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetRankDataList req = new ReqGetRankDataList();
		req.num = BigInteger.valueOf(num);
		req.rankid = BigInteger.valueOf(rankId);
		req.start = BigInteger.valueOf(start);
		req.uid = BigInteger.valueOf(uid);

		this.type = rankId;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRANKDATALIST_CID;
		goGirlPkt.reqgetrankdatalist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iGetRankList = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iGetRankList) {
			RspGetRankDataList rsp = pkt.rspgetrankdatalist;
			int retCode = rsp.retcode.intValue();
			int total = rsp.total.intValue();
			int end = rsp.isend.intValue();
			GoGirlUserInfoList list = rsp.userlist;
			iGetRankList.getRankListCallBack(retCode, total, end, type, list);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iGetRankList) {
			iGetRankList.getRankListCallBack(resultCode, -1, -1, type, null);
		}
	}

	public interface IGetRankList {
		public void getRankListCallBack(int retCode, int total, int end, int type, GoGirlUserInfoList list);

	}

}
