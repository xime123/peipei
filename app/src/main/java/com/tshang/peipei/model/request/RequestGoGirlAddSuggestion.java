package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlAddSuggestion;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlAddSuggestion;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 *
 * @Description: 添加意见反馈
 *
 * @author Jeff
 *
 * @date 2014-05-27
 *
 * @version V1.0   
 */
public class RequestGoGirlAddSuggestion extends AsnBase implements ISocketMsgCallBack {

	IAddSuggestion mIAddSuggestion;

	public void addSuggestion(byte[] auth, int ver, String con, String link, String nick, int uid, IAddSuggestion callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGoGirlAddSuggestion req = new ReqGoGirlAddSuggestion();
		req.content = con.getBytes();
		req.from = link.getBytes();
		req.uid = BigInteger.valueOf(uid);
		req.nick = nick.getBytes();
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLADDSUGGESTION_CID;
		goGirlPkt.reqgogirladdsuggestion = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mIAddSuggestion = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mIAddSuggestion) {
			RspGoGirlAddSuggestion rsp = pkt.rspgogirladdsuggestion;
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				mIAddSuggestion.addSuggestionCallBack(retCode);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mIAddSuggestion) {
			mIAddSuggestion.addSuggestionCallBack(resultCode);
		}
	}

	public interface IAddSuggestion {
		public void addSuggestionCallBack(int retCode);
	}

}
