package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDownloadHeadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspDownloadHeadPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestGetRelasionship.java 
 *
 * @Description: 关注
 *
 * @author vactor
 *
 * @date 2014-5-4 上午10:10:44 
 *
 * @version V1.0   
 */
public class RequestDownloadHeadPic extends AsnBase implements ISocketMsgCallBack {

	IGetPicKey mGetPicKey;

	public void getPicKey(byte[] auth, int ver, int uid, int height, int width, IGetPicKey callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqDownloadHeadPic req = new ReqDownloadHeadPic();
		req.height = BigInteger.valueOf(height);
		req.width = BigInteger.valueOf(width);
		req.uid = BigInteger.valueOf(uid);
		req.sincemodtime = BigInteger.valueOf(0);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQDOWNLOADHEADPIC_CID;
		goGirlPkt.reqdownloadheadpic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mGetPicKey = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mGetPicKey) {
			RspDownloadHeadPic rsp = pkt.rspdownloadheadpic;
			String key = new String(rsp.key);
			int uid = rsp.uid.intValue();
			int retCode = rsp.retcode.intValue();
			if (checkRetCode(retCode))
				mGetPicKey.getPicKeyCallBack(retCode, uid, key);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != mGetPicKey) {
			mGetPicKey.getPicKeyCallBack(resultCode, 0, null);
		}
	}

	public interface IGetPicKey {
		public void getPicKeyCallBack(int retCode, int uid, String pic);
	}

}
