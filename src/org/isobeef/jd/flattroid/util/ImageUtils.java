package org.isobeef.jd.flattroid.util;

import org.isobeef.jd.flattroid.data.Holder;
import org.isobeef.jd.flattroid.data.ImageDisplayer;
import org.isobeef.jd.flattroid.data.StringImageBundle;
import org.isobeef.jd.flattroid.listener.ImageListener;

import android.content.Context;
import android.widget.ImageView;

public class ImageUtils {
	public static void loadOrSetImage(StringImageBundle bundle, ImageView imageView, Context context) {
		if (bundle.hasBitmap()) {
			new ImageDisplayer().display(bundle.getBitmap(), imageView);
		} else {
			Holder.getImageLoader(context).loadImage(context, bundle.getAvatarUrl(), new ImageListener(bundle, imageView));
		}
	}

}
