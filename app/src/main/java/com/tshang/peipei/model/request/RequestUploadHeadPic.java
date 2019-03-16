package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUploadPersonalPic;
import com.tshang.peipei.protocol.asn.gogirl.RspUploadPersonalPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestUploadHeadPic.java 
 *
 * @Description:  上传头像
 *
 * @author vactor
 *
 * @date 2014-4-21 下午3:28:57 
 *
 * @version V1.0   
 */
public class RequestUploadHeadPic extends AsnBase implements ISocketMsgCallBack {

	private IUploadHeadPic iUploadHeadPic;

	public void uploadHeadPic(byte[] auth, int ver, int uid, byte[] pic, int type, IUploadHeadPic callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);

		ReqUploadPersonalPic req = new ReqUploadPersonalPic();
		req.headpic = pic;
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPLOADPERSONALPIC_CID;
		goGirlPkt.requploadpersonalpic = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.iUploadHeadPic = callBack;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspUploadPersonalPic rspUploadAlbumPic = pkt.rspuploadpersonalpic;
		int retCode = rspUploadAlbumPic.retcode.intValue();
		if (null != iUploadHeadPic) {
			if (checkRetCode(retCode))
				iUploadHeadPic.uploadHeadPicCallBack(retCode);
		}

	}

	@Override
	public void error(int resultCode) {
		if (null != iUploadHeadPic) {
			iUploadHeadPic.uploadHeadPicCallBack(resultCode);
		}
	}

	public interface IUploadHeadPic {
		public void uploadHeadPicCallBack(int retCode);
	}

}
