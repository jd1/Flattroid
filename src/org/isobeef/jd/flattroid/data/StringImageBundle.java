package org.isobeef.jd.flattroid.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Holds the url and description of an image and if available the bitmap.
 * @author Johannes Dilli
 *
 */
public class StringImageBundle implements Parcelable {
	public String getDescription() {
		return description;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public void setBitmap(Bitmap b) {
		bitmap = b;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public boolean hasBitmap() {
		return bitmap != null;
	}

	public StringImageBundle(String stringItem, String avatar) {
		super();
		this.description = stringItem;
		this.avatarUrl = avatar;
	}

	protected StringImageBundle(Parcel in) {
		description = in.readString();
		avatarUrl = in.readString();
		bitmap = in.readParcelable((ClassLoader) Bitmap.CREATOR);
	}

	protected String description;
	protected String avatarUrl;
	protected Bitmap bitmap = null;
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(description);
		dest.writeString(avatarUrl);
		if(bitmap != null) {
			dest.writeParcelable(bitmap, 0);
		}
		
	}
	
	public static final Parcelable.Creator<StringImageBundle> CREATOR =
	    new Parcelable.Creator<StringImageBundle>() {
	            public StringImageBundle createFromParcel(Parcel in) {
	                return new StringImageBundle(in);
	            }
	 
	            public StringImageBundle[] newArray(int size) {
	                return new StringImageBundle[size];
	            }
	        };

}
