package org.isobeef.jd.flattroid.util;

import org.isobeef.jd.flattroid.data.Holder;
import org.isobeef.jd.flattroid.data.ImageDisplayer;
import org.isobeef.jd.flattroid.data.StringImageBundle;
import org.isobeef.jd.flattroid.listener.ImageListener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class ImageUtils {
	public static void loadOrSetImage(StringImageBundle bundle, ImageView imageView, Context context) {
		if (bundle.hasBitmap()) {
            imageView.setImageBitmap(bundle.getBitmap());
            imageView.setVisibility(View.VISIBLE);
		} else {
			Holder.getImageLoader(context).displayImage(bundle.getAvatarUrl(), imageView); //.loadImage(bundle.getAvatarUrl(), new ImageListener(bundle, imageView));
		}
	}

}
