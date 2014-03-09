package org.isobeef.jd.flattroid.flattrApi;

import java.util.EnumSet;
import java.util.List;

import org.isobeef.jd.flattroid.activity.Register;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.TokenFetcher;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.AndroidAuthenticator;
import org.shredzone.flattr4j.oauth.ConsumerKey;
import org.shredzone.flattr4j.oauth.Scope;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

public class FlattrOAuthHelper implements OnFetched<AccessToken>{
	public static final String HOST="org.isobeef.jd.flattroid";
	private static final String TAG = "FlattrOAuthHelper";
	
	private ConsumerKey foa;
	private AndroidAuthenticator auth;
	private Register register;
	
	public FlattrOAuthHelper(Register register) {
		foa = new FlattrKeySecret();
		auth = new AndroidAuthenticator(HOST, foa);
		this.register = register;
	}
	
	public void startAuthActivity() throws FlattrException {
		
		auth.setScope(EnumSet.of(Scope.FLATTR, Scope.EXTENDEDREAD));
		Intent intent = auth.createAuthenticateIntent();
		register.startActivity(intent);
	}
	
	public void handleCallback(Uri uri) {
		new TokenFetcher(auth, uri, this).execute();
	}
	
	@Override
	public void onFetched(AccessToken token) {
		MyLog.d(TAG, "token Fetched");
		Storage.storeFlattrToken(token, register);
		register.done();
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(register);
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		// TODO Auto-generated method stub
		
	}
}
