package com.tshang.peipei.activity.reward.adapter;

import com.tshang.peipei.activity.reward.JoinFragment;
import com.tshang.peipei.activity.reward.PulishFragment;
import com.tshang.peipei.activity.reward.WinFragment;
import com.tshang.peipei.base.babase.BAConstants.Gender;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @Title: MineFragmentViewPagerAdapter.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-9-29 下午4:07:43 
 *
 * @version V1.0   
 */
public class MineFragmentViewPagerAdapter extends FragmentPagerAdapter {

	private int anonymNickId;

	public MineFragmentViewPagerAdapter(FragmentManager fm, int anonymNickId) {
		super(fm);
		this.anonymNickId = anonymNickId;
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		switch (arg0) {
		case 0:
			fragment = new JoinFragment(anonymNickId);
			break;
		case 1:
			fragment = new PulishFragment(anonymNickId);
			break;
		case 2:
			fragment = new WinFragment(anonymNickId);
			break;

		default:
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
