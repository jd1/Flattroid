package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.oauth.AccessToken;
import org.shredzone.flattr4j.oauth.AndroidAuthenticator;

import android.net.Uri;

public class TokenFetcher extends FetcherAsyncTask<Void, Void, AccessToken> {

	
	protected AndroidAuthenticator auth;
	protected Uri uri;
	
	public TokenFetcher(AndroidAuthenticator auth, Uri uri, OnFetched<AccessToken> listener) {
		super(listener);
		this.auth = auth;
		this.uri = uri;
	}
	
	@Override
	protected AccessToken doInBackground(Void... arg0) {
		AccessToken token = null;
		try {
			token = auth.fetchAccessToken(uri);
		} catch (FlattrException e) {
			exceptions.add(e);
		}
		return token;
	}
	
	
}
