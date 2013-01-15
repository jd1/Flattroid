package org.isobeef.jd.flattroid.activity;

import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.adapter.PagerAdapter;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.UserActivityData;
import org.isobeef.jd.flattroid.asyncTask.UserDataFetcher;
import org.isobeef.jd.flattroid.fragment.FlattrsFragment;
import org.isobeef.jd.flattroid.fragment.TitleFragment;
import org.isobeef.jd.flattroid.fragment.UserFragment;
import org.isobeef.jd.flattroid.listener.CustomTabListener;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class UserActivity extends FlattrActivity implements OnFetched<UserActivityData> {
	private static final String FRAGMENTS = "fragments";

	public static String USER_ID = "userId";
	
	protected ActionBar mActionBar;
	private ViewPager viewPager;
	private PagerAdapter adapter;
	protected TabListener tabListener;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new PagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		tabListener = new CustomTabListener(viewPager);
		
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionBar.setSelectedNavigationItem(position);
            }
        };
        viewPager.setOnPageChangeListener(pageChangeListener);
		
		if(savedInstanceState != null) {
        	String[] frags = savedInstanceState.getStringArray(FRAGMENTS);
        	for(String tag : frags) {
        		TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag(tag);
        		if(fragment == null) {
        			Log.e(TAG, "fragment null " + tag);
        		} else {
        			add(fragment);
        		}
        	}
        } else {
        	Uri uri = getIntent().getData();
    		if (uri != null) {
    			MyLog.d(TAG, uri.toString());
    			if(uri.getPathSegments().size() >= 2) {
    				UserId id = User.withId(uri.getPathSegments().get(1));
    				try {
    					new UserDataFetcher(service, UserActivity.this).execute(id);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    			
    			
    		}
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
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
		User user = result.getUser();
		if(user != null) {
			UserFragment fragment = new UserFragment(user);
			add(fragment);
		}
		if(flattrs != null) {
			FlattrsFragment fragment = new FlattrsFragment(flattrs);
			add(fragment);
		}
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(FlattrException e) {
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		e.printStackTrace();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	  
		String[] fragments = new String[adapter.getFragments().size()];
		int counter = adapter.getFragments().size() -1;
		for(Fragment frag : adapter.getFragments()) {
			MyLog.d(TAG, "save " + frag);
			fragments[counter] = frag.getTag();
			counter--;
		}
		savedInstanceState.putStringArray(FRAGMENTS, fragments);
	}
	
	private void add(TitleFragment fragment) {
		adapter.add(fragment);
		Tab tab = mActionBar.newTab()
				.setText(fragment.getTitle())
				.setTabListener(tabListener);
		mActionBar.addTab(tab);
	}
}
