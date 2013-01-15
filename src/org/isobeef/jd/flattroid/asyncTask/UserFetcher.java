package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

public class UserFetcher extends FetcherAsyncTask<UserId, Void, User> {
	protected FlattrService service;

	public UserFetcher(FlattrService service, OnFetched<User> listener) {
		super(listener);
		this.service = service;
	}

	@Override
	protected User doInBackground(UserId... params) {
		User user = null;
		try {
			if(params.length > 0) {
				user = service.getUser(params[0]);
			} else {
				user = service.getMyself();
			}
		} catch (FlattrException e) {
			exception = e;
		}
		return user;
		
	}
}
