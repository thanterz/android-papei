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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.ActionBar.LayoutParams;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class OrdersActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
	
	private ProgressBar spinner;
	private int langSelected = -1;
	private String mykey;
	private String cr;
	private String json;
	
	
	ArrayList<String> matches_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		
		final LinearLayout active = (LinearLayout)findViewById(R.id.activeorderslistlayout);
		final LinearLayout history = (LinearLayout)findViewById(R.id.orderhistorylistlayout);
		
		Button showactiveorderButton = (Button)findViewById(R.id.showactiveorders);
		Button showorderhistoryButton = (Button)findViewById(R.id.showorderhistory);
		
		showactiveorderButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(active.getVisibility()==View.GONE)
		                {
		                    active.setVisibility(View.VISIBLE);
		
		                }
		               history.setVisibility(View.GONE);
						
					}
		});
		
		showorderhistoryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(history.getVisibility()==View.GONE)
                {
                    history.setVisibility(View.VISIBLE);

                }
               active.setVisibility(View.GONE);
				
			}
		});
		SharedPreferences shared = getSharedPreferences("MyPref", 0);
        cr = (shared.getString("csrftoken", ""));
        mykey = (shared.getString("key", ""));
		
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_orders;
        new ActiveOrdersApi().execute();
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Orders", "My Cart", "My Account", "FAQ" ,"Log Out"};
        mAdapter = new DrawerAdapter(OrdersActivity.this, menuArray);
        mDrawerList.setAdapter(mAdapter);
        
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(OrdersActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(OrdersActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(OrdersActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(OrdersActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(OrdersActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(OrdersActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(OrdersActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.orders, menu);
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
		getSupportActionBar().setTitle(R.string.title_activity_orders);
		invalidateOptionsMenu();
    }
    class ActiveOrdersApi extends AsyncTask<Void, Void, Boolean> {
    	
    	@Override
    	protected Boolean doInBackground(Void... params) {
    		// TODO: attempt authentication against a network service.
    		HttpClient httpClient = new DefaultHttpClient();
    		HttpGet httpget = new HttpGet("http://www.theama.info/curbweb/api/api/pickups/");
    		httpget.setHeader("Authorization","Token "+ mykey);
    		//httpput.setHeader("Content-Type", "application/json");
    		//httpput.setHeader("X-CSRFToken",cr);
    		//httpput.setHeader("Cookie","csrftoken="+cr);
    		//httpput.setHeader("Cookie","sessionid="+sessionString);
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
    			Log.d("json",json);
				try {
					createOrdersList();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			else{
				Toast.makeText(getApplicationContext(), "Login error.Try again!", Toast.LENGTH_SHORT).show();
			}
    	}}
    	private void createOrdersList () throws Throwable{
    		OrdersAdapter adapter = new OrdersAdapter(this, generateData());
    		 
            ListView listView = (ListView) findViewById(R.id.ordersList);
     
            listView.setAdapter(adapter);
    	    
    	    setListViewHeightBasedOnChildren(adapter, listView);
           // listView.setOnItemClickListener(new OnItemClickListener() {
             //   public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
               // 	Order entry = (Order) parent.getItemAtPosition(position);
                    //Intent intent = new Intent(OrdersActivity.this, CategoriesActivity.class);
                    //intent.putExtras(extras);
                    //startActivity(intent);
               // }
            //});
        }
     
        private ArrayList<Order> generateData() throws JSONException, Throwable{
    		JSONArray ordersList =  new JSONArray(json);
    		ArrayList<Order> orders = new ArrayList<Order>();
    		for (int i = 0; i < ordersList.length(); i++) {
    		    //JSONObject shopList = shopsList.getJSONObject(i);
    		    String purchaseString = ordersList.getJSONObject(i).getString("purchase_no");
    	        String dateString = ordersList.getJSONObject(i).getString("date");
    	       
    	        Order nOrder = new Order(purchaseString,dateString); 
    	        
    	        orders.add(nOrder);
    	        
    		}
    		
            return orders;
        }
        @SuppressLint("NewApi") public static void setListViewHeightBasedOnChildren(OrdersAdapter adapter, ListView listView) 
  	  {
  		  OrdersAdapter listAdapter = adapter;
  	      if (listAdapter == null)
  	          return;

  	      int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
  	      int totalHeight=0;
  	      View view = null;

  	      for (int i = 0; i < listAdapter.getCount(); i++) 
  	      {
  	          view = listAdapter.getView(i, view, listView);

  	          if (i == 0)
  	              view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,  LayoutParams.WRAP_CONTENT));

  	          view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
  	          totalHeight += view.getMinimumHeight();

  	      }

  	      ViewGroup.LayoutParams params = listView.getLayoutParams();
  	      params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

  	      listView.setLayoutParams(params);
  	      listView.requestLayout();

  	  }

    
    
    }
    
