package org.isobeef.jd.flattroid.fragment;

import java.util.List;

import org.isobeef.jd.flattroid.util.JsonUtils;
import org.shredzone.flattr4j.model.Activity;

import android.os.Bundle;
import android.util.Log;

/**
 * Fragment to show {@link Activity}s.
 * @author Johannes Dilli
 *
 */
public class ActivitiesFragment extends TitleFragment {
	
	private static final String TAG = "ActivitiesFragment";
	public static final String ACTIVITIES = "activities";
	public static final String USER_ID = "userId";
	
	protected String userId;
	protected List<Activity> activities;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if ( arguments != null ) {
			activities = JsonUtils.createFromJson(arguments.getStringArrayList(ACTIVITIES), Activity.class);
		} else if (savedInstanceState != null) {
			activities = JsonUtils.createFromJson(savedInstanceState.getStringArrayList(ACTIVITIES), Activity.class);
		} else {
			throw new IllegalArgumentException("No activities!");
		}
		
		Activity a = activities.get(0);
		
		Log.v(TAG, a.getVerb() + " " + a.getTitle());
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(ACTIVITIES, JsonUtils.toJson(activities));
        outState.putString(USER_ID, userId);
    }
}
