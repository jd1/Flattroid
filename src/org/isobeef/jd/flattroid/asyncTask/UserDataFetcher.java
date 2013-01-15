package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;
import org.shredzone.flattr4j.model.Activity.Type;

public class UserDataFetcher extends ServiceTask<UserId, Void, UserActivityData> implements UserActivityData{

	protected List<Activity> incoming;
	protected List<Activity> outgoing;
	protected List<Flattr> flattrs;
	protected List<Thing> things;
	protected User user;
	
	public UserDataFetcher(FlattrService service, OnFetched<UserActivityData> listener)
			throws Exception {
		super(service, listener);
	}

	@Override
	protected UserActivityData doInBackground(UserId... params) {
		UserId userId = params[0];
		try {
			incoming = service.getActivities(userId, Type.INCOMING);
		} catch (FlattrException e) {
			exception = e;
		}
		try {
			outgoing = service.getActivities(userId, Type.OUTGOING);
		} catch (FlattrException e) {
			exception = e;
		}
		try {
			flattrs = service.getFlattrs(userId);
		} catch (FlattrException e) {
			exception = e;
		}
		try {
			things = service.getThings(userId);
		} catch (FlattrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			user = service.getUser(userId);
		} catch (FlattrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public List<Activity> getIncoming() {
		return incoming;
	}

	@Override
	public List<Activity> getOutgoing() {
		return outgoing;
	}

	@Override
	public List<Flattr> getFlattrs() {
		return flattrs;
	}

	@Override
	public List<Thing> getThings() {
		return things;
	}

	@Override
	public User getUser() {
		return user;
	}
	
	

}
