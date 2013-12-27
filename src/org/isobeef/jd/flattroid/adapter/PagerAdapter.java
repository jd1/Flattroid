package org.isobeef.jd.flattroid.adapter;

import java.util.ArrayList;
import java.util.List;

import org.isobeef.jd.flattroid.fragment.TitleFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class PagerAdapter extends FragmentPagerAdapter {

	List<TitleFragment> fragments = new ArrayList<TitleFragment>();
	FragmentManager fm;
	
	public PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}
	
	public void add(TitleFragment fragment) {
		fragments.add(fragment);
		FragmentTransaction trans = fm.beginTransaction();
		trans.add(fragment.getId() , fragment);
		trans.commit();
	}
	
	public List<TitleFragment> getFragments() {
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
	
	@Override
    public CharSequence getPageTitle(int position) {
		return fragments.get(position).getTitle();
    }
}
