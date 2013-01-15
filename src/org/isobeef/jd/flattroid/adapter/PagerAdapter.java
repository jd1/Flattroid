package org.isobeef.jd.flattroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments = new ArrayList<Fragment>();
	
	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void add(Fragment fragment) {
		fragments.add(fragment);
	}
	
	public List<Fragment> getFragments() {
		return fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
