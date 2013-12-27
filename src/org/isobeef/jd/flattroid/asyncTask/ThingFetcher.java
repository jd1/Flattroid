package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Thing;

import android.net.Uri;

public class ThingFetcher extends ServiceTask<Uri, Void, Thing> {

	private static final String TAG = "ThingFetcher";

	public ThingFetcher(FlattrService service, OnFetched<Thing> listener) {
		super(service, listener);
	}

	@Override
	protected Thing doInBackground(Uri... params) {
		Uri uri = params[0];
		String host = uri.getHost();
		List<String> pathSegments = uri.getPathSegments();
		if(host.equals("flattr.com") && pathSegments.size() >= 2 && pathSegments.get(0).equals("thing")) {
			try {
				MyLog.d(TAG, pathSegments.get(1));
				return service.getThing(Thing.withId(pathSegments.get(1)));
			} catch (FlattrException e) {
				exceptions.add(e);
				return null;
			}
		}
		try {
			return service.getThingByUrl(params[0].toString());
		} catch (FlattrException e) {
			exceptions.add(e);
			return null;
		}
	}



}
