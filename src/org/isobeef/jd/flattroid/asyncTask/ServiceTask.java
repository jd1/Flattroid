package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.FlattrService;

public abstract class ServiceTask<Params, Progress, Result> extends FetcherAsyncTask<Params, Progress, Result> {

	protected FlattrService service;
	public ServiceTask(FlattrService service, OnFetched<Result> listener) throws Exception {
		super(listener);
		if(service == null) {
			throw new Exception("Service mustn't be null!");
		} else {
			this.service = service;
		}
	}

}
