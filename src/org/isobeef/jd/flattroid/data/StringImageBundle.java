package org.isobeef.jd.flattroid.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class StringImageBundle implements Parcelable {
	public String getStringItem() {
		return stringItem;
	}

	public String getAvatar() {
		return avatar;
	}
	
	public void setBitmap(Bitmap b) {
		bitmap = b;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public StringImageBundle(String stringItem, String avatar) {
		super();
		this.stringItem = stringItem;
		this.avatar = avatar;
	}

	protected StringImageBundle(Parcel in) {
		stringItem = in.readString();
		avatar = in.readString();
		bitmap = in.readParcelable((ClassLoader) Bitmap.CREATOR);
	}

	protected String stringItem;
	protected String avatar;
	protected Bitmap bitmap = null;
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(stringItem);
		dest.writeString(avatar);
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
