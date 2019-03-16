package com.tshang.peipei.model.request;

import java.io.File;

import android.text.TextUtils;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSmileVoice;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSmileVoice;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 后宫语音下载
 *
 * @author jeff 
 *
 * @date 2014-12-8 下午19:59:54 
 *
 * @version V1.6.0   
 */
public class RequestGetSmileVoice extends AsnBase implements ISocketMsgCallBack {
	private String fileName;

	public void getSmileVoice(byte[] auth, int ver, String smileid, String fileName) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSmileVoice req = new ReqGetSmileVoice();
		req.smileid = smileid.getBytes();
		this.fileName = fileName;

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSMILEVOICE_CID;
		goGirlPkt.reqgetsmilevoice = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;

		RspGetSmileVoice rsp = pkt.rspgetsmilevoice;
		int retCode = rsp.retcode.intValue();
		if (checkRetCode(retCode)) {//下载成功了把以前的删除掉
			String smileId = new String(rsp.smileid);
			String dir = SdCardUtils.getInstance().getHaremVoiceDir(smileId);
			if (!TextUtils.isEmpty(dir)) {
				File dirFile = new File(dir);
				if (dirFile != null && dirFile.exists()) {
					File[] files = dirFile.listFiles();
					if (files != null && files.length != 0) {
						for (File file : files) {
							file.delete();
						}
					}
				}
				SdCardUtils.getInstance().saveFile(rsp.voice, dir, fileName);//保存语音
			}
		}

	}

	@Override
	public void error(int resultCode) {

	}

}
