package com.tshang.peipei.model.viewdatacache;

import java.io.File;

import android.app.Activity;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;
import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.base.BaseFile;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.viewdatacache.utils.ConfigCacheUtil;

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
public class SpaceCustomCacheViewData {
	public static final String SPACECUSTOM_PHOTO_FILE_NAME_START = "spacecustom_photo_file_name_start";
	public static final String SPACECUSTOM_SKILL_FILE_NAME_START = "spacecustom_skill_file_name_start";

	/**
	 * 
	 * @author Jeff 缓存的个人空间的相册列表
	 *
	 * @param activity 上下文
	 * @param infoList 相册列表
	 */
	public static void setPhotoInfoListCacheData(Activity activity, PhotoInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, SPACECUSTOM_PHOTO_FILE_NAME_START + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 缓存的个人空间的相册列表
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static PhotoInfoList getPhotoInfoListCacheData(Activity activity) {
		File file = ConfigCacheUtil.getCacheFile(activity, SPACECUSTOM_PHOTO_FILE_NAME_START);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				PhotoInfoList list = new PhotoInfoList();
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

	/**
	 * 
	 * @author Jeff 缓存的个人空间的相册列表
	 *
	 * @param activity 上下文
	 * @param infoList 礼物列表
	 */
	public static void setRetGGSkillInfoListCacheData(Activity activity, RetGGSkillInfoList infoList) {
		if (infoList == null || infoList.isEmpty()) {
			return;
		}
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info != null) {
			DEREncoder edc = new DEREncoder();
			try {
				infoList.encode(edc);
				byte[] data = edc.toByteArray();
				ConfigCacheUtil.setUrlCache(activity, data, SPACECUSTOM_SKILL_FILE_NAME_START + info.uid.intValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 缓存的个人空间的相册列表
	 * @author Administrator
	 *
	 * @param activity
	 * @return
	 */
	public static RetGGSkillInfoList getRetGGSkillInfoListCacheData(Activity activity) {
		File file = ConfigCacheUtil.getCacheFile(activity, SPACECUSTOM_SKILL_FILE_NAME_START);
		if (file != null) {
			byte[] contents = BaseFile.getBytesByFilePath(file);
			if (contents != null && contents.length != 0) {
				RetGGSkillInfoList list = new RetGGSkillInfoList();
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