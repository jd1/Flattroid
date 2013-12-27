package org.isobeef.jd.flattroid.activity;

import java.util.ArrayList;
import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.FlattrsFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.ThingFetcher;
import org.isobeef.jd.flattroid.asyncTask.UserFetcher;
import org.isobeef.jd.flattroid.data.Holder;
import org.isobeef.jd.flattroid.data.ImageDisplayer;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.data.StringImageBundle;
import org.isobeef.jd.flattroid.listener.ImageListener;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ThingActivity extends FlattrActivity implements OnFetched<Thing>{
	protected static final String TAG = "ThingActivity";
	
	protected Button flattrBtn;
	protected TextView titleView;
	protected TextView textView;
	protected TextView categoryView;
	protected TextView createdView;
	protected ImageView image;
	protected GridLayout flattredBy;
	
	protected ThingFetcher fetcher;
	
	protected static final String TITLE = "title";
	protected String title;
	
	protected static final String TEXT = "text";
	protected String text;
	
	protected static final String CATEGORY = "category";
	protected String category;
	
	protected static final String CREATED = "created";
	protected String created;
	
	protected static final String AVATAR = "avatar";
	protected String avatar;
			
	protected static final String USERS = "users";
	protected ArrayList<StringImageBundle> users;
	
	protected boolean hasData = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flattr_item);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		flattrBtn = (Button) findViewById(R.id.flattr);
		titleView = (TextView) findViewById(R.id.title);
		textView = (TextView) findViewById(R.id.textView1);
		categoryView = (TextView) findViewById(R.id.textView2);
		createdView = (TextView) findViewById(R.id.textView3);
		image = (ImageView) findViewById(R.id.imageView1);
		flattredBy = (GridLayout) findViewById(R.id.gridLayout);
		
		if(savedInstanceState != null) {
			title = savedInstanceState.getString(TITLE);
			text = savedInstanceState.getString(TEXT);
			category = savedInstanceState.getString(CATEGORY);
			created = savedInstanceState.getString(CREATED);
			avatar = savedInstanceState.getString(AVATAR);
			users = savedInstanceState.getParcelableArrayList(USERS);
			hasData = true;
		} else {
			users = new ArrayList<StringImageBundle>();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(hasData) {
			displayBasicContent();
			for(StringImageBundle bundle : users) {
				displayUser(bundle);
			}
		} else {
			Uri uri = getIntent().getData();
			if (uri != null) {
				MyLog.d(TAG, uri.toString());
				if(fetcher != null) {
					fetcher.cancel(true);
				}
				fetcher = new ThingFetcher(service, ThingActivity.this);
				fetcher.execute(uri);
			}
		}
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
		outState.putString(TITLE, title);
		outState.putString(TEXT, text);
		outState.putString(CATEGORY, category);
		outState.putString(CREATED, created);
		outState.putString(AVATAR, avatar);
		outState.putParcelableArrayList(USERS, users);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onFetched(Thing result) {
		thing = result;
		if(result != null) {
			title = result.getTitle();
			text = result.getDescription();
			category = Storage.getCategoryName(result.getCategoryId(), this);
			created = result.getCreated().toLocaleString();
			avatar = result.getImage().replace("medium", "huge");
			
			displayBasicContent();
			
			new FlattrsFetcher(service, new FlattrsListener()).execute(Thing.withId(result.getThingId()));
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
		Log.d(TAG, "menu btn");
		switch (item.getItemId()) {
		case android.R.id.home:
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
            	finish();
            }
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
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		for(FlattrException e : exceptions) {
			e.printStackTrace();
		}
	}
	
	private void displayBasicContent() {
		MyLog.d(TAG, "display stuff");
		getSupportActionBar().setTitle(title);
		titleView.setText(title);
		textView.setText(text);
		categoryView.setText(category);
		
		createdView.setText(created);
		
		flattrBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				flattr(thing);
				
			}
		});
		flattrBtn.setVisibility(View.VISIBLE);
		Holder.getImageLoader(this).displayImage(avatar, image);
	}
	
	private void displayUser(StringImageBundle bundle) {
        
		LayoutInflater vi = (LayoutInflater) flattredBy.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.user_grid, null);
		
		ImageView image = (ImageView) view.findViewById(R.id.imageView1);
		TextView text = (TextView) view.findViewById(R.id.textView1);

		text.setText(bundle.getStringItem());
		if(bundle.getBitmap() == null) {
			MyLog.d(TAG, "load Image");
			Holder.getImageLoader(ThingActivity.this).loadImage(this, bundle.getAvatar(), new ImageListener(bundle, image));
		} else {
			MyLog.d(TAG, "image found");
			new ImageDisplayer().display(bundle.getBitmap(), image);
		}
		
		
		flattredBy.addView(view);
	}
	
	class FlattrsListener implements OnFetched<List<Flattr>> {

		@Override
		public void onFetched(List<Flattr> result) {
			for(Flattr flattr : result) {
				new UserFetcher(service, new UserListener()).execute(flattr);
			}
		}

		@Override
		public ProgressDialog getProgressDialog() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onError(List<FlattrException> exceptions) {
			for(FlattrException e : exceptions) {
				e.printStackTrace();
			}
		}
		
	}
	
	class UserListener implements OnFetched<User> {

		@Override
		public void onFetched(User result) {
			String avatar = result.getGravatar();
			avatar = avatar.replace("small", "medium");
			MyLog.d(TAG, avatar);
			StringImageBundle bundle = new StringImageBundle(result.getUserId(), avatar);
			users.add(bundle);
			displayUser(bundle);
		}

		@Override
		public ProgressDialog getProgressDialog() {
			return null;
		}

		@Override
		public void onError(List<FlattrException> exceptions) {
			for(FlattrException e : exceptions) {
				e.printStackTrace();
			}
		}
	}

}
