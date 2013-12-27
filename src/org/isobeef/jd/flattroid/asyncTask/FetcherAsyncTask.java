package org.isobeef.jd.flattroid.asyncTask;

import java.util.ArrayList;
import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class FetcherAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	protected OnFetched<Result> listener;
	protected ProgressDialog dialog;
	protected List<FlattrException> exceptions = new ArrayList<FlattrException>();
	
	public FetcherAsyncTask(OnFetched<Result> listener) {
		super();
		this.listener = listener;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = listener.getProgressDialog();
		if(dialog != null) {
			dialog.setMessage("Processing");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}
	}
	
	@Override
	protected void onPostExecute(Result result) {
		if(dialog != null) {
			dialog.dismiss();
		}
		if(!exceptions.isEmpty()) {
			listener.onError(exceptions);
		}
		if(result != null) {
			listener.onFetched(result);
		}
	}
}
