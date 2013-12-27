package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.Activity.Type;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;
import org.shredzone.flattr4j.model.UserId;

import android.app.ProgressDialog;

public class UserDataFetcher extends ServiceTask<UserId, Integer, UserActivityData> implements UserActivityData{

	protected List<Activity> incoming;
	protected List<Activity> outgoing;
	protected List<Flattr> flattrs;
	protected List<Thing> things;
	protected User user;
	
	public UserDataFetcher(FlattrService service, OnFetched<UserActivityData> listener) {
		super(service, listener);
	}
	
	@Override
	protected void onPreExecute() {
		dialog = listener.getProgressDialog();
		if(dialog != null) {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.setMax(5);
			dialog.show();
		}
	}
	
	protected void onProgressUpdate(Integer... progress) {
		if(progress.length == 1) {
			dialog.setProgress(progress[0]);
		}
		
    }

	@Override
	protected UserActivityData doInBackground(UserId... params) {
		UserId userId = params[0];
		this.publishProgress(0);
		try {
			incoming = service.getActivities(userId, Type.INCOMING);
		} catch (FlattrException e) {
			exceptions.add(e);
		} finally {
			this.publishProgress(1);
		}
		try {
			outgoing = service.getActivities(userId, Type.OUTGOING);
		} catch (FlattrException e) {
			exceptions.add(e);
		} finally {
			this.publishProgress(2);
		}
		try {
			flattrs = service.getFlattrs(userId);
		} catch (FlattrException e) {
			exceptions.add(e);
		} finally {
			this.publishProgress(3);
		}
		try {
			things = service.getThings(userId);
		} catch (FlattrException e) {
			exceptions.add(e);
		} finally {
			this.publishProgress(4);
		}
		try {
			user = service.getUser(userId);
		} catch (FlattrException e) {
			exceptions.add(e);
		} finally {
			this.publishProgress(5);
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
