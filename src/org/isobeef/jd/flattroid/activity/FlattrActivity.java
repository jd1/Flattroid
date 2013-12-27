package org.isobeef.jd.flattroid.activity;

import java.util.List;

import org.isobeef.jd.flattroid.asyncTask.FlattrTask;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.data.Storage;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public abstract class FlattrActivity extends ActionBarActivity {
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
		new FlattrTask(service, new FlattrListener()).execute(thing);
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
		public void onError(List<FlattrException> exceptions) {
			for(FlattrException e : exceptions) {
				Toast.makeText(FlattrActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}
}
