package org.isobeef.jd.flattroid.fragment;

import java.util.List;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.asyncTask.ImageFetcher;
import org.isobeef.jd.flattroid.asyncTask.OnFetched;
import org.isobeef.jd.flattroid.asyncTask.UserFetcher;
import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.exception.FlattrException;
import org.shredzone.flattr4j.model.User;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserFragment extends TitleFragment implements OnFetched<Bitmap>{
	
	public static final String USER = "user";
	protected static final String TAG = "UserFragment";
	protected static final String NAME = "name";
	protected static final String COUNTRY = "country";
	protected static final String DESCRIPTION = "description";
	protected static final String IMAGE_URL = "imageUrl";
	protected static final String IMAGE = "image";

	protected TextView nameView;
	protected TextView countryView;
	protected TextView descriptionView;
	protected ImageView imageView;
	
	protected boolean hasData;
	protected String name;
	protected String country = "";
	protected String description;
	protected String imageUrl;
	protected Bitmap image;
	
	protected UserFetcher fetcher = null;
	
	public UserFragment() {
		hasData = false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
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
			if(avatar.contains("gravatar")) {
				avatar = avatar.replace("s=40", "s=200");
			} else {
				avatar = avatar.replace("small", "large");
			}
			imageUrl = avatar;
		} else if(!hasData && savedInstanceState != null) {
			name = savedInstanceState.getString(NAME);
			country = savedInstanceState.getString(COUNTRY);
			description = savedInstanceState.getString(DESCRIPTION);
			imageUrl = savedInstanceState.getString(IMAGE_URL);
			image = (Bitmap) savedInstanceState.getParcelable(IMAGE);
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
		
		if(image == null) {
			new ImageFetcher(this).execute(imageUrl);
		} else {
			imageView.setImageBitmap(image);
		}
		
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
        outState.putString(IMAGE_URL, imageUrl);
        outState.putParcelable(IMAGE, image);
    }

	@Override
	public void onFetched(Bitmap result) {
		image = result;
		imageView.setImageBitmap(result);
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return null;
	}

	@Override
	public void onError(List<FlattrException> exceptions) {
		for(FlattrException e : exceptions) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return "Info";
	}
	
}
