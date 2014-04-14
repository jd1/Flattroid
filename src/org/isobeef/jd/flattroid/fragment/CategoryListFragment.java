package org.isobeef.jd.flattroid.fragment;

import java.util.List;

import org.isobeef.jd.flattroid.adapter.CategoryListAdapter;
import org.isobeef.jd.flattroid.data.Storage;
import org.isobeef.jd.flattroid.data.Storage.WaitForCategories;
import org.shredzone.flattr4j.model.Category;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryListFragment extends ListFragment {
	
	private CategoryListAdapter listAdapter;
	private OnCategorySelectedListener listener;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final FragmentActivity activity = getActivity();
		Storage.getCategories(activity, new WaitForCategories() {
			
			@Override
			public void onError() {
				Toast.makeText(activity, "Unable to load categories.", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onCategoriesAvailable(List<Category> categories) {
				listAdapter = new CategoryListAdapter(categories, activity);
				setListAdapter( listAdapter );
			}
		});
		
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
