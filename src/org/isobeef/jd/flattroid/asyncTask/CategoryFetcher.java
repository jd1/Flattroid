package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.CategoryId;
import org.shredzone.flattr4j.model.SearchQuery;
import org.shredzone.flattr4j.model.SearchResult;
import org.shredzone.flattr4j.model.Thing;

/**
 * Fetch things in a category.
 */
public class CategoryFetcher extends ServiceTask<CategoryId, Void, List<Thing>> {

	public CategoryFetcher(FlattrService service,
			OnFetched<List<Thing>> listener) {
		super(service, listener);
	}

	@Override
	protected List<Thing> doInBackground(CategoryId... ids) {
		if (ids.length != 1) {
			throw new IllegalArgumentException("Only one category id allowed!");
		}
		SearchQuery query = new SearchQuery();
		query.addCategory(ids[0]);
		try {
			SearchResult result = service.searchThings(query, null, null);
			return result.getThings();
		} catch (FlattrException e) {
			exceptions.add(e);
			return null;
		}
	}

}
