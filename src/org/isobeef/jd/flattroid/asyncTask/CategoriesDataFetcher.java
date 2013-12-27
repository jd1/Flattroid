package org.isobeef.jd.flattroid.asyncTask;

import java.util.HashMap;
import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.CategoryId;
import org.shredzone.flattr4j.model.SearchQuery;
import org.shredzone.flattr4j.model.SearchResult;
import org.shredzone.flattr4j.model.Thing;

import android.app.ProgressDialog;

public class CategoriesDataFetcher extends ServiceTask<CategoryId, Integer, CategoryDataProvider> implements CategoryDataProvider{
	
	protected HashMap<CategoryId, List<Thing>> things = new HashMap<CategoryId, List<Thing>>();
	
	public CategoriesDataFetcher(FlattrService service,
			OnFetched<CategoryDataProvider> listener) {
		super(service, listener);
	}
	@Override
	protected void onPreExecute() {
		dialog = listener.getProgressDialog();
		if(dialog != null) {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();
		}
	}
	
	protected void onProgressUpdate(Integer... progress) {
		if(progress.length == 2) {
			dialog.setProgress(progress[0]);
			dialog.setMax(progress[1]);
			dialog.setMessage("Category " + progress[0] + "/" + progress[1]);
		}
		
    }
	

	@Override
	protected CategoryDataProvider doInBackground(CategoryId... params) {
		this.publishProgress(0);
		for(int i = 0; i < params.length; i++) {
			CategoryId id = params[i];
			SearchQuery query = new SearchQuery();
			query.addCategory(id);
			try {
				SearchResult result = service.searchThings(query, null, null);
				things.put(id, result.getThings());
			} catch (FlattrException e) {
				exceptions.add(e);
			}
			
			this.publishProgress(i+1, params.length);
		}
		return this;
	}
	@Override
	public List<Thing> getThings(CategoryId id) {
		return things.get(id);
	}

	
}
