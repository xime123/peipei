package com.tshang.peipei.activity.dialog;

import android.app.Activity;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.UserSharePreference;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.user.UserAccountBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackUserExit;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.PeiPeiDatabase;
import com.tshang.peipei.storage.database.operate.DynamicOperate;
import com.tshang.peipei.storage.database.operate.FriendOperate;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;
import com.tshang.peipei.storage.database.operate.PhotoOperate;
import com.tshang.peipei.storage.database.operate.PublishOperate;
import com.tshang.peipei.storage.database.operate.ReceiptOperate;

import de.greenrobot.event.EventBus;

/**
 * @Title: GoLogoutDialog.java 
 *
 * @Description: 退出登录
 *
 * @author allen  
 *
 * @date 2014-7-17 上午11:01:27 
 *
 * @version V1.0   
 */
public class GoLogoutDialog extends BaseNormalDialog implements BizCallBackUserExit {

	public GoLogoutDialog(Activity context, int theme, int title, int sure, int cancel) {
		super(context, theme, title, sure, cancel);
	}

	@Override
	public void OnclickSure() {
		GoGirlUserInfo userInfo = UserUtils.getUserEntity(context);
		if (userInfo == null) {
			return;
		}
		try {
			UserAccountBiz userAccountBiz = new UserAccountBiz((Activity) context);
			userAccountBiz.userExit(userInfo.auth, BAApplication.app_version_code, userInfo.uid.intValue(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		Intent intent = new Intent(context, PeipeiFloatingService.class);
//		context.stopService(intent);

		BAApplication.getInstance().closeOrOutRoom(0);

		try {
			if (BAApplication.mQQAuth != null) {
				BAApplication.mQQAuth.logout(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		BAApplication.clearShow();
		BAApplication.showBoxNum = 0;
		BAApplication.isShowBox = false;
		BAApplication.mLocalUserInfo = null;
		try {
			UserSharePreference.getInstance(context).removeUserByKey();
			NoticeEvent noticeEvent = new NoticeEvent();
			noticeEvent.setFlag(NoticeEvent.NOTICE63);
			EventBus.getDefault().post(noticeEvent);
			((Activity) context).finish();
		} catch (Exception e) {
			e.printStackTrace();
		}

		PeiPeiDatabase.closeEuDb();//关闭数据库
		PeipeiSessionOperate.closeSession();
		ReceiptOperate.closeReceipt(context);
		PublishOperate.closePublish(context);
		PhotoOperate.closePhoto(context);
		FriendOperate.closeFriend(context);
		DynamicOperate.closeSession(context);
	}

	@Override
	public void exit(int retCode) {}

}
