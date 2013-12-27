package org.isobeef.jd.flattroid.adapter;

import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.data.Holder;
import org.shredzone.flattr4j.model.Thing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThingListAdapter extends BaseAdapter {

	protected List<Thing> things;
	protected Context context;
	
	public ThingListAdapter(List<Thing> flattrs, Context context) {
		this.things = flattrs;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return things.size();
	}

	@Override
	public Thing getItem(int position) {
		return things.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Thing thing = getItem(position);
		
		View rowView = inflater.inflate(R.layout.flattr_list_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.description);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
		
		textView.setText(thing.getTitle());
		Holder.getImageLoader(context).displayImage(thing.getImage(), imageView);
		
		return rowView;
	}

}
