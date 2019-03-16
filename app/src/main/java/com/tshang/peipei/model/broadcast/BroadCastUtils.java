package com.tshang.peipei.model.broadcast;

import java.util.List;

import android.app.Activity;
import android.text.Layout;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BasePhone;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.EnterBroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfoList;
import com.tshang.peipei.protocol.asn.gogirl.ShowShareBroadcastInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.vender.common.util.ListUtils;

/**
 * @Title: BroadCastUtils.java 
 *
 * @Description: 广播页面一些基本逻辑处理帮助函数
 *
 * @author Administrator  
 *
 * @date 2014年8月7日 下午6:21:51 
 *
 * @version V1.0   
 */
public class BroadCastUtils {
	/**
	 * 动态计算置顶广播的高度
	 * @author Jeff
	 *
	 * @param activity 上下文对象
	 * @param lists 置顶广播的list
	 * @return 返回控件的参数
	 */
	@SuppressWarnings("unchecked")
	public static LinearLayout.LayoutParams setTopViewParams(Activity activity, final TextView tv, List<BroadcastInfo> lists) {
		int height = 0;
		try {
			if (lists != null && lists.size() != 0) {
				for (Object object : lists) {
					StringBuffer sb = new StringBuffer();
					BroadcastInfo broadCastInfo = (BroadcastInfo) object;
					GoGirlUserInfoList userInfoList = broadCastInfo.tousers;
					if (!ListUtils.isEmpty(userInfoList)) {//处理@的用户名字
						for (int i = 0, len = userInfoList.size(); i < len; i++) {
							GoGirlUserInfo info = (GoGirlUserInfo) userInfoList.get(i);
							if (info != null) {
								String alias = SharedPreferencesTools.getInstance(activity, BAApplication.mLocalUserInfo.uid.intValue() + "_remark")
										.getAlias(info.uid.intValue());

								String userName = TextUtils.isEmpty(alias) ? new String(info.nick) : alias;
								sb.append("@").append(userName);
							}
						}
					}
					if (broadCastInfo.contenttxt != null) {
						String content = new String(broadCastInfo.contenttxt);
						sb.append(content);
						tv.setText(sb.toString());
						int tempHeight = getTextViewHeight(activity, tv);
						if (height < tempHeight) {
							height = tempHeight;
						}

					}

				}
			}
			height = height + BaseUtils.dip2px(activity, 38);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int screenHeight = BasePhone.getScreenHeight(activity) / 2;
		int minHeight = BaseUtils.dip2px(activity, 75);
		if (height < minHeight) {
			height = minHeight;
		}
		if (height > screenHeight) {//最大高度不能够大于屏幕高度的一半
			height = screenHeight;
		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
		return params;
	}

	private static int getTextViewHeight(Activity activiyt, TextView pTextView) {
		Layout layout = pTextView.getLayout();
		int line = pTextView.getLineCount();
		//		System.out.println("置顶的行数==" + line);
		int linepadding = 0;
		if (line > 1) {
			linepadding = line * BaseUtils.dip2px(activiyt, 8);
		}
		int desired = layout.getLineTop(pTextView.getLineCount());
		int padding = pTextView.getCompoundPaddingTop() + pTextView.getCompoundPaddingBottom();
		return desired + padding + linepadding;
	}

	public static GoGirlDataInfo getGoGirlDataInfo(byte[] datalist) {//解析数据
		GoGirlDataInfoList infoList = new GoGirlDataInfoList();
		BERDecoder dec = new BERDecoder(datalist);

		try {
			infoList.decode(dec);
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		if (infoList.isEmpty()) {
			return null;
		}
		return (GoGirlDataInfo) infoList.get(0);
	}

	public static FingerGuessingInfo getFingerGuessingInfo(GoGirlDataInfo datainfo) {//解析数据
		if (datainfo == null) {
			return null;
		}
		FingerGuessingInfo info = new FingerGuessingInfo();
		BERDecoder dec = new BERDecoder(datainfo.data);

		try {
			info.decode(dec);
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		return info;
	}

	public static EnterBroadcastInfo getEnterBroadcastInfo(GoGirlDataInfo datainfo) {//解析数据
		if (datainfo == null) {
			return null;
		}
		EnterBroadcastInfo info = new EnterBroadcastInfo();
		BERDecoder dec = new BERDecoder(datainfo.data);

		try {
			info.decode(dec);
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		return info;
	}

	public static ShowShareBroadcastInfo getShowShareBroadcastInfo(GoGirlDataInfo datainfo) {//解析数据
		if (datainfo == null) {
			return null;
		}
		ShowShareBroadcastInfo info = new ShowShareBroadcastInfo();
		BERDecoder dec = new BERDecoder(datainfo.data);

		try {
			info.decode(dec);
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		return info;
	}
}
