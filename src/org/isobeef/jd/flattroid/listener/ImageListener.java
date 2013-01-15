package org.isobeef.jd.flattroid.listener;

import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.exception.FlattrException;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class ImageListener implements OnFetched<Bitmap> {

	protected static final String TAG = "ImageListener";
	
	protected ImageView image;
	public ImageListener(ImageView image) {
		this.image = image;
	}
	
	@Override
	public void onFetched(Bitmap result) {
		if(result != null) {
			image.setImageBitmap(result);
			image.setVisibility(View.VISIBLE);

			MyLog.d(TAG, result.getWidth() + "x" + result.getHeight());
			MyLog.d(TAG, image.getWidth() + "x" + image.getHeight());
		}
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return null;
	}

	@Override
	public void onError(FlattrException e) {
		e.printStackTrace();
		
	}
	
}
