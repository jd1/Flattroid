package org.isobeef.jd.flattroid.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

public class ImageDisplayer implements BitmapDisplayer {

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		imageView.setImageBitmap(bitmap);
		imageView.setVisibility(View.VISIBLE);
		return bitmap;
	}

}
