package org.isobeef.jd.flattroid.activity;

import java.util.EnumSet;
import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.TokenFetcher;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.flattrApi.FlattrKeySecret;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.AndroidAuthenticator;
import org.shredzone.flattr4j.oauth.Scope;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Register extends ActionBarActivity implements OnFetched<AccessToken> {
	private static final String HOST="org.isobeef.jd.flattroid";
	private static final String TAG = Register.class.getName(); 
	
	private Button authBtn;
	private AndroidAuthenticator auth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		auth = new AndroidAuthenticator(HOST, new FlattrKeySecret());
		setContentView(R.layout.register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setTitle(R.string.authenticate);
		
		authBtn = (Button) findViewById(R.id.btn_auth);
		authBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAuthActivity();

			}
		});
	}
	
	private void startAuthActivity() {
		auth.setScope(EnumSet.of(Scope.FLATTR, Scope.EXTENDEDREAD));
		try {
			Intent intent = auth.createAuthenticateIntent();
			startActivity(intent);
		} catch (FlattrException e) {
			Log.e(TAG, "Unable to authenticate", e);
			Toast.makeText(this, "Unable to authenticate", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return false;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = getIntent().getData();
		if (uri != null) {
			MyLog.d(TAG, "Got callback: {0}", uri.toString());
			new TokenFetcher(auth, uri, this).execute();
		}
	}
	
	@Override
	public void onFetched(AccessToken token) {
		MyLog.d(TAG, "token Fetched {0}", token.getToken());
		Storage.storeFlattrToken(token, this);
		if(Storage.getFlattrToken(this) == null) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Authentication complete!", Toast.LENGTH_LONG).show();
			authBtn.setEnabled(false);
            this.finish();
            Intent i=new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
		}
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(this);
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		MyLog.logExceptions(TAG, "Authentication error", exceptions);
	}
}
