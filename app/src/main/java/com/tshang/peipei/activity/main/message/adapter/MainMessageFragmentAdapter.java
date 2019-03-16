package com.tshang.peipei.activity.main.message.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tshang.peipei.activity.main.message.DynamicFragment;
import com.tshang.peipei.activity.main.message.DynamicFragmentManage;
import com.tshang.peipei.activity.main.message.FriendsFragment;
import com.tshang.peipei.activity.main.message.MessageFragment;

/**
 * @Title: HallFtAdapter.java 
 *
 * @Description:(用一句话描述该文件做什么) 消息主界面
 *
 * @author Jeff 
 *
 * @date 2014年12月18日 上午10:22:13 
 *
 * @version V2.0   
 */
public class MainMessageFragmentAdapter extends FragmentPagerAdapter {

	public MainMessageFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment ft = null;
		switch (arg0) {
		case 0://消息
			ft = new MessageFragment();
			break;
		case 1://好友
			ft = new FriendsFragment();
			break;
		}
		return ft;
	}

	@Override
	public int getCount() {
		return 2;

	}

}
