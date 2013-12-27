package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;

public class CategoriesBasicFetcher extends ServiceTask<Void, Void, List<Category>> {

	public CategoriesBasicFetcher(FlattrService service,
			OnFetched<List<Category>> listener) {
		super(service, listener);
	}

	@Override
	protected List<Category> doInBackground(Void... params) {
		try {
			return service.getCategories();
		} catch (FlattrException e) {
			exceptions.add(e);
			return null;
		}
	}

}
