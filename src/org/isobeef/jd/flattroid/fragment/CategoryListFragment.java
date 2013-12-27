package org.isobeef.jd.flattroid.fragment;

import org.isobeef.jd.flattroid.adapter.CategoryListAdapter;
import org.isobeef.jd.flattroid.data.Storage;
import org.shredzone.flattr4j.model.Category;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class CategoryListFragment extends ListFragment {
	
	private CategoryListAdapter listAdapter;
	private OnCategorySelectedListener listener;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listAdapter = new CategoryListAdapter(Storage.getCategories(getActivity()), getActivity());
		setListAdapter( listAdapter );
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	listener = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCategorySelectedListener");
        }
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Category item = listAdapter.getItem(position);
		listener.onCategorySelected(item);
	}
	
	// Container Activity must implement this interface
    public interface OnCategorySelectedListener {
        public void onCategorySelected(Category category);
    }
}
