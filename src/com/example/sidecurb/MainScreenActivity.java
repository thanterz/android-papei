package com.example.sidecurb;

import java.io.IOException;
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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private double lon;
	private double lat;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	private ProgressBar spinner;
	private int langSelected = -1;
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private static final int REQUEST_CODE = 1234;
	private SharedPreferences pref;
	Button Start;
	TextView Speech;
	Dialog match_text_dialog;
	ListView textlist;
	ArrayList<String> matches_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		spinner = (ProgressBar)findViewById(R.id.progress);
		spinner.setVisibility(View.VISIBLE);	
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_main_screen;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public void onBackPressed() {
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Orders", "My Cart", "My Account", "FAQ" ,"Log Out"};
        mAdapter = new DrawerAdapter(MainScreenActivity.this, menuArray);
        mDrawerList.setAdapter(mAdapter);
        
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(MainScreenActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(MainScreenActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(MainScreenActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(MainScreenActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(MainScreenActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(MainScreenActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        if (id == R.id.english) {
        	updateconfig("en");
        }
        else if(id == R.id.greek){
        	updateconfig("el");
        }
        else if(id == R.id.speak){
        	speak();
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
			//HttpClient httpclient = new DefaultHttpClient();
			HttpClient httpClient = DefaultHttp.getInstance();
			HttpGet httpget = new HttpGet("http://www.theama.info/curbweb/api/api/nearby/lat/"+lat+"/lng/"+lon+"/?format=json");
			HttpEntity httpEntity = null;
            HttpResponse response = null;
			try {
				response = httpClient.execute(httpget);
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
				spinner.setVisibility(View.GONE);
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
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            	
            	Shop entry = (Shop) parent.getItemAtPosition(position);
            	pref = getApplicationContext().getSharedPreferences("MyPref", 0);
				Editor editor = pref.edit();
				editor.putString("curshop", entry.getId());
				editor.commit();
                Intent intent = new Intent(MainScreenActivity.this, CategoriesActivity.class);
                intent.putExtra("shop", entry.getShop());
                intent.putExtra("address", entry.getAddress());
                intent.putExtra("logo", entry.getLogo());
                intent.putExtra("name", entry.getName());
                intent.putExtra("lat", entry.getLat());
                intent.putExtra("lon", entry.getLon());
                intent.putExtra("distance", entry.getDistance());
                Bundle extras = new Bundle();
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
 
    private ArrayList<Shop> generateData() throws JSONException, Throwable{
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
	        String shop = shopsList.getJSONObject(i).getString("shop");
	        String lat = shopsList.getJSONObject(i).getString("lat");
	        String lon = shopsList.getJSONObject(i).getString("lon");
	        if(logo.indexOf("http://www.theama.info/media/")==-1 ){
	        	logo = "http://www.theama.info/media/"+logo;
	        }
	        Shop nShop = new Shop(id,name,logo, null,distance, address, shop,lat,lon); 
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
		getSupportActionBar().setTitle(R.string.title_activity_main_screen);
		invalidateOptionsMenu();
    }
    
    public void speak(){
    	if(isConnected()){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            startActivityForResult(intent, REQUEST_CODE);
    	}
    	else{
            Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
        }
    }
    
    public  boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
        	return true;
        } 
        else {
        	return false;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
    		matches_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		String command;
    		for(int i = 0; i < matches_text.size();i++){
    			command = matches_text.get(i);
    			if(command.equalsIgnoreCase("Cart")){
    				Intent intent = new Intent(MainScreenActivity.this, CartActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("stores")){
    				Intent intent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("account")){
    				Intent intent = new Intent(MainScreenActivity.this, AccountActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("frequently asked questions")){
    				Intent intent = new Intent(MainScreenActivity.this, FAQActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("log out")){
    				Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
    				startActivity(intent);
            	}
    			else if(command.equalsIgnoreCase("orders") ){
    				Intent intent = new Intent(MainScreenActivity.this, OrdersActivity.class);
    				startActivity(intent);
            	}
    		}
    	}
    	else{
    		Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
          float x = se.values[0];
          float y = se.values[1];
          float z = se.values[2];
          mAccelLast = mAccelCurrent;
          mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
          float delta = mAccelCurrent - mAccelLast;
          mAccel = mAccel * 0.9f + delta; // perform low-cut filter
          if (mAccel > 12) {
        	    Intent shakeintent = new Intent(MainScreenActivity.this, MainScreenActivity.class);
        	    startActivity(shakeintent);
        	}
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
      };

      @Override
      protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
      }

      @Override
      protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
      }
    
}
