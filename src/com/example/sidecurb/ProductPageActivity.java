package com.example.sidecurb;

import java.io.InputStream;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ProductPageActivity extends ActionBarActivity{
	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
	private String sku;
	private String name;
	private String url;
	private String description;
	private String price;
	private String photo;
	private String categ;
	private ProgressBar spinner;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_page);
		//spinner = (ProgressBar)findViewById(R.id.progress);
		//spinner.setVisibility(View.VISIBLE);	
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "Details";
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        intent = getIntent();
        sku = intent.getStringExtra("sku");
        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        photo = intent.getStringExtra("photo");
        categ = intent.getStringExtra("categ");
        
        TextView prodDescription = (TextView)findViewById(R.id.description);
        prodDescription.setText(description);
        TextView prodPrice = (TextView)findViewById(R.id.price);
        prodPrice.setText(price);
        ImageView imageView  = (ImageView) findViewById(R.id.image);
        new DownloadImageTask(imageView).execute(photo);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Categories", "My Cart", "My Account", "FAQ" };
        mAdapter = new DrawerAdapter(this, menuArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	if(position==0){
            		intent = new Intent(ProductPageActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(ProductPageActivity.this, CategoriesActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(ProductPageActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(ProductPageActivity.this, AccountActivity.class);
            	}
            	else{
            		intent = new Intent(ProductPageActivity.this, FAQActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(ProductPageActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
     	  ImageView bmImage;

     	  public DownloadImageTask(ImageView bmImage) {
     	      this.bmImage = bmImage;
     	  }

     	  protected Bitmap doInBackground(String... urls) {
     	      String urldisplay = urls[0];
     	      Bitmap mIcon11 = null;
     	      try {
     	        InputStream in = new java.net.URL(urldisplay).openStream();
     	        mIcon11 = BitmapFactory.decodeStream(in);
     	      } catch (Exception e) {
     	          Log.e("Error", e.getMessage());
     	          e.printStackTrace();
     	      }
     	      return mIcon11;
     	  }

     	  protected void onPostExecute(Bitmap result) {
     		  bmImage.setImageBitmap(result);
     	  }
     	}
}
