package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.exception.FlattrException;

import android.app.ProgressDialog;

public interface OnFetched<T> {
	public void onFetched(T result);
	public ProgressDialog getProgressDialog();
	public void onError(List<FlattrException> exceptions);
}
