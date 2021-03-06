package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import org.json.JSONObject;

import com.example.sidecurb.RegisterActivity.CallApi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AccountActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private String json;
	private String jsonstring;
	private String json2;
	private int langSelected = -1;
	private String cr;
	private String mykey;
	private String sessionString;
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
	 	
		final LinearLayout ln = (LinearLayout)findViewById(R.id.passreset);
		final LinearLayout mainln = (LinearLayout)findViewById(R.id.vlayout);
		final LinearLayout cardln = (LinearLayout)findViewById(R.id.cardlayout);
		
		Button showaccountButton = (Button)findViewById(R.id.showaccount);
		Button showpassButton = (Button)findViewById(R.id.showpass);
		Button showcardButton = (Button)findViewById(R.id.showcard);
		Button updateDataButton = (Button)findViewById(R.id.updatebtn);
		Button emailpassButton = (Button)findViewById(R.id.emailpasssentbtn);
		
		emailpassButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PassResset().execute();
			}
		});
		
		showpassButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ln.getVisibility()==View.GONE)
                {
                    ln.setVisibility(View.VISIBLE);

                }
               mainln.setVisibility(View.GONE);
               cardln.setVisibility(View.GONE);
				
			}
		});
		
		showaccountButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(mainln.getVisibility()==View.GONE)
		                {
		                    mainln.setVisibility(View.VISIBLE);
		
		                }
		               ln.setVisibility(View.GONE);
		               cardln.setVisibility(View.GONE);
						
					}
		});
		
		showcardButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cardln.getVisibility()==View.GONE)
                {
                    cardln.setVisibility(View.VISIBLE);

                }
               ln.setVisibility(View.GONE);
               mainln.setVisibility(View.GONE);
				
			}
		});
		
		updateDataButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new UpdateAccountApi().execute();
			}
		
		});
		
		
		
		mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_account;
        getSupportActionBar().setTitle(mActivityTitle);
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();
        
        SharedPreferences shared = getSharedPreferences("MyPref", 0);
        cr = (shared.getString("csrftoken", ""));
        mykey = (shared.getString("key", ""));
        sessionString = (shared.getString("session", ""));
        
        new CallApi().execute();
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Categories", "My Cart", "My Account", "FAQ", "Log Out" };
        mAdapter = new DrawerAdapter(this, menuArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(AccountActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(AccountActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(AccountActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(AccountActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(AccountActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(AccountActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(AccountActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.account, menu);
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
		getSupportActionBar().setTitle(R.string.title_activity_account);
		invalidateOptionsMenu();
    }
    
class CallApi extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			HttpClient httpclient = DefaultHttp.getInstance();
			HttpGet httpget = new HttpGet("http://www.theama.info/curbweb/user/?format=json");
			httpget.setHeader("Authorization","Token "+ mykey);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader("X-CSRFToken",cr);
			httpget.setHeader("Cookie","csrftoken="+cr);
			httpget.setHeader("Cookie","sessionid="+sessionString);
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				    
            }
			else{
				Toast.makeText(getApplicationContext(), "Register error.Try again!", Toast.LENGTH_SHORT).show();
			}
		}

	}

class UpdateAccountApi extends AsyncTask<Void, Void, Boolean> {
	
	private String uname;

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.
		HttpClient httpputclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut("http://www.theama.info/curbweb/user/?format=json");
		httpput.setHeader("Authorization","Token "+ mykey);
		//httpput.setHeader("Content-Type", "application/json");
		httpput.setHeader("X-CSRFToken",cr);
		httpput.setHeader("Cookie","csrftoken="+cr);
		httpput.setHeader("Cookie","sessionid="+sessionString);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		EditText editTextName = (EditText)findViewById(R.id.nameinput);
		nameValuePairs.add(new BasicNameValuePair("username", editText1.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("email", editText2.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("first_name", editTextName.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("last_name", editText4.getText().toString()));
		try {
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        HttpResponse response2 = null;
		try {
			response2 = httpputclient.execute(httpput);
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
			json2 = EntityUtils.toString(response2.getEntity());
			
			JSONObject object = new JSONObject(json2);
			//if(object.isNull("username")){
			//	return false;
			//}
			//else{
				return true;
			//}
			
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
			//Intent intent = new Intent(AccountActivity.this, MainScreenActivity.class);
		    //startActivity(intent);
			try {
				Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
        }
		else{
			Toast.makeText(getApplicationContext(), "Register error.Try again!", Toast.LENGTH_SHORT).show();
		}
	}

}

class PassResset	 extends AsyncTask<Void, Void, Boolean> {
	
	private String uname;

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.
		HttpClient httppostclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.theama.info/curbweb/rest-auth/password/reset/");
		httppost.setHeader("Authorization","Token "+ mykey);
		//httpput.setHeader("Content-Type", "application/json");
		httppost.setHeader("X-CSRFToken",cr);
		httppost.setHeader("Cookie","csrftoken="+cr);
		httppost.setHeader("Cookie","sessionid="+sessionString);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		EditText editTextName = (EditText)findViewById(R.id.emailpassinput);
		nameValuePairs.add(new BasicNameValuePair("email", editTextName.getText().toString()));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        HttpResponse response2 = null;
		try {
			response2 = httppostclient.execute(httppost);
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
			json2 = EntityUtils.toString(response2.getEntity());
			
			JSONObject object = new JSONObject(json2);
			if(object.isNull("success")){
				return false;
			}
			else{
				return true;
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
			//Intent intent = new Intent(AccountActivity.this, MainScreenActivity.class);
		    //startActivity(intent);
			try {
				Toast.makeText(getApplicationContext(), "Pass reset instructions on your email!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
        }
		else{
			Toast.makeText(getApplicationContext(), "Pass reset error.Try again!", Toast.LENGTH_SHORT).show();
		}
	}

}


}

