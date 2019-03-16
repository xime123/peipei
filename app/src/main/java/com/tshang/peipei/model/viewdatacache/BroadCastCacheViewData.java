package com.tshang.peipei.model.viewdatacache;

import java.io.File;

import android.app.Activity;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;
import com.tshang.peipei.network.socket.ThreadPoolService;

/**
 * @Title: GiftListCacheViewData.java 
 *
 * @Description: TODO缓存送礼界面数据
 *
 * @author Jeff  
 *
 * @date 2014年10月11日 下午3:13:32 
 *
 * @version V1.3.0   
 */
public class BroadCastCacheViewData {
	public static final String BRAODCAST_TOP_FEMALE_FILE_NAME_START = "BroadcastInfoListTop";
	public static final String BRAODCAST_ALL_MALE_FILE_NAME_START = "BroadcastInfoListAll";
	public static final String BRAODCAST_SYSTEM_FILE_NAME_START = "BroadcastInfoListSystem";
	public static final String BROADCAST_GAME_FILE_NAME_START = "BroadcastInfoListGame";

	/**
	 * 
	 * @author Jeff 缓存送礼界面数据
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setBroadcastInfoListCacheData(final Activity activity, final String fileNameStart, final BroadcastInfoList infoList) {
		ThreadPoolService.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				if (infoList == null || infoList.isEmpty()) {
					return;
				}
				GoGirlUserInfo info = UserUtils.getUserEntity(activity);
				if (info != null) {
					DEREncoder edc = new DEREncoder();
					try {
						infoList.encode(edc);
						byte[] data = edc.toByteArray();
						ConfigCacheUtil.setUrlCache(activity, data, fileNameStart + info.uid.intValue());
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			}
		});

	}

	/**
	 * 缓存的礼物数据
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static BroadcastInfoList getBroadcastInfoListCacheData(Activity activity, String fileNameStart) {
		File file = ConfigCacheUtil.getCacheFile(activity, fileNameStart);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				BroadcastInfoList list = new BroadcastInfoList();
				BERDecoder dec = new BERDecoder(contents);
				try {
					list.decode(dec);
					return list;
				} catch (ASN1Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

}
