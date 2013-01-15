package org.isobeef.jd.flattroid.data;

import java.util.List;

import org.isobeef.jd.flattroid.asyncTask.CategoryFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.oauth.AccessToken;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Storage {
	private static final String TAG = "Storage";
	
	private static final String NAME = "pref";
	private static final String CATEGORIES = "categories";
	private static final String ACCESS_TOKEN = "AccessToken";
	private static FlattrService service;
	private static List<Category> categories = null;
	
	public static void storeFlattrToken(AccessToken token, Context context) {
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		if (token != null) {
			editor.putString(ACCESS_TOKEN, token.getToken());
			MyLog.d(TAG, token.getToken());
		} else {
			editor.putString(ACCESS_TOKEN, null);
			MyLog.d(TAG, "token null");
		}
		editor.commit();
		
	}
	
	private static void storeCategories(List<Category> categories, Context context) {
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		for(Category c : categories) {
			editor.putString(c.getCategoryId(), c.getName());
			MyLog.d(TAG, "Store " + c.getCategoryId() + ": " + c.getName());
		}
		editor.commit();
	}
	
	public static List<Category> getCategories(Context c) {
		if(categories == null) {
			try {
				new CategoryFetcher(service, new CategoryListener(c)).execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return categories;
		
	}
	
	public static Category getCategory(String id, Context context) {
		List<Category> categories = getCategories(context);
		if(categories != null) {
			for(Category c : getCategories(context)) {
				if(c.getCategoryId().equals(id)) {
					return c;
				}
			}
		}
		return null;
	}
	
	public static String getCategoryName(String id, Context context) {
		Category c = getCategory(id, context);
		if(c == null) {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
			return pref.getString(id, id);
		} else {
			return c.getName();
		}
	}
	
	public static String getFlattrToken(Context context) {
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return pref.getString(ACCESS_TOKEN, null);
	}
	
	public static FlattrService getService(Context context) {
		if(service == null) {
			String token = getFlattrToken(context);
			if(token == null) {
				MyLog.d(TAG, "token null");
				return null;
			} else {
				service = FlattrFactory.getInstance().createFlattrService(token);
			}
		}
		return service;
	}
	
	static class CategoryListener implements OnFetched<List<Category>> {
		private Context context;
		
		public CategoryListener(Context c) {
			context = c;
		}
		
		@Override
		public void onFetched(List<Category> result) {
			categories = result;
			storeCategories(result, context);
		}

		@Override
		public ProgressDialog getProgressDialog() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onError(FlattrException e) {
			Log.e(TAG, e.getMessage());
			
		}
		
	}
}
