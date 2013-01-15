package org.isobeef.jd.flattroid.fragment;

import java.util.ArrayList;
import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.ImageFetcher;
import org.shredzone.flattr4j.model.Flattr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class FlattrsFragment extends TitleFragment {
	
	protected static final String FLATTRS = "flattrs";
	
	protected ArrayList<String> flattrs = new ArrayList<String>();
	protected boolean hasData;
	
	protected ArrayAdapter<String> adapter;
	
	public FlattrsFragment(List<Flattr> flattrs) {
		for(Flattr f : flattrs) {
			this.flattrs.add(f.getCreated().toLocaleString() + ": " + f.getThing().getTitle());
		}
		hasData = true;
	}
	
	public FlattrsFragment() {
		hasData = false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!hasData && savedInstanceState != null) {
			flattrs = savedInstanceState.getStringArrayList(FLATTRS);
		}
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, flattrs);
		//setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.flattrs, container, false);
		
		ListView list = (ListView) v.findViewById(R.id.listView1);
		list.setAdapter(adapter);
		
		return v;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(FLATTRS, flattrs);
    }

	@Override
	public String getTitle() {
		return "Flattrs";
	}
}
