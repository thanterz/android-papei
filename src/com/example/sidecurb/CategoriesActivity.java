package com.example.sidecurb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.sidecurb.MainScreenActivity.CallApi;
import com.example.sidecurb.MainScreenActivity.MyLocationListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi") public class CategoriesActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private String shop;
	private String address;
	private String logo;
	private String name;
	private String lat;
	private String lon;
	private String distance;
	private ProgressBar spinner;
	private Intent intent;
	private int langSelected = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
		spinner = (ProgressBar)findViewById(R.id.progress);
		spinner.setVisibility(View.VISIBLE);	
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_categories;
        getSupportActionBar().setTitle(mActivityTitle);
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();
        intent = getIntent();
        shop = intent.getStringExtra("shop");
        address = intent.getStringExtra("address");
        logo 	= intent.getStringExtra("logo");
        name 	= intent.getStringExtra("name");
        lat 	= intent.getStringExtra("lat");
        lon 	= intent.getStringExtra("lon");
        distance = intent.getStringExtra("distance");
        TextView shopName = (TextView)findViewById(R.id.name);
        TextView shopAddress = (TextView)findViewById(R.id.address);
        ImageView imageView  = (ImageView)findViewById(R.id.image);
        TextView distView = (TextView)findViewById(R.id.distance);
        TextView timeView = (TextView) findViewById(R.id.time);
        imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lon));
				startActivity(intent);
			}
		});
        ImageView mapiconView = (ImageView) findViewById(R.id.mapicon);
		mapiconView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
						Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lon));
						startActivity(intent);
					}
		});
        shopName.setText(name);
        shopAddress.setText(address);
        distView.setText(distance);
        timeView.setText(R.string.readytext);
        new DownloadImageTask(imageView).execute(logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Orders", "My Cart", "My Account", "FAQ", "Log Out" };
        mAdapter = new DrawerAdapter(this, menuArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(CategoriesActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(CategoriesActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(CategoriesActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(CategoriesActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(CategoriesActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(CategoriesActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(CategoriesActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
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
        getMenuInflater().inflate(R.menu.categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.english) {
        	updateconfig("en");
        }
        else if(id == R.id.greek){
        	updateconfig("el");
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
class CallApi extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(shop+"categories/?format=json");
			HttpEntity httpEntity = null;
            HttpResponse response = null;
			try {
				response = httpclient.execute(httpget);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
    			
            	httpEntity = response.getEntity();
                try {
					json = EntityUtils.toString(httpEntity,"UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return null;

		}
		

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success.equals(true)){
				try {
					createCategoriesList();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				spinner.setVisibility(View.GONE);
            }
			else{
				Toast.makeText(getApplicationContext(), "Login error.Try again!", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	private void createCategoriesList () throws Throwable{
		CategoryAdapter adapter = new CategoryAdapter(this, generateData());
		 
	    ListView listView = (ListView) findViewById(R.id.categoriesList);
	
	    listView.setAdapter(adapter);
	    
	    setListViewHeightBasedOnChildren(adapter, listView);
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	        	Category entry = (Category) parent.getItemAtPosition(position);
	            Intent intent = new Intent(CategoriesActivity.this, ProductsActivity.class);
	            Integer catid = Integer.parseInt((String) entry.getId()) + 1;
	            intent.putExtra("shop", shop+"categories/"+catid);
	            Bundle extras = new Bundle();
	            intent.putExtras(extras);
	            startActivity(intent);
	        }
	    });
	}
	
	private ArrayList<Category> generateData() throws JSONException, Throwable{
		
		JSONArray categoriesList =  new JSONArray(json);
		
		ArrayList<Category> categories = new ArrayList<Category>();
		
		for (int i = 0; i < categoriesList.length(); i++) {
		    //JSONObject shopList = shopsList.getJSONObject(i);
		    String id = categoriesList.getJSONObject(i).getString("parent_cat_id");
	        String name = categoriesList.getJSONObject(i).getString("name");
	        String url = categoriesList.getJSONObject(i).getString("url");
	        
	        Category cat = new Category(id,name,url); 
	        categories.add(cat);
		}
	    return categories;
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
     		  new CallApi().execute();
     	  }
     	}
	  public static void setListViewHeightBasedOnChildren(CategoryAdapter adapter, ListView listView) 
	  {
		  CategoryAdapter listAdapter = adapter;
	      if (listAdapter == null)
	          return;

	      int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	      int totalHeight=0;
	      View view = null;

	      for (int i = 0; i < listAdapter.getCount(); i++) 
	      {
	          view = listAdapter.getView(i, view, listView);

	          if (i == 0)
	              view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,  
	                                        LayoutParams.WRAP_CONTENT));

	          view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	          totalHeight += view.getMinimumHeight();

	      }

	      ViewGroup.LayoutParams params = listView.getLayoutParams();
	      params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

	      listView.setLayoutParams(params);
	      listView.requestLayout();

	  }
	  
	  public void updateconfig(String s){
			String languageToLoad = s;
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config , getBaseContext().getResources().getDisplayMetrics());
			langSelected = 0;
			Bundle tempBundle = new Bundle();
			onCreate(tempBundle);
			getSupportActionBar().setTitle(R.string.title_activity_categories);
			invalidateOptionsMenu();
	    }
}
