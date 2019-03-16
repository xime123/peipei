package com.tshang.peipei.model.biz.space;

import android.app.Activity;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestDelSkill;
import com.tshang.peipei.model.request.RequestDelSkill.IDelSKill;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;

/**
 *  AddSkillBiz.java 
 *
 * (用一句话描述该文件做什么) 删除技能逻辑
 *
 * @author Jeff  
 *
 * @date 2014年7月11日 下午5:37:56 
 *
 * @version V1.0   
 */
public class DeleteSkillBiz {
	private Activity activity;

	public DeleteSkillBiz(Activity activity) {
		this.activity = activity;
	}

	public void deleteSkillBiz(int skillid, IDelSKill callBack) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		BaseUtils.showDialog(activity, R.string.str_deleting);
		RequestDelSkill requestDelSkill = new RequestDelSkill();
		requestDelSkill.delSkill(userEntity.auth, BAApplication.app_version_code, skillid, userEntity.uid.intValue(), callBack);
	}

}
