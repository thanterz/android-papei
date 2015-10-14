package com.example.sidecurb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String json;
	private String shop;
	private String address;
	private String logo;
	private String name;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "Categories";
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();
        intent = getIntent();
        shop = intent.getStringExtra("shop");
        address = intent.getStringExtra("address");
        logo 	= intent.getStringExtra("logo");
        name 	= intent.getStringExtra("name");
        //TextView shopName = (TextView)findViewById(R.id.name);
        TextView shopAddress = (TextView)findViewById(R.id.address);
        ImageView imageView  = (ImageView)findViewById(R.id.image);
        //shopName.setText(name);
        shopAddress.setText(address);
        new DownloadImageTask(imageView).execute(logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Categories", "My Cart", "My Account", "FAQ" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	if(position==0){
            		intent = new Intent(CategoriesActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(CategoriesActivity.this, CategoriesActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(CategoriesActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(CategoriesActivity.this, AccountActivity.class);
            	}
            	else{
            		intent = new Intent(CategoriesActivity.this, FAQActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(CategoriesActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        if (id == R.id.action_settings) {
            return true;
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
				Toast.makeText(getApplicationContext(), "Shops are ok", Toast.LENGTH_SHORT).show();
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
	    /*listView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	        	Shop entry = (Shop) parent.getItemAtPosition(position);
	            Intent intent = new Intent(CategoriesActivity.this, CategoriesActivity.class);
	            intent.putExtra("id", entry.getId());
	            intent.putExtra("address", entry.getAddress());
	            Bundle extras = new Bundle();
	            intent.putExtras(extras);
	            startActivity(intent);
	        }
	    });*/
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
}
