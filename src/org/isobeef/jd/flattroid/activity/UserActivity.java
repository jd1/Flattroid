package org.isobeef.jd.flattroid.activity;

import java.util.ArrayList;
import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.adapter.PagerAdapter;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.UserActivityData;
import org.isobeef.jd.flattroid.asyncTask.UserDataFetcher;
import org.isobeef.jd.flattroid.fragment.ActivitiesFragment;
import org.isobeef.jd.flattroid.fragment.ThingFragment;
import org.isobeef.jd.flattroid.fragment.TitleFragment;
import org.isobeef.jd.flattroid.fragment.UserFragment;
import org.isobeef.jd.flattroid.util.JsonUtils;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity to show details about a user, his flattr things and activities.
 * @author Johannes Dilli
 *
 */
public class UserActivity extends FlattrActivity implements OnFetched<UserActivityData>, TabListener {
	private static final String FRAGMENTS = "fragments";

	public static String USER_ID = "userId";
	
	protected ActionBar mActionBar;
	private PagerAdapter adapter;
	protected ViewPager viewPager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_viewpager);
		adapter = new PagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);
		
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //indicator.setOnPageChangeListener(pageChangeListener);
		
		
		if(savedInstanceState != null) {
			
        	String[] frags = savedInstanceState.getStringArray(FRAGMENTS);
        	for(String tag : frags) {
        		TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag(tag);
        		if(fragment == null) {
        			Log.e(TAG, "fragment null " + tag);
        		} else {
        			adapter.add(fragment);
        		}
        	}
        } else {
        	Uri uri = getIntent().getData();
    		if (uri != null) {
    			MyLog.d(TAG, uri.toString());
    			if(uri.getPathSegments().size() >= 2) {
    				UserId id = User.withId(uri.getPathSegments().get(1));
    				new UserDataFetcher(service, UserActivity.this).execute(id);
    			}
    			
    			
    		}
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_thing, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.flattr:	
			flattr(thing);
			return true;
		case R.id.share:
			share(thing.getLink());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onFetched(UserActivityData result) {
		List<Activity> incoming = result.getIncoming();
		List<Activity> outgoing = result.getOutgoing();
		List<Flattr> flattrs = result.getFlattrs();
		List<Thing> things = result.getThings();
		User user = result.getUser();
		
		if(user != null) {
			mActionBar.setTitle(user.getFirstname() + " " + user.getLastname());
			addUser(user);
		}
		
		if(things != null && !things.isEmpty()) {
			addThings(things, "Things");
		}
		
		if(flattrs != null) {
			List<Thing> flattrThings = new ArrayList<Thing>();
			for(Flattr flattr : flattrs) {
				flattrThings.add(flattr.getThing());
			}
			addThings(flattrThings, "Flattrs");
		}
		
		if(incoming != null && !incoming.isEmpty()) {
			addActivities(incoming, "Incoming");
		}
		
		if(outgoing != null && !outgoing.isEmpty()) {
			addActivities(outgoing, "Outgoing");
		}
	}
	
	private void addUser(User user) {
		UserFragment fragment = new UserFragment();
		Bundle bundle = new Bundle();
		bundle.putString(UserFragment.USER, user.toJSON());
		fragment.setArguments(bundle);
		addTab(fragment);
		Log.d(TAG, "added user fragment.");
	}
	
	private void addThings(List<Thing> things, String title) {
		ThingFragment fragment = new ThingFragment();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(ThingFragment.FLATTRS, JsonUtils.toJson(things));
		bundle.putString(ThingFragment.TITLE, "Flattrs");
		fragment.setArguments(bundle);
		addTab(fragment);
		Log.d(TAG, "added thing fragment for " + title);
	}
	
	private void addActivities(List<Activity> activities, String title) {
		ActivitiesFragment fragment = new ActivitiesFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ActivitiesFragment.TITLE, title);
		bundle.putStringArrayList(ActivitiesFragment.ACTIVITIES, JsonUtils.toJson(activities));
		fragment.setArguments(bundle);
		addTab(fragment);
		Log.d(TAG, "added activities fragment for " + title);
	}
	
	private void addTab(TitleFragment fragment) {
		adapter.add(fragment);
		Tab tab = mActionBar.newTab();
		tab.setText(fragment.getTitle());
		tab.setTabListener(this);
		mActionBar.addTab(tab);
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		Toast.makeText(this, "Failed to load user data", Toast.LENGTH_LONG).show();
		for(FlattrException e : exceptions) {
			Log.e(TAG, "Failed to load user data", e);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	  
		String[] fragments = new String[adapter.getFragments().size()];
		int counter = 0;//adapter.getFragments().size() -1;
		for(Fragment frag : adapter.getFragments()) {
			MyLog.d(TAG, "save " + frag);
			fragments[counter] = frag.getTag();
			counter++;
		}
		savedInstanceState.putStringArray(FRAGMENTS, fragments);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
}
