package org.isobeef.jd.flattroid.listener;

import org.isobeef.jd.flattroid.data.ImageDisplayer;
import org.isobeef.jd.flattroid.data.StringImageBundle;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

public class ImageListener implements ImageLoadingListener {

	protected StringImageBundle bundle;
	protected ImageView view;
	public ImageListener(StringImageBundle sib, ImageView v) {
		bundle = sib;
		view = v;
	}
	
	@Override
	public void onLoadingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingFailed(FailReason failReason) {
		Log.e("ImageListener", failReason.toString());
		
	}

	@Override
	public void onLoadingComplete(Bitmap loadedImage) {
		bundle.setBitmap(loadedImage);
		new ImageDisplayer().display(loadedImage, view);
		
	}

	@Override
	public void onLoadingCancelled() {
		// TODO Auto-generated method stub
		
	}

	
	
}
