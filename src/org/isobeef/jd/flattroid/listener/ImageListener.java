package org.isobeef.jd.flattroid.listener;

import org.isobeef.jd.flattroid.data.ImageDisplayer;
import org.isobeef.jd.flattroid.data.StringImageBundle;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageListener implements ImageLoadingListener {

	protected StringImageBundle bundle;
	protected ImageView view;
	public ImageListener(StringImageBundle sib, ImageView v) {
		bundle = sib;
		view = v;
	}

    @Override
    public void onLoadingStarted(String s, View view) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {
        Log.e("ImageListener", failReason.toString());
    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        bundle.setBitmap(bitmap);
        this.view.setImageBitmap(bundle.getBitmap());
        this.view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingCancelled(String s, View view) {
        // TODO Auto-generated method stub
    }
}
