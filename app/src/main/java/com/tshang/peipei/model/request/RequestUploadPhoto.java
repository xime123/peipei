package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUploadAlbumPicV2;
import com.tshang.peipei.protocol.asn.gogirl.RspUploadAlbumPicV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestUploadPhoto.java 
 *
 * @Description: 上传照片
 *
 * @author vactor  
 *
 * @date 2014-4-3 下午2:16:35 
 *
 * @version V1.0   
 */
public class RequestUploadPhoto extends AsnBase implements ISocketMsgCallBack {

	private IUploadPhotos mUploadPhotosCallBack;

	/**
	 *  上传相片方法
	 * @author vactor
	 * @param auth
	 * @param ver
	 * @param uid
	 * @param albumid
	 * @param pic
	 * @param picTitle
	 * @param picDesc
	 * @param isSend 最后一张传1,前面的传0
	 */
	public void uploadPhotos(byte[] auth, int ver, int uid, int albumid, byte[] pic, String picTitle, String picDesc, int isSend,
			IUploadPhotos uploadPhotosCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqUploadAlbumPicV2 req = new ReqUploadAlbumPicV2();
		req.albumid = BigInteger.valueOf(albumid);
		req.islastone = BigInteger.valueOf(isSend);
		req.pic = pic;
		req.picdesc = picDesc.getBytes();
		req.pictitle = picTitle.getBytes();
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUPLOADALBUMPICV2_CID;
		goGirlPkt.requploadalbumpicv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);

		this.mUploadPhotosCallBack = uploadPhotosCallBack;
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		RspUploadAlbumPicV2 rspUploadAlbumPic = pkt.rspuploadalbumpicv2;
		int retCode = rspUploadAlbumPic.retcode.intValue();

		if (null != mUploadPhotosCallBack)
			if (checkRetCode(retCode))
				mUploadPhotosCallBack.uploadPhotosCallBack(retCode, rspUploadAlbumPic.charmnum.intValue());

	}

	@Override
	public void error(int resultCode) {
		if (null != mUploadPhotosCallBack)
			mUploadPhotosCallBack.uploadPhotosCallBack(resultCode, 0);
	}

	public interface IUploadPhotos {

		public void uploadPhotosCallBack(int retCode, int charmnum);
	}

}
