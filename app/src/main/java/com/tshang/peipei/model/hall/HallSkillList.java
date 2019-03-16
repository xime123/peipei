package com.tshang.peipei.model.hall;

import android.app.Activity;

import com.tshang.peipei.protocol.asn.gogirl.RetGGSkillInfoList;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;

/**
 * @Title: HallOnTimeFemale.java 
 *
 * @Description: 女性大厅新人榜
 *
 * @author Jeff  
 *
 * @date 2014年10月10日 上午11:21:53 
 *
 * @version V1.3.0   
 */
public class HallSkillList extends HallBase {
	private boolean isRefresh = true;//区分是刷新状态还是夹在更多状态
	private int startLoad = 0;//加载位置

	public int getStartLoad() {
		return startLoad;
	}

	public void setStartLoad(int startLoad) {
		this.startLoad = startLoad;
	}

	public HallSkillList(Activity activity, BAHandler mHandler) {
		super(activity);
		this.mHandler = mHandler;
	}

	public void getSkillList(boolean isRefresh, int type) {//女性日榜
		this.isRefresh = isRefresh;
		if (isRefresh) {
			startLoad = 0;
		}
		getSkillList(startLoad, type);//加载技能列表
	}

	@Override
	public void getHallSkillListCallBack(int retcode, int isEnd, RetGGSkillInfoList list) {
		if (retcode == 0) {//网络请求数据成功回调
			startLoad += LOADCOUNT;
			if (isRefresh) {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_SKILL_LIST_SUCCESS_VALUE, isEnd, REFRESH_CODE, list));//通过handler把数据传到UI界面去更新
			} else {
				mHandler.sendMessage(mHandler.obtainMessage(HandlerValue.HALL_GET_SKILL_LIST_SUCCESS_VALUE, isEnd, LOADMORE_CODE, list));
			}
		} else {
			mHandler.sendEmptyMessage(HandlerValue.HALL_GET_SKILL_LIST_FAILED_VALUE);//加载失败处理
		}
	}

}
