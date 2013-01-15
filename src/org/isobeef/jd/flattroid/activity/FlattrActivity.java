package org.isobeef.jd.flattroid.activity;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.FlattrTask;
import org.isobeef.jd.flattroid.asyncTask.ImageFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.ThingFetcher;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.flattrApi.FlattrOAuthHelper;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Thing;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class FlattrActivity extends SherlockFragmentActivity{
	protected static final String TAG = "FlattrActivity";
	
	protected FlattrService service;
	protected Thing thing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		service = Storage.getService(this);
		if(service == null) {
			Intent in = new Intent(this, Register.class);
			startActivity(in);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(service == null) {
			service = Storage.getService(this);
		}
	}
	
	protected void share(String link) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		//i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
		i.putExtra(Intent.EXTRA_TEXT, link);
		startActivity(Intent.createChooser(i, "Share URL"));
	}
	
	protected void flattr(Thing thing) {
		try {
			new FlattrTask(service, new FlattrListener()).execute(thing);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class FlattrListener implements OnFetched<Boolean> {
		@Override
		public void onFetched(Boolean result) {
			if(result) {
				Toast.makeText(FlattrActivity.this, "You flattred " + thing.getTitle(), Toast.LENGTH_SHORT).show();
			}
			
		}
		
		@Override
		public ProgressDialog getProgressDialog() {
			return new ProgressDialog(FlattrActivity.this);
		}

		@Override
		public void onError(FlattrException e) {
			Toast.makeText(FlattrActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			
		}
	}
}
