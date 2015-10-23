package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private ProgressBar spinner;
	private int langSelected = -1;
	private String cr;
	private String mykey;
	private String cartsString;
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	private TextView textView1;
	private Intent intent;
	private Float sum;
	private String cart;
	private SharedPreferences pref;
	private static final int REQUEST_CODE = 1234;
	private String shopid;
	private String shop;
	private String sid;
	private int integershopid;
	Button Start;
	TextView Speech;
	Dialog match_text_dialog;
	ListView textlist;
	ArrayList<String> matches_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		spinner = (ProgressBar)findViewById(R.id.progress);
		spinner.setVisibility(View.VISIBLE);	
		
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_summary;
        intent = getIntent();
        sum = intent.getFloatExtra("sum", 0);
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        SharedPreferences shared = getSharedPreferences("MyPref", 0);
        mykey = (shared.getString("key", ""));
        cartsString = (shared.getString("cart", ""));
        shop = (shared.getString("shop", ""));
        String[] bits = shop.split("/");
        String shopid = bits[bits.length-1];
        integershopid = Integer.parseInt(shopid);
        
        new CallApi().execute();
        Button completeorderButton = (Button)findViewById(R.id.completeorderbtn);
        completeorderButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CompleteOrderApi().execute();
			}
		});
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Orders", "My Cart", "My Account", "FAQ" ,"Log Out"};
        mAdapter = new DrawerAdapter(SummaryActivity.this, menuArray);
        mDrawerList.setAdapter(mAdapter);
        
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(SummaryActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(SummaryActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(SummaryActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(SummaryActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(SummaryActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(SummaryActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(SummaryActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.summary, menu);
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
			HttpClient httpclient = DefaultHttp.getInstance();
			HttpGet httpget = new HttpGet("http://www.theama.info/curbweb/user/?format=json");
			httpget.setHeader("Authorization","Token "+ mykey);
			HttpEntity httpEntity = null;
            HttpResponse response = null;
			try {
				Log.d("my key", cartsString);
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
				//Intent intent = new Intent(AccountActivity.this, MainScreenActivity.class);
			    //startActivity(intent);
				String username = null;
				String email = null;
				String fname = null;
				String lname = null;
				json = "[" + json + "]";
				JSONArray dataList;
				try {
					dataList = new JSONArray(json);
					username = dataList.getJSONObject(0).getString("username");
					email = dataList.getJSONObject(0).getString("email");
					fname = dataList.getJSONObject(0).getString("first_name");
					lname = dataList.getJSONObject(0).getString("last_name");
					
					editText1 = (EditText)findViewById(R.id.userinput);
					editText1.setText(username);
					editText2 = (EditText)findViewById(R.id.emailinput);
					editText2.setText(email);
					editText3 = (EditText)findViewById(R.id.nameinput);
					editText3.setText(fname);
					editText4 = (EditText)findViewById(R.id.surnameinput);
					editText4.setText(lname);
					textView1 = (TextView)findViewById(R.id.sumval);
					textView1.setText(sum.toString()+"�");
					spinner.setVisibility(View.GONE);	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				    
            }
			else{
				spinner.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "Error.Try again!", Toast.LENGTH_SHORT).show();
			}
		}

	}

class CompleteOrderApi extends AsyncTask<Void, Void, Boolean> {
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.theama.info/curbweb/api/api/pickups/");
		httppost.setHeader("Authorization","Token "+ mykey);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
		String uniqueID = UUID.randomUUID().toString();
		Log.d("unique",uniqueID);
		nameValuePair.add(new BasicNameValuePair("purchase_no", uniqueID ));
		nameValuePair.add(new BasicNameValuePair("pick", "false"));
		nameValuePair.add(new BasicNameValuePair("store",Integer.toString(integershopid)));
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.ENGLISH);
	    String cDateTime=dateFormat.format(new Date());

		nameValuePair.add(new BasicNameValuePair("date", cDateTime));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			String jsonstring = EntityUtils.toString(response.getEntity());
			JSONObject object = new JSONObject(jsonstring);
			if(object.has("purchase_no")){
				return true;
			}
			else{
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;

	}
	

	@Override
	protected void onPostExecute(final Boolean success) {
		if(success.equals(true)){
			try {
				
				Toast.makeText(getApplicationContext(), "Order Completed", Toast.LENGTH_SHORT).show();
				pref = getApplicationContext().getSharedPreferences("MyPref", 0);
				Editor editor = pref.edit();
				editor.remove("cart");
				cart = pref.getString("cart", null);
				cart = "[]";
				sid = pref.getString("shop", null);
				sid = "";
				editor.putString("cart", cart);
				editor.putString("shop", sid);
				editor.commit();
				Intent intent = new Intent(SummaryActivity.this, MainScreenActivity.class);
			    startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
        }
		else{
			Toast.makeText(getApplicationContext(), "check your exclamation marks", Toast.LENGTH_SHORT).show();
		}
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
		getSupportActionBar().setTitle(R.string.title_activity_summary);
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
    				Intent intent = new Intent(SummaryActivity.this, CartActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("stores")){
    				Intent intent = new Intent(SummaryActivity.this, MainScreenActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("account")){
    				Intent intent = new Intent(SummaryActivity.this, AccountActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("frequently asked questions")){
    				Intent intent = new Intent(SummaryActivity.this, FAQActivity.class);
    				startActivity(intent);
    			}
    			else if(command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("log out")){
    				Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
    				startActivity(intent);
            	}
    			else if(command.equalsIgnoreCase("orders") ){
    				Intent intent = new Intent(SummaryActivity.this, OrdersActivity.class);
    				startActivity(intent);
            	}
    			else if(command.equals("complete order")){
    				//Intent intent = new Intent(SummaryActivity.this, FAQActivity.class);
    				//startActivity(intent);
    				new CompleteOrderApi().execute();
    			}
    		}
    	}
    	else{
    		Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
