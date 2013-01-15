package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.exception.FlattrException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class FetcherAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	protected OnFetched<Result> listener;
	protected ProgressDialog dialog;
	protected FlattrException exception = null;
	
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
		if(exception == null) {
			listener.onFetched(result);
		} else {
			listener.onError(exception);
		}
	}
}
