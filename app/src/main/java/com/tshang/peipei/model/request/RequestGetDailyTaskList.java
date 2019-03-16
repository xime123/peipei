package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GGTaskInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetDailyTaskList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetDailyTaskList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetFreshTaskList.java 
 *
 * @Description: 获取每日任务
 *
 * @author allen  
 *
 * @date 2014-7-18 下午12:00:34 
 *
 * @version V1.0   
 */
public class RequestGetDailyTaskList extends AsnBase implements ISocketMsgCallBack {

	private IGetDailyTaskList iCallBack;

	public void getDailyTaskList(byte[] auth, int ver, int uid, int start, int num, IGetDailyTaskList callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetDailyTaskList req = new ReqGetDailyTaskList();
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETDAILYTASKLIST_CID;
		goGirlPkt.reqgetdailytasklist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != iCallBack) {
			RspGetDailyTaskList rsp = pkt.rspgetdailytasklist;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iCallBack.dailyCallBack(retCode, rsp.tasklist, rsp.loginreward.rewardsilvercoin.intValue());
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iCallBack) {
			iCallBack.dailyCallBack(resultCode, null, 0);
		}
	}

	public interface IGetDailyTaskList {
		public void dailyCallBack(int retCode, GGTaskInfoList list, int loginreward);
	}

}
