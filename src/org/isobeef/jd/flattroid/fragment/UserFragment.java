package org.isobeef.jd.flattroid.fragment;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.UserFetcher;
import org.isobeef.jd.flattroid.data.StringImageBundle;
import org.isobeef.jd.flattroid.util.ImageUtils;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.model.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment to show details about a {@link User}.
 */
public class UserFragment extends TitleFragment {
	
	public static final String USER = "user";
	protected static final String TAG = "UserFragment";
	protected static final String NAME = "name";
	protected static final String COUNTRY = "country";
	protected static final String DESCRIPTION = "description";
	protected static final String USER_IMAGE = "userImage";

	protected TextView nameView;
	protected TextView countryView;
	protected TextView descriptionView;
	protected ImageView imageView;
	
	protected String name;
	protected String country = "";
	protected String description;
	protected StringImageBundle userImage;
	
	protected UserFetcher fetcher = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		setTitle("Info");
		if ( arguments != null ) {
			String userJson = arguments.getString(USER);
			User user = new User( new FlattrObject(userJson));
			
			name = user.getFirstname() + " " + user.getLastname() + " (" + user.getUserId() + ")";
			if(user.getCity().length() > 0) {
				country += user.getCity();
			}
			if(user.getCity().length() > 0 && user.getCountry().length() > 0) {
				country += ", ";
			}
			if(user.getCountry().length() > 0) {
				country += user.getCountry();
			}
			description = user.getDescription();
			String avatar = user.getGravatar();
            //TODO move to util
			if(avatar.contains("gravatar")) {
				avatar = avatar.replace("s=40", "s=200");
			} else {
				avatar = avatar.replace("small", "large");
			}
			
			userImage = new StringImageBundle(user.getUserId(), avatar);
		} else if(savedInstanceState != null) {
			name = savedInstanceState.getString(NAME);
			country = savedInstanceState.getString(COUNTRY);
			description = savedInstanceState.getString(DESCRIPTION);
			userImage = savedInstanceState.getParcelable(USER_IMAGE);
		} else {
			throw new IllegalArgumentException("No user!");
		}
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user, container, false);
		
		nameView = (TextView) v.findViewById(R.id.name);
		countryView = (TextView) v.findViewById(R.id.country);
		descriptionView = (TextView) v.findViewById(R.id.description);
		imageView = (ImageView) v.findViewById(R.id.imageView1);
		
		nameView.setText(name);
		countryView.setText(country);
		descriptionView.setText(description);
		
		countryView.setVisibility(country.length() > 0 ? View.VISIBLE : View.GONE);
		ImageUtils.loadOrSetImage(userImage, imageView, getActivity());
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME, name);
        outState.putString(COUNTRY, country);
        outState.putString(DESCRIPTION, description);
        outState.putParcelable(USER_IMAGE, userImage);
    }
}
