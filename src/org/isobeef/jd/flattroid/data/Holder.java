package org.isobeef.jd.flattroid.data;

import org.isobeef.jd.flattroid.util.MyLog;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Holder {
	
	protected static final String TAG = "Holder";
	
	protected static ImageLoader imageLoader = null;
	public static ImageLoader getImageLoader(Context c) {
		if(imageLoader == null) {
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showStubImage(android.R.drawable.checkbox_off_background)
			.displayer(new ImageDisplayer())
	        .cacheInMemory()
	        .build();
		
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c.getApplicationContext())
				.memoryCache(new WeakMemoryCache())
				.defaultDisplayImageOptions(defaultOptions)
	        	.build();
			ImageLoader.getInstance().init(config);
			imageLoader = ImageLoader.getInstance();
			MyLog.d(TAG, "init");
		}
		return imageLoader;
	}
}
