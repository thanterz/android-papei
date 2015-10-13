package com.example.sidecurb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainScreenActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String json;
	private double lon;
	private double lat;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "Stores";
        addDrawerItems();
        setupDrawer();

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
            		intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(MainScreenActivity.this, CategoriesActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(MainScreenActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(MainScreenActivity.this, AccountActivity.class);
            	}
            	else{
            		intent = new Intent(MainScreenActivity.this, FAQActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(MainScreenActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.main_screen, menu);
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
			HttpGet httpget = new HttpGet("http://www.theama.info/curbweb/api/api/nearby/lat/"+lat+"/lng/"+lon+"/?format=json");
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
//            	json = response.getEntity();
    			
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
					createShopsList();
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

	private void createShopsList () throws Throwable{
		ShopsAdapter adapter = new ShopsAdapter(this, generateData());
		 
        ListView listView = (ListView) findViewById(R.id.shopsList);
 
        listView.setAdapter(adapter);
    }
 
    private ArrayList<Shop> generateData() throws JSONException, Throwable{
    	/*BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(json.getContent()));
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = "";
		StringBuffer jsons = new StringBuffer();
		try {
			while ((line = rd.readLine()) != null) {
				jsons.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
    	/*InputStream inputStream = null;
    	inputStream = json.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	   
	    
	    String line = null;
	    if(reader.ready()){
	    	while ((line = reader.readLine()) != null)
	    	{
	    		sb.append(line + "\n");
	    	}
	    }*/
		JSONArray shopsList =  new JSONArray(json);
		ArrayList<Shop> shops = new ArrayList<Shop>();
		for (int i = 0; i < shopsList.length(); i++) {
		    //JSONObject shopList = shopsList.getJSONObject(i);
		    String id = shopsList.getJSONObject(i).getString("id");
	        String name = shopsList.getJSONObject(i).getString("name");
	        String logo = shopsList.getJSONObject(i).getString("logo");
	        String distance = ""+shopsList.getJSONObject(i).getString("distance")+"";
	        distance = distance.substring(0,distance.indexOf(".")) +"m";
	        String address = shopsList.getJSONObject(i).getString("address");
	        if(logo.indexOf("http://www.theama.info/media/")==-1 ){
	        	logo = "http://www.theama.info/media/"+logo;
	        }
	        Shop nShop = new Shop(id,name,logo, null,distance, address); 
	        /*
	        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
	        Shop nShop = new Shop(id,name,logo);
	        //nShop.setImage(mIcon_val);*/
	        shops.add(nShop);
		}
        return shops;
    }
    public class MyLocationListener implements LocationListener{

	    @Override
	    public void onLocationChanged(Location loc) {
    		lat 	= loc.getLatitude();
    		lon		= loc.getLongitude();
    		if(lat != 0 && lon != 0){
    			mlocManager.removeUpdates(mlocListener);
    			new CallApi().execute();
    		}
			//Toast.makeText( getApplicationContext(),"lat="+lat+" lon="+lon,Toast.LENGTH_SHORT).show();

	    }


	    @Override
	    public void onProviderDisabled(String provider) {
	
	    	Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
	
	    }


	    @Override
	    public void onProviderEnabled(String provider){
	
	    	Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
	
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {


	    }

    }
}
