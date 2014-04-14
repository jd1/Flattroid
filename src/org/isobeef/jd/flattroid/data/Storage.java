package org.isobeef.jd.flattroid.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.isobeef.jd.flattroid.asyncTask.CategoriesBasicFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.FlattrFactory;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.oauth.AccessToken;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.appcompat.R;
import android.util.Log;

public class Storage {
	private static final String TAG = "Storage";

	private static final String NAME = "pref";
	private static final String CATEGORIES = "categories";
	private static final String ACCESS_TOKEN = "AccessToken";
	private static FlattrService service;
	private static List<Category> categories = null;
	private static List<WaitForCategories> categoriesListeners = new ArrayList<Storage.WaitForCategories>();
	private static boolean fetchInProgress = false;

	private static Comparator<Category> comp = new Comparator<Category>() {

		@Override
		public int compare(Category lhs, Category rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}
	};

	public static void storeFlattrToken(AccessToken token, Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(NAME, Context.MODE_PRIVATE);
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

	private static void storeCategories(List<Category> categories,
			Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		for (Category c : categories) {
			editor.putString(c.getCategoryId(), c.toJSON());
			MyLog.d(TAG, "Store " + c.getCategoryId() + " " + c.getName());
		}
		editor.commit();
	}

	public static void getCategories(Context context,
			WaitForCategories waitForCategories) {
		if (categories == null) {
			categories = loadCategoriesFromDB(context);
		}
		if (categories == null) {
			categoriesListeners.add(waitForCategories);
			if (!fetchInProgress) {
				new CategoriesBasicFetcher(service, new CategoryListener(
						context)).execute();

			}
		} else {
			waitForCategories.onCategoriesAvailable(categories);
		}
	}

	private static List<Category> loadCategoriesFromDB(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
		Map<String, ?> map = pref.getAll();
		if (!map.isEmpty()) {
			List<Category> categories = new ArrayList<Category>();
			for (String key : map.keySet()) {
				String json = (String) map.get(key);
				Log.d(TAG, key + " : " + json);
				try {
					categories.add(new Category(new FlattrObject(json)));
				} catch (RuntimeException e) {
					Editor editor = pref.edit();
					editor.remove(key);
					editor.commit();
					MyLog.d(TAG, "remove " + key + " : " + json);
				}
			}
			if (!categories.isEmpty()) {

				Collections.sort(categories, comp);
				return categories;
			}
		}
		return null;
	}

	public static void getCategory(final String id, Context context, final WaitForCategory waitForCategory) {
		getCategories(context, new WaitForCategories() {
			
			@Override
			public void onError() {
                waitForCategory.onError();
			}
			
			@Override
			public void onCategoriesAvailable(List<Category> categories) {
                boolean categoryFound = false;
				if (categories != null) {
					for (Category category : categories) {
						if (category.getCategoryId().equals(id)) {
                            waitForCategory.onCategoriesAvailable(category);
                            categoryFound = true;
						}
					}
				}
                if(!categoryFound) {
                    waitForCategory.onError();
                }
			}
		});
	}

	public static String getFlattrToken(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return pref.getString(ACCESS_TOKEN, null);
	}

	public static FlattrService getService(Context context) {
		if (service == null) {
			String token = getFlattrToken(context);
			if (token == null) {
				MyLog.d(TAG, "token null");
				return null;
			} else {
				service = FlattrFactory.getInstance()
						.createFlattrService(token);
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
			for (WaitForCategories w : categoriesListeners) {
				w.onCategoriesAvailable(result);
			}
			categoriesListeners.clear();
		}

		@Override
		public ProgressDialog getProgressDialog() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onError(List<FlattrException> exceptions) {
			for (FlattrException e : exceptions) {
				Log.e(TAG, e.getMessage());
			}
			for (WaitForCategories w : categoriesListeners) {
				w.onError();
			}
			categoriesListeners.clear();
		}

	}

	public interface WaitForCategories extends WaitFor{
		void onCategoriesAvailable(List<Category> categories);
	}

    public interface WaitForCategory extends WaitFor{
        void onCategoriesAvailable(Category category);
    }

    public interface WaitFor {
        void onError();
    }
}
