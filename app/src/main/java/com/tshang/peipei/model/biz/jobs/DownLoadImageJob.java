package com.tshang.peipei.model.biz.jobs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.Context;
import android.text.TextUtils;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: DownLoadImageJob.java 
 *
 * @Description: 下载图片并保持，用于私聊保持图片用 
 *
 * @author allen  
 *
 * @date 2014-4-21 下午7:38:43 
 *
 * @version V1.0   
 */
public class DownLoadImageJob extends Job {

	private static final int PRIORITY = 1;
	private String key;
	private Context mContext;
	private int fuid;
	private long loaclid;
	private int type;

	protected DownLoadImageJob() {
		super(new Params(PRIORITY).setRequiresNetwork(true));

	}

	public DownLoadImageJob(Context context, String key, int dbfriend, long loaclid, int sex, int type) {
		this();
		this.mContext = context;
		this.key = key;
		this.fuid = dbfriend;
		this.loaclid = loaclid;
		this.type = type;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void onAdded() {

	}

	@Override
	protected void onCancel() {

	}

	@Override
	public void onRun() throws Throwable {
		if (!TextUtils.isEmpty(key)) {
			byte[] returnData = null;
			Socket socket = new Socket();
			String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
			if (BAConstants.IS_TEST) {
				host = BAConstants.PEIPEI_SERVER_HOST_TEST;
			}
			InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
			try {
				socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
			ReqGoGirlDownloadPic req = new ReqGoGirlDownloadPic();

			req.key = key.getBytes();
			req.width = BigInteger.valueOf(0);
			req.height = BigInteger.valueOf(0);
			req.sincemodtime = BigInteger.valueOf(0);

			// 整合成完整消息体
			GoGirlPkt goGirlPkt = new GoGirlPkt();
			goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLDOWNLOADPIC_CID;
			goGirlPkt.reqgogirldownloadpic = req;

			PKTS body = new PKTS();
			body.choiceId = PKTS.GOGIRLPKT_CID;
			body.gogirlpkt = goGirlPkt;
			ydmxMsg.body = body;
			byte[] msg = AsnBase.encode(ydmxMsg);

			OutputStream os = null;
			InputStream is = null;
			try {
				if (socket.isOutputShutdown() || socket.isInputShutdown()) {
					return;
				}
				os = socket.getOutputStream();
				is = socket.getInputStream();
				// 发送请求msg
				os.write(msg);
				os.flush();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				int ret = -1;
				byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
				byte[] bb = new WeakReference<byte[]>(new byte[0]).get();
				while ((len = is.read(b, 0, b.length)) != -1) {
					baos.write(b, 0, len);
					bb = baos.toByteArray();
					ret = AsnProtocolTools.is_pkg_complete(bb);
					if (ret > 0) {
						break;
					} else if (ret == 0) {
						bb = null;
						continue;
					} else {
						break;
					}
				}
				if (len < 0 && ret <= 0) {
					return;
				}

				YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
				GoGirlPkt pkt = resultPacket.body.gogirlpkt;

				RspGoGirlDownloadPic rsp = pkt.rspgogirldownloadpic;

				returnData = rsp.picdata;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != os) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					os = null;
				}
				closeInputStream(is);
				if (null != socket) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (returnData != null) {//数据下载成功
				ChatRecordBiz.saveFile(mContext, fuid, loaclid, returnData, true);
			} else {//下载失败
				ChatOperate chatDatabase = null;
				if (type == 0) {
					chatDatabase = ChatOperate.getInstance(BAApplication.getInstance().getApplicationContext(), fuid, false);
				} else if (type == 1) {
					chatDatabase = ChatOperate.getInstance(BAApplication.getInstance().getApplicationContext(), fuid, true);
				}
				if (chatDatabase != null)
					chatDatabase.delete(loaclid);
			}
		}

	}

	private void closeInputStream(InputStream is) {

		try {
			if (is != null) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		}
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		return false;
	}

}
