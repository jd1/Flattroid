package org.isobeef.jd.flattroid.asyncTask;

import org.shredzone.flattr4j.exception.FlattrException;

import android.app.ProgressDialog;

public interface OnFetched<T> {
	public void onFetched(T result);
	public ProgressDialog getProgressDialog();
	public void onError(FlattrException e);
}
