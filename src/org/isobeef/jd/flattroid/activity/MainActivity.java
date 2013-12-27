package org.isobeef.jd.flattroid.activity;

import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.data.Storage;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnFetched<User> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FlattrService service = Storage.getService(this);
		if(service == null) {
			Intent in = new Intent(this, Register.class);
			startActivity(in);
		} else {
			//new UserFetcher(service, this).execute();
		}
		Button b1 = (Button) findViewById(R.id.button1);
		Button b2 = (Button) findViewById(R.id.button2);

		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(MainActivity.this, ThingActivity.class);
				Uri uri = Uri.parse("https://flattr.com/thing/1039966/NSFW059-Der-Titel-dieser-Sendung");
				in.setData(uri);
				startActivity(in);
				
			}
		});
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(MainActivity.this, UserActivity.class);
				Uri uri = Uri.parse("https://flattr.com/profile/timpritlove");
				in.setData(uri);
				startActivity(in);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_catalog:
			Intent inC = new Intent(MainActivity.this, CatalogActivity.class);
			startActivity(inC);
			return true;
		
		case R.id.about:	
			Intent inA = new Intent(this, WebViewActivity.class);
			inA.putExtra("url", "file:///android_asset/about.html");
			startActivity(inA);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onFetched(User user) {
		if(user != null) {
			TextView view = (TextView) findViewById(R.id.textview1);
			view.setText(user.getFirstname() + " " + user.getLastname() + " " + user.getEmail());
		}
		
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		for(FlattrException e : exceptions) {
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

}
