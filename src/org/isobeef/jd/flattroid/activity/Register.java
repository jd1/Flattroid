package org.isobeef.jd.flattroid.activity;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.flattrApi.FlattrOAuthHelper;
import org.shredzone.flattr4j.exception.FlattrException;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Register extends ActionBarActivity {
	private Button authBtn;
	private FlattrOAuthHelper builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		builder = new FlattrOAuthHelper(this);
		setContentView(R.layout.register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		authBtn = (Button) findViewById(R.id.btn_auth);
		authBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					builder.startAuthActivity();
				} catch (FlattrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			return false;
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = getIntent().getData();
		if (uri != null) {
			builder.handleCallback(uri);
		}
	}
	
	public void done() {
		if(Storage.getFlattrToken(this) == null) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Authentication complete!", Toast.LENGTH_LONG).show();
			authBtn.setEnabled(false);
		}
	}
}
