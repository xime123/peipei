package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetFansList;
import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetFansList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetFansList.java 
 *
 * @Description: 获取我的关注接口
 *
 * @author allen  
 *
 * @date 2014-5-20 下午7:18:17 
 *
 * @version V1.0   
 */
public class RequestGetFansList extends AsnBase implements ISocketMsgCallBack {

	private IGetFansList getFansList;

	public void getFans(byte[] auth, int ver, int uid, int start, int num, IGetFansList callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetFansList req = new ReqGetFansList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETFANSLIST_CID;
		goGirlPkt.reqgetfanslist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getFansList = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getFansList) {
			RspGetFansList rsp = pkt.rspgetfanslist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				getFansList.getFansList(retCode, rsp.followlist, rsp.isend.intValue());
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != getFansList) {
			getFansList.getFansList(resultCode, null, 0);
		}

	}

	public interface IGetFansList {
		public void getFansList(int retCode, RetFollowInfoList list, int isend);
	}
}
