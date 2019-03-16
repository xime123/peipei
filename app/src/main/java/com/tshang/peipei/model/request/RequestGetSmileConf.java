package com.tshang.peipei.model.request;

import java.io.File;
import java.math.BigInteger;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetSmileConf;
import com.tshang.peipei.protocol.asn.gogirl.RspGetSmileConf;
import com.tshang.peipei.protocol.asn.gogirl.SmileConfInfo;
import com.tshang.peipei.protocol.asn.gogirl.SmileConfInfoList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.base.SdCardUtils;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/**
 * @Title: RequestAddBlacklist.java 
 *
 * @Description: 后宫语音配置文件
 *
 * @author jeff 
 *
 * @date 2014-12-8 下午19:59:54 
 *
 * @version V1.6.0   
 */
public class RequestGetSmileConf extends AsnBase implements ISocketMsgCallBack {

	public void getSmileConf(byte[] auth, int ver, int confver, int uid, int fromos) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqGetSmileConf req = new ReqGetSmileConf();
		req.appver = BigInteger.valueOf(ver);
		req.confver = BigInteger.valueOf(confver);
		req.fromos = BigInteger.valueOf(fromos);
		req.uid = BigInteger.valueOf(uid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETSMILECONF_CID;
		goGirlPkt.reqgetsmileconf = req;
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

		RspGetSmileConf rsp = pkt.rspgetsmileconf;
		int retCode = rsp.retcode.intValue();
		SmileConfInfoList list = rsp.conflist;
		if (checkRetCode(retCode)) {
			if (list != null) {
				for (Object object : list) {
					SmileConfInfo info = (SmileConfInfo) object;
					String smiledId = new String(info.id);
					String dir = SdCardUtils.getInstance().getHaremVoiceDir(smiledId);
					String fileName = new String(info.voicefile);
					File dirFile = new File(dir);
					if (!dirFile.exists())
						dirFile.mkdirs();
					File file = new File(dir + "/" + fileName);

					if (!file.exists()) {//如果文件不存在就下载语音文件
						RequestGetSmileVoice req = new RequestGetSmileVoice();
						req.getSmileVoice("".getBytes(), BAApplication.app_version_code, smiledId, fileName);

					}
				}
			}
		}

	}

	@Override
	public void error(int resultCode) {

	}

}
