package org.isobeef.jd.flattroid.util;

import android.util.Log;

public class MyLog {
	private final static Boolean DEBUG = true;
	
	static {
		
	}
	
	public static void d(String tag, String message) {
		if(DEBUG) Log.d(tag, message);
	}
	
	public static void d(String tag, Object message) {
		d(tag, message.toString());
	}
}

