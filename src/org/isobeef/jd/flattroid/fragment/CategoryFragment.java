package org.isobeef.jd.flattroid.fragment;

import java.util.List;

import org.isobeef.jd.flattroid.activity.ThingActivity;
import org.isobeef.jd.flattroid.adapter.ThingListAdapter;
import org.isobeef.jd.flattroid.asyncTask.CategoryFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.util.JsonUtils;
import org.shredzone.flattr4j.FlattrService;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.Category;
import org.shredzone.flattr4j.model.Thing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class CategoryFragment extends ListFragment implements OnFetched<List<Thing>> {
	
	public static final String CATEGORY = "category";
	private static final String TAG = "CategoryFragment";
	private Category category;
	private ThingListAdapter listAdapter;
	private FlattrService service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			category = JsonUtils.createFromJson(arguments.getString(CATEGORY), Category.class);
		} else if (savedInstanceState != null) {
			category = JsonUtils.createFromJson(savedInstanceState.getString(CATEGORY), Category.class);
		} else {
			throw new IllegalArgumentException("No category!");
		}
		service = Storage.getService(getActivity());
		CategoryFetcher fetcher = new CategoryFetcher(service, this);
		fetcher.execute(category);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(CATEGORY, category.toJSON());
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if (listAdapter != null) {
			Thing thing = listAdapter.getItem(position);
			Intent in = new Intent(getActivity(), ThingActivity.class);
			Uri uri = Uri.parse(thing.getUrl());
			in.setData(uri);
			startActivity(in);
		} else {
			Log.v(TAG, "No list adapter set. This shouldn't happen.");
		}
	}

	@Override
	public void onFetched(List<Thing> result) {
		listAdapter = new ThingListAdapter(result, getActivity());
		setListAdapter(listAdapter);
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return new ProgressDialog(getActivity());
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		for (FlattrException flattrException : exceptions) {
			Log.e(TAG, "Fetching category failed.", flattrException);
		}
	}
}
