package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;

public class FlattrTask extends ServiceTask<Thing, Void, Boolean> {

	public FlattrTask(FlattrService service, OnFetched<Boolean> listener) {
		super(service, listener);
	}

	@Override
	protected Boolean doInBackground(Thing... params) {
		try {
			Thing thing = params[0];
			if(thing == null) {
				exceptions.add(new FlattrException("thing is null"));
				return false;
			} else {
				service.click(thing);
				return true;
			}
		} catch (FlattrException e) {
			exceptions.add(e);
			return false;
		}
	}

}
