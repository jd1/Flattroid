package org.isobeef.jd.flattroid.fragment;

import java.util.ArrayList;
import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.activity.ThingActivity;
import org.isobeef.jd.flattroid.adapter.ThingListAdapter;
import org.isobeef.jd.flattroid.util.JsonUtils;
import org.isobeef.jd.flattroid.util.MyLog;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.model.Thing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ThingFragment extends TitleFragment {
	private static final String TAG = "ThingFragment";
	
	public static final String FLATTRS = "flattrs";
	public static final String TITLE = "title";
	
	protected List<Thing> things;
	
	protected ThingListAdapter adapter;
	
	public ThingFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        MyLog.d(TAG, getTitle()  + " onCreate");
        Bundle arguments = getArguments();
        if ( arguments == null ) {
        	arguments = savedInstanceState;
        }
        
		if(arguments != null) {
			things = new ArrayList<Thing>();
			List<String> json = arguments.getStringArrayList(FLATTRS);
			for(String j : json) {
				things.add(new Thing(new FlattrObject(j)));
			}
			adapter = new ThingListAdapter(things, getActivity());
		} else {
			throw new IllegalArgumentException("No things!");
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MyLog.d(TAG, getTitle() +  " created");
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.flattrs, container, false);
		
		ListView list = (ListView) v.findViewById(R.id.listView1);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position,
					long id) {
				Thing thing = adapter.getItem(position);
				Intent in = new Intent(getActivity(), ThingActivity.class);
				Uri uri = Uri.parse(thing.getLink());
				in.setData(uri);
				getActivity().startActivity(in);
			}
		});
		MyLog.d(TAG, getTitle() +  " view created");
		return v;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putStringArrayList(FLATTRS, JsonUtils.toJson(things));
        MyLog.d(TAG, "onSave");
    }
}
