package org.isobeef.jd.flattroid.activity;

import org.isobeef.jd.flattroid.R;
import org.isobeef.jd.flattroid.fragment.CategoryFragment;
import org.isobeef.jd.flattroid.fragment.CategoryListFragment.OnCategorySelectedListener;
import org.shredzone.flattr4j.model.Category;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * {@link ActionBarActivity} to show the catalog with {@link Category}s.
 * @author Johannes Dilli
 *
 */
public class CatalogActivity extends ActionBarActivity implements OnCategorySelectedListener{

	private static final String TAG = "CatalogActivity";
	private boolean dualPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog);
		getSupportActionBar().setTitle("Catalog");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Uri uri = getIntent().getData();
		if(uri != null) {
			Log.i(TAG, uri.toString());
		}
		
		//TODO add dual pane
		dualPane = false;
	}

	@Override
	public void onCategorySelected(Category category) {
		if (dualPane) {
			//TODO adjust
			CategoryFragment fragment = new CategoryFragment();
			Bundle bundle = new Bundle();
			bundle.putString(CategoryFragment.CATEGORY, category.toJSON());
			fragment.setArguments(bundle);
			
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			
			transaction.replace(R.id.catalogFragment, fragment);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.addToBackStack(category.getName());
			transaction.commit();
		} else {
			Intent intent = new Intent();
            intent.setClass(this, CategoryActivity.class);
            intent.putExtra(CategoryActivity.CATEGORY, category.toJSON());
            startActivity(intent);
		}
		
		
		// TODO Auto-generated method stub
		Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
	}
}
