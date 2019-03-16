package com.tshang.peipei.model.request;

import android.app.Activity;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestLoadingPic.java 
 *
 * @Description: 举报相片
 *
 * @author jeff 
 *
 * @date 2014-12-02 上午10:59:54 
 *
 * @version V1.6.0   
 */
public class RequestReportPic extends AsnBase implements ISocketMsgCallBack {

	private IReportPicListener callback;

	public void getReportPic(Activity activity, String pic_key, int pic_uid, IReportPicListener callback) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		int uid = 0;
		if (info != null) {
			uid = info.uid.intValue();
		}
		String host = "op.sanxin8.cn";
		String url = "/topic/Albumpic?key=" + pic_key + "&self_uid=" + uid + "&photo_uid=" + pic_uid;

		byte[] requests = http_encode(url, host);
		PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
		this.callback = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		//		System.out.println("--------" + new String(msg));
		if (msg != null && msg.length > 0) {
			if (null != callback) {
				callback.reportPicCallBack(0);
			}
		} else {
			if (null != callback) {
				callback.reportPicCallBack(-1);
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != callback) {
			callback.reportPicCallBack(-1);
		}

	}

	public interface IReportPicListener {
		public void reportPicCallBack(int resultCode);
	}

}
