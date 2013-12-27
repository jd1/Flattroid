package org.isobeef.jd.flattroid.activity;

import org.isobeef.jd.flattroid.fragment.CategoryFragment;
import org.isobeef.jd.flattroid.util.JsonUtils;
import org.shredzone.flattr4j.model.Category;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class CategoryActivity extends ActionBarActivity {
	public static final String CATEGORY = "category";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		String category;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			category = extras.getString(CATEGORY);
		} else if (savedInstanceState != null) {
			category = savedInstanceState.getString(CATEGORY);
		} else {
			throw new IllegalArgumentException("No category!");
		}
		
		getSupportActionBar().setTitle("Catalog");
		getSupportActionBar().setSubtitle(JsonUtils.createFromJson(category, Category.class).getName());
		
		CategoryFragment fragment = new CategoryFragment();
		Bundle args = new Bundle();
		args.putString(CategoryFragment.CATEGORY, category);
		fragment.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(android.R.id.content, fragment);
		transaction.commit();
	}

}
