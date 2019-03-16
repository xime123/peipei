package com.tshang.peipei.model.request;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;

/**
 * @Title: RequestLoadingPic.java 
 *
 * @Description: 获取闪屏加载页
 *
 * @author jeff 
 *
 * @date 2014-12-02 上午10:59:54 
 *
 * @version V1.6.0   
 */
public class RequestGetShowAdv extends AsnBase implements ISocketMsgCallBack {

	private IGetShowAdvUrl addblack;

	public void getAdv(Activity activity, IGetShowAdvUrl addblack) {
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/media/getShowMedias";
		this.addblack = addblack;
		//		System.out.println("host========" + host);
		//		System.out.println("地址========" + url);

		byte[] requests = http_encode(url, host);
		PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		if (msg != null && msg.length > 0) {
			int len = AsnProtocolTools.http_net_body_length(msg);
			int totalLen = msg.length;
			if (len > 0) {
				byte[] tempByte = new byte[len];
				//				int j = 0;
				//				for (int i = totalLen - len; i < totalLen; i++) {
				//					tempByte[j++] = msg[i];
				//				}
				//				
				System.arraycopy(msg, totalLen - len, tempByte, 0, len);
				String urlData = new String(tempByte);
				if (null != addblack) {
					addblack.getAdvUrl(urlData);
				}
				//				System.out.println("=22==" + new String(tempByte));
			}

		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.getAdvUrl("");
		}

	}

	public interface IGetShowAdvUrl {
		public void getAdvUrl(String urlData);
	}

}
