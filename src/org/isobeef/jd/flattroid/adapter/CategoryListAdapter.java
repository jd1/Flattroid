package org.isobeef.jd.flattroid.adapter;

import java.util.List;

import org.shredzone.flattr4j.model.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

	private final List<Category> categories;
	private final Context context;

	public CategoryListAdapter(List<Category> categories, Context context) {
		super();
		this.categories = categories;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Category getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(android.R.layout.simple_list_item_1, parent, false);
	    }

	    TextView text = (TextView) convertView.findViewById(android.R.id.text1);

	    text.setText(getItem(position).getName());

	    return convertView;
	}

}
