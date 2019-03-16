package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqEditSkill;
import com.tshang.peipei.protocol.asn.gogirl.RspEditSkill;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestEditSkill.java 
 *
 * @Description: 修改技能
 *
 * @author Jeff
 *
 */
public class RequestEditSkill extends AsnBase implements ISocketMsgCallBack {

	IEditSkill iEditSkill;

	public void editSkill(byte[] auth, int ver, String desc, String title, int skillid, int giftid, int num, int uid, IEditSkill callBack) {

		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqEditSkill req = new ReqEditSkill();
		req.desc = desc.getBytes();
		req.skillid = BigInteger.valueOf(skillid);
		req.giftnum = BigInteger.valueOf(num);
		req.title = title.getBytes();
		req.uid = BigInteger.valueOf(uid);
		req.giftid = BigInteger.valueOf(giftid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQEDITSKILL_CID;
		goGirlPkt.reqeditskill = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.iEditSkill = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		if (null != iEditSkill) {
			RspEditSkill rsp = pkt.rspeditskill;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				iEditSkill.editSkillCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != iEditSkill) {
			iEditSkill.editSkillCallBack(resultCode);
		}

	}

	public interface IEditSkill {
		public void editSkillCallBack(int retCode);
	}

}
