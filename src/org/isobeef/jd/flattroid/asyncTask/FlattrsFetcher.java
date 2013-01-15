package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.ThingId;

public class FlattrsFetcher extends ServiceTask<ThingId, Void, List<Flattr>> {

	public FlattrsFetcher(FlattrService service,
			OnFetched<List<Flattr>> listener) throws Exception {
		super(service, listener);
	}

	@Override
	protected List<Flattr> doInBackground(ThingId... arg0) {
		try {
			return service.getFlattrs(arg0[0]);
		} catch (FlattrException e) {
			// TODO Auto-generated catch block
			exception = e;
			return null;
		}
	}

}
