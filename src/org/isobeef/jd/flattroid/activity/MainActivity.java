package org.isobeef.jd.flattroid.activity;

import java.net.URI;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.R.id;
import org.isobeef.jd.flattroid.R.layout;
import org.isobeef.jd.flattroid.R.menu;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.UserFetcher;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.fragment.UserFragment;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnFetched<User> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FlattrService service = Storage.getService(this);
		if(service == null) {
			Intent in = new Intent(this, Register.class);
			startActivity(in);
		} else {
			new UserFetcher(service, this).execute();
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
				Uri uri = Uri.parse("https://flattr.com/profile/Schmidtlepp");
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
	public void onError(FlattrException e) {
		e.printStackTrace();
		
	}

}
