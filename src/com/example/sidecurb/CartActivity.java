package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class CartActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private int langSelected = -1;
	private static SharedPreferences pref;
	private Button btn;
	private static float sum = 0;
	private String csrftoken;
	private static EditText qnt;
	private SensorManager mSensorManager;
	private float mAccel; 
	private float mAccelCurrent; 
	private float mAccelLast; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_cart;
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        try {
			createProductsList();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        qnt = (EditText)findViewById(R.id.qnt);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	

	@Override
	public void onBackPressed() {
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
            		intent = new Intent(CartActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(CartActivity.this, AccountActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(CartActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(CartActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(CartActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(CartActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(CartActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.cart, menu);
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
		getSupportActionBar().setTitle(R.string.title_activity_cart);
		invalidateOptionsMenu();
    }
    
	private void createProductsList () throws Throwable{
		CartAdapter adapter = new CartAdapter(this, generateData());
		 
	    ListView listView = (ListView) findViewById(R.id.cartList);
	
	    listView.setAdapter(adapter);
	    //listView.onViewRemoved(child)
	    btn = (Button)findViewById(R.id.btn);
	    btn.setText("Total: "+sum+"");
	    btn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(sum > 0){
	        		Intent intent = new Intent(CartActivity.this, SummaryActivity.class);
	        		intent.putExtra("sum", sum);
	        		Bundle extras = new Bundle();
	        		intent.putExtras(extras);
	        		startActivity(intent);
	        	}
	        }
	    });
	    //btn.setText(R.string.sums+sum+R.string.euro);
	}
	
	private ArrayList<Product> generateData() throws JSONException, Throwable{
		
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	  	String cart = pref.getString("cart", null);
		
	  	JSONArray productsList =  new JSONArray(cart);
		
		ArrayList<Product> products = new ArrayList<Product>();
		sum = 0;
		for (int i = 0; i < productsList.length(); i++) {
		    //JSONObject shopList = shopsList.getJSONObject(i);
		    String id = String.valueOf(i);// productsList.getJSONObject(i).getString("id");
		    String sku = productsList.getJSONObject(i).getString("sku");
	        String name = productsList.getJSONObject(i).getString("name");
	        String url = productsList.getJSONObject(i).getString("url");
	        String price = productsList.getJSONObject(i).getString("price");
		    String photo = productsList.getJSONObject(i).getString("photo");
	        String qnt = productsList.getJSONObject(i).getString("qnt");
	        sum+= Float.valueOf(price) * Float.valueOf(qnt);
	        Product prod = new Product(id,sku,name,url,price,"",photo,"","",qnt); 
	        products.add(prod);
		}
	    return products;
	}
	
	 @TargetApi(Build.VERSION_CODES.KITKAT) static OnClickListener remove (View v, ArrayList<Product> productsArrayList, Product s, int position){
		 productsArrayList.remove(s);
		 String cart = pref.getString("cart", null);
		 try {
			JSONArray productsList =  new JSONArray(cart);
			productsList.remove(position);
			Editor editor = pref.edit();
			editor.putString("cart", productsList.toString());
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public static void recalculate(View v, ArrayList<Product> productsArrayList, Product s, int position, String quantity) {
		String cart = pref.getString("cart", null);
		 try {
			JSONArray productsList =  new JSONArray(cart);
	        productsList.getJSONObject(position).put("qnt",quantity);
			
			Editor editor = pref.edit();
			editor.putString("cart", productsList.toString());
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
     			Editor editor = pref.edit();
     			editor.putString("cart", "[]");
     			editor.putString("shop", "");
     			editor.commit();
        	    Intent shakeintent = new Intent(CartActivity.this, CartActivity.class);
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

