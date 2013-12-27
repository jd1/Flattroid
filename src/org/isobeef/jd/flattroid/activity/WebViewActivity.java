package org.isobeef.jd.flattroid.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends ActionBarActivity {
	
	protected String mUrl;
	protected WebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		wv = new WebView(this);
		wv.setHorizontalScrollBarEnabled(true);
		wv.setVerticalScrollBarEnabled(true);
		wv.setWebViewClient(new WebViewClient()
        {
            // Override URL
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
            	Log.d("WebView", url);
            	if(url.startsWith("license://")) {
            		String license = url.substring(10);
            		Log.d("WebView", license);
            		String newUrl;
            		if(license.equals("apache")) {
            			newUrl = "file:///android_asset/LICENSE-2.0.txt";
            		} else if(license.equals("mit_cv")) {
            			newUrl = "file:///android_asset/MIT_CV.txt";
            		} else if(license.equals("bsd_image")) {
            			newUrl = "file:///android_asset/BSD-Image.txt";
            		} else {
            			return false;
            		}
            		
            		
            		Intent in = new Intent(getApplicationContext(), WebViewActivity.class);
            		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			in.putExtra("url", newUrl);
        			startActivity(in);
            		return true;
            	} else {
	            	Intent intent = new Intent(Intent.ACTION_VIEW);
	            	intent.setData(Uri.parse(url));
	            	startActivity(intent);
	            	return true;
            	}
            }
        });
		
		setContentView(wv);
		Bundle extras;
		if (savedInstanceState == null) {
		    extras = getIntent().getExtras();
		} else {
			extras = savedInstanceState;
		}
		mUrl = extras.getString("url");
		wv.loadUrl(mUrl);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			finish();
			/*Intent intent = new Intent(this, MainActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);*/
	        return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	  
		savedInstanceState.putString("url", mUrl);
	}
}
