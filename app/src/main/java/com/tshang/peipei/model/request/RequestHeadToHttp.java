package com.tshang.peipei.model.request;

import org.json.JSONObject;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;

/**
 * @Title: RequestHeadToHttp.java 
 *
 * @Description: http下载头像
 *
 * @author allen  
 *
 * @date 2015-3-13 下午6:19:28 
 *
 * @version V1.0   
 */
public class RequestHeadToHttp extends AsnBase implements ISocketMsgCallBack {

	private IHeadToHttp callback;
	private int type;

	public void loadHead(int uid, int type, IHeadToHttp callback) {
		this.type = type;
		this.callback = callback;
		String host = "ppapp.tshang.com";
		if (BAConstants.IS_TEST) {
			host = "pptest.yidongmengxiang.com";
		}

		String url = "/share/downuserface?uid=" + uid;

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
				System.arraycopy(msg, totalLen - len, tempByte, 0, len);

				String s = new String(tempByte);
				try {
					JSONObject j = new JSONObject(s);
					int retCode = -1;
					if (j.has("retcode")) {
						retCode = j.getInt("retcode");
					}
					String path = "";
					if (j.has("picurl")) {
						path = j.getString("picurl");
					}
					if (callback != null) {
						callback.resultLoadHead(retCode, type, path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			if (callback != null) {
				callback.resultLoadHead(-1, type, "");
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (callback != null) {
			callback.resultLoadHead(resultCode, type, "");
		}
	}

	public interface IHeadToHttp {
		public void resultLoadHead(int retCode, int type, String url);
	}

}
