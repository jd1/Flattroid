package org.isobeef.jd.flattroid.util;

import java.text.MessageFormat;
import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;

import android.util.Log;

public class MyLog {
	private final static Boolean DEBUG = true;
		
	public static void d(String tag, String message) {
		if(DEBUG) Log.d(tag, message);
	}
	
	public static void d(String tag, Object message) {
		d(tag, message.toString());
	}
	
	public static void d(String tag, String message, Object... params) {
		d(tag, MessageFormat.format(message, params));
	}
	
	public static void logExceptions(String tag, String message, List<FlattrException> exceptions) {
		Log.e(tag, message);
		for (FlattrException flattrException : exceptions) {
			Log.e(tag, flattrException.toString());
		}
	}
}

