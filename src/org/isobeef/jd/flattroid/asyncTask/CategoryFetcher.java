package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;

public class CategoryFetcher extends ServiceTask<Void, Void, List<Category>> {

	public CategoryFetcher(FlattrService service,
			OnFetched<List<Category>> listener) throws Exception {
		super(service, listener);
	}

	@Override
	protected List<Category> doInBackground(Void... params) {
		try {
			return service.getCategories();
		} catch (FlattrException e) {
			exception = e;
			return null;
		}
	}

}
