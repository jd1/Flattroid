package org.isobeef.jd.flattroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class TitleFragment extends Fragment {
	
	public static final String TITLE = "TITLE";
	private String title;
	
	public final String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if ( arguments != null ) {
			title = arguments.getString(TITLE);
		} else if (savedInstanceState != null) {
			title = savedInstanceState.getString(TITLE);
		} else {
			throw new IllegalArgumentException("No title!");
		}
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, title);
    }
}
