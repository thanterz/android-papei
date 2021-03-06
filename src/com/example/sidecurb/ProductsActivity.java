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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductsActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private String url;
	private Intent intent;
	private ProgressBar spinner;
	private int langSelected = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		spinner = (ProgressBar)findViewById(R.id.progress);
		spinner.setVisibility(View.VISIBLE);	
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_products;
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        intent = getIntent();
        url = intent.getStringExtra("shop");
        new CallApi().execute();
        //TextView shopName = (TextView)findViewById(R.id.name);
        //shopName.setText(name);
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
            		intent = new Intent(ProductsActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(ProductsActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(ProductsActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(ProductsActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(ProductsActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(ProductsActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(ProductsActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.products, menu);
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
			HttpGet httpget = new HttpGet(url+"/?format=json");
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
					createProductsList();
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
				Toast.makeText(getApplicationContext(), "products not ok!", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	private void createProductsList () throws Throwable{
		ProductAdapter adapter = new ProductAdapter(this, generateData());
		 
	    ListView listView = (ListView) findViewById(R.id.productsList);
	
	    listView.setAdapter(adapter);
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	        	Product entry = (Product) parent.getItemAtPosition(position);
	            Intent intent = new Intent(ProductsActivity.this, ProductPageActivity.class);
	            //intent.putExtra("url", entry.getUrl());
	            intent.putExtra("sku", entry.getSku());
	            intent.putExtra("name", entry.getName());
	            intent.putExtra("url", entry.getUrl());
	            intent.putExtra("price", entry.getPrice());
	            intent.putExtra("description", entry.getDescription());
	            intent.putExtra("photo", entry.getPhoto());
	            intent.putExtra("categ", entry.getCateg());
	            intent.putExtra("sid", entry.getSid());
	            Bundle extras = new Bundle();
	            intent.putExtras(extras);
	            startActivity(intent);
	        }
	    });
	}
	
	private ArrayList<Product> generateData() throws JSONException, Throwable{
		
		JSONArray productsList =  new JSONArray(json);
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		for (int i = 0; i < productsList.length(); i++) {
		    //JSONObject shopList = shopsList.getJSONObject(i);
		    String id = String.valueOf(i);// productsList.getJSONObject(i).getString("id");
		    String sku = productsList.getJSONObject(i).getString("sku");
	        String name = productsList.getJSONObject(i).getString("name");
	        String url = productsList.getJSONObject(i).getString("url");
	        String price = productsList.getJSONObject(i).getString("price");
	        String description = productsList.getJSONObject(i).getString("description");
		    String photo = productsList.getJSONObject(i).getString("photo");
	        String categ = productsList.getJSONObject(i).getString("categ");
	        String sid = productsList.getJSONObject(i).getString("sid");
	        
	       Product prod = new Product(id,sku,name,url,price,description,photo,categ,sid,"0"); 
	        products.add(prod);
		}
	    return products;
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
		getSupportActionBar().setTitle(R.string.title_activity_products);
		invalidateOptionsMenu();
    }
}
