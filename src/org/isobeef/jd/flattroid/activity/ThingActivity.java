package org.isobeef.jd.flattroid.activity;

import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.FlattrsFetcher;
import org.isobeef.jd.flattroid.asyncTask.ImageFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.ThingFetcher;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.exception.MarshalException;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import org.isobeef.jd.flattroid.listener.ImageListener;

public class ThingActivity extends FlattrActivity implements OnFetched<Thing>{
	
	protected Button flattrBtn;
	protected TextView title;
	protected TextView text;
	protected TextView category;
	protected TextView created;
	protected ImageView image;
	protected GridView flattredBy;
	
	protected ThingFetcher fetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flattr_item);

		
		flattrBtn = (Button) findViewById(R.id.flattr);
		title = (TextView) findViewById(R.id.title);
		text = (TextView) findViewById(R.id.textView1);
		category = (TextView) findViewById(R.id.textView2);
		created = (TextView) findViewById(R.id.textView3);
		image = (ImageView) findViewById(R.id.imageView1);
		flattredBy = (GridView) findViewById(R.id.gridView1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = getIntent().getData();
		if (uri != null) {
			MyLog.d(TAG, uri.toString());
			if(fetcher != null) {
				fetcher.cancel(true);
			}
			try {
				fetcher = new ThingFetcher(service, ThingActivity.this);
				fetcher.execute(uri);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}

	@Override
	public void onFetched(Thing result) {
		thing = result;
		if(result != null) {
			title.setText(result.getTitle());
			text.setText(result.getDescription());
			category.setText(Storage.getCategoryName(result.getCategoryId(), this));
			
			created.setText(result.getCreated().toLocaleString());
			
			flattrBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					flattr(thing);
					
				}
			});
			flattrBtn.setVisibility(View.VISIBLE);
			
			
			try {
				new FlattrsFetcher(service, new FlattrsListener()).execute(Thing.withId(result.getThingId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String avatar = result.getImage();
			avatar = avatar.replace("medium", "huge");
			MyLog.d(TAG, avatar);
			new ImageFetcher(new ImageListener(image)).execute(avatar);
		}
		
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
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(FlattrException e) {
		e.printStackTrace();
		
	}
	
	class FlattrsListener implements OnFetched<List<Flattr>> {

		@Override
		public void onFetched(List<Flattr> result) {
			flattredBy.setAdapter(new GridModel(result));
			
		}

		@Override
		public ProgressDialog getProgressDialog() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onError(FlattrException e) {
			Log.e(TAG, e.getMessage());
			
		}
		
	}
	
	class GridModel extends BaseAdapter {
		
		protected List<Flattr> flattrs;
		
		public GridModel(List<Flattr> flattrs) {
			this.flattrs = flattrs;
		}
		
		@Override
		public int getCount() {
			return flattrs.size();
		}

		@Override
		public Flattr getItem(int position) {
			return flattrs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Flattr flattr = getItem(position);
			View view = convertView;
	        
			if (view == null) {
	           	LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	           	view = vi.inflate(R.layout.user_grid, null);
	        }
			
			ImageView image = (ImageView) view.findViewById(R.id.imageView1);
			TextView text = (TextView) view.findViewById(R.id.textView1);

			text.setText(flattr.getUserId());
			MyLog.d(TAG, flattr.getUserId());
			try{
				String avatar = flattr.getUser().getGravatar();
				new ImageFetcher(new ImageListener(image)).execute(avatar);
			} catch(MarshalException e) {
				Log.e(TAG, e.getMessage());
			}
			
			
			return view;
		}
		
	}
}
