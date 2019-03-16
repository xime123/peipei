package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetRankSkillListV2;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RspGetRankSkillListV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRankSkillList.java 
 *
 * @Description: 获取技能列表 
 *
 * @author allen  
 *
 * @date 2014-7-14 下午8:24:27 
 *
 * @version V1.0   
 */
public class RequestGetRankSkillListV2 extends AsnBase implements ISocketMsgCallBack {

	private IMainSkillV2 callback;

	public void getHallSkillV2List(byte[] auth, int ver, int uid, int type, int start, int num, IMainSkillV2 mainHotCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体	
		ReqGetRankSkillListV2 req = new ReqGetRankSkillListV2();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETRANKSKILLLISTV2_CID;
		goGirlPkt.reqgetrankskilllistv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		// 编码
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callback = mainHotCallBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callback) {

			RspGetRankSkillListV2 rspGetMainList = pkt.rspgetrankskilllistv2;
			int isEnd = rspGetMainList.isend.intValue();
			int retCode = rspGetMainList.retcode.intValue();
			if (checkRetCode(retCode))
				callback.getMainSkillV2CallBack(retCode, isEnd, rspGetMainList.skilllist);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.getMainSkillV2CallBack(resultCode, -1, null);
		}
	}

	public interface IMainSkillV2 {
		public void getMainSkillV2CallBack(int retCode, int isEnd, RetGGSkillInfoList userInfoList);
	}

}
