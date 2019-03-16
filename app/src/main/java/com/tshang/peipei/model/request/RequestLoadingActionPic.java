package com.tshang.peipei.model.request;

import java.io.File;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.tshang.peipei.base.SdCardUtils;
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
public class RequestLoadingActionPic extends AsnBase implements ISocketMsgCallBack {

	private ILoadingPicHttp addblack;
	private String ImageName;
	private int type = -1;

	public void getLoadingPic(Activity activity, String saveUrl, long time, ILoadingPicHttp callback) {
		if (TextUtils.isEmpty(saveUrl)) {
			return;
		}
		//		String[] urls = saveUrl.split(",");
		//		if (urls == null || urls.length != 3) {
		//			return;
		//		}
		String usrlName = saveUrl;
		int index = usrlName.indexOf("/", 7);
		String host = usrlName.substring(7, index);

		//		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		//		int uid = 0;
		//		if (info != null) {
		//			uid = info.uid.intValue();
		//		}

		String url = usrlName.substring(index);
		ImageName = url;

		byte[] requests = http_encode(url, host);
		PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	public void getLoadingPicShow(Activity activity, String saveUrl, long time, ILoadingPicHttp callback) {
		if (TextUtils.isEmpty(saveUrl)) {
			return;
		}

		int index = saveUrl.indexOf("/", 7);
		String host = saveUrl.substring(7, index);

		String url = saveUrl.substring(index);
		ImageName = url;

		byte[] requests = http_encode(url, host);
		PeiPeiRequest request = new PeiPeiRequest(requests, this, false, host, 80);
		this.addblack = callback;
		AppQueueManager.getInstance().addRequest(request);
	}

	public void getLoadingPicShow(Activity activity, String saveUrl, long time, int type, ILoadingPicHttp callback) {
		this.type = type;
		getLoadingPicShow(activity, saveUrl, time, callback);
	}

	@Override
	public void succuess(byte[] msg) {
		//		System.out.println("msg========" + new String(msg));
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
				String dir = SdCardUtils.getInstance().getLoadPicDir();
				if (!TextUtils.isEmpty(dir) && !TextUtils.isEmpty(ImageName)) {
					//										System.out.println("这里进来了==============="+dir);
					File dirFile = new File(dir);
					if (!dirFile.exists())
						dirFile.mkdirs();
					File[] files = dirFile.listFiles();
					if (files != null && files.length != 0) {
						for (File file : files) {
							file.delete();
						}
					}
					String saveImageName = ImageName.substring(ImageName.lastIndexOf("/"));
					saveImageName = SdCardUtils.getInstance().saveFile(tempByte, dir, saveImageName);//保存图片
					addblack.loadingPic(0, type, saveImageName);
				}
			}
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != addblack) {
			addblack.loadingPic(resultCode, type, null);
		}

	}

	public interface ILoadingPicHttp {
		public void loadingPic(int retCode, int type, String str);
	}

}
