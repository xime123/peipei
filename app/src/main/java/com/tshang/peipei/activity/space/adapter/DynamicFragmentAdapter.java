package com.tshang.peipei.activity.space.adapter;

import com.tshang.peipei.activity.main.message.DynamicFragmentAbout;
import com.tshang.peipei.activity.main.message.DynamicFragmentAll;
import com.tshang.peipei.activity.main.message.DynamicFragmentMe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @Title: DynamicFragmentAdapter.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-10-16 下午5:32:20 
 *
 * @version V1.0   
 */
public class DynamicFragmentAdapter extends FragmentPagerAdapter {

	public DynamicFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment ft = null;
		switch (arg0) {
		case 0://全部动态
			ft = new DynamicFragmentAll();
			break;
		case 1://我的动态
			ft = new DynamicFragmentMe();
			break;
		case 2://关于我
			ft = new DynamicFragmentAbout();
			break;
		}
		return ft;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
