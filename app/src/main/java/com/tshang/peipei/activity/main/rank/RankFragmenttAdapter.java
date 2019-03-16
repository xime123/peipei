package com.tshang.peipei.activity.main.rank;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * @Title: RankFtAdapter.java 
 *
 * @Description:(用一句话描述该文件做什么) 
 *
 * @author Jeff 
 *
 * @date 2014年7月29日 下午6:17:43 
 *
 * @version V1.0   
 */
public class RankFragmenttAdapter extends FragmentPagerAdapter {

	public RankFragmenttAdapter(FragmentManager fm) {
		super(fm);
	}
	
	private Fragment ft;

	@Override
	public Fragment getItem(int arg0) {
		Fragment ft = null;
		switch (arg0) {
		case 0://新人
			ft = new RankNewFragment();
			break;
		case 1://魅力
			ft = new RankFemaleFragment();
			break;
		case 2://财富
			ft = new RankMaleFragment();
			break;
		case 3://游戏
			ft = new RankGameFragment();
			break;
		}
		return ft;
	}

	@Override
	public int getCount() {
		return 4;

	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		ft = (Fragment) object;
		super.setPrimaryItem(container, position, object);
	}
	
	public Fragment getFt() {
		return ft;
	}
}
