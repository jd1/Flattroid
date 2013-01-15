package org.isobeef.jd.flattroid.fragment;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class ActivitiesFragment extends TitleFragment {
	
	protected String UserId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public String getTitle() {
		return "Activities";
	}
}
