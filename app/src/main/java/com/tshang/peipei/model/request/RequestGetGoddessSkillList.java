package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.activity.skillnew.bean.GoddessSkillInfo;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSkillInfo;
import com.tshang.peipei.protocol.asn.gogirl.SkillTextInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGetGoddessSkillList.java 
 *
 * @Description: 获取女神技列表
 *
 * @author DYH  
 *
 * @date 2015-11-6 下午6:45:44 
 *
 * @version V1.0   
 */
public class RequestGetGoddessSkillList extends AsnBase implements ISocketMsgCallBack {

	private GetGoddessSkillListCallBack callBack;

	public void requestGoddessSkillList(byte[] auth, int ver, int uid, int type, int start, int num, GetGoddessSkillListCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSkillInfo req = new ReqGetSkillInfo();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSKILLINFO_CID;
		goGirlPkt.reqgetskillinfo = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspGetSkillInfo rsp = pkt.rspgetskillinfo;
			int retCode = rsp.retcode.intValue();
			int isSend = rsp.isend.intValue();
			GoddessSkillInfo obj = new GoddessSkillInfo();
			int isOpenKisll = rsp.isopenskill.intValue();
			int totalSkillNum = rsp.totalskillnum.intValue();
			int selectedSkillNum = rsp.selectedskillnum.intValue();
			SkillTextInfoList info = rsp.skillTextInfoList;
			obj.setIsopenskill(isOpenKisll);
			obj.setTotalskillnum(totalSkillNum);
			obj.setSelectedskillnum(selectedSkillNum);
			obj.setObject(info);
			callBack.onGoddessSkillListSuccess(retCode, isSend, obj);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.onGoddessSkillListError(resultCode);
	}

	public interface GetGoddessSkillListCallBack {
		public void onGoddessSkillListSuccess(int code, int isSend, Object obj);

		public void onGoddessSkillListError(int code);
	}

}
