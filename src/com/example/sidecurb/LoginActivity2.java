package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
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

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class LoginActivity2 extends ActionBarActivity {
	
	private EditText emailString;
	private EditText passwordString;
	private Button loginbtn;
	private int langSelected = -1;
	private int mActivityTitle;
	private String json,json2;
	private Cookie cook; 
	private Cookie cook2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
        mActivityTitle = R.string.title_activity_login_activity2;
        getSupportActionBar().setTitle(mActivityTitle);
		emailString = (EditText) findViewById(R.id.emailinput);
		passwordString = (EditText) findViewById(R.id.passwordinput);
	    loginbtn = (Button) findViewById(R.id.loginbtn);
	    loginbtn.setOnClickListener(new OnClickListener()
	    {
			  public void onClick(View v)
			  {
				  new CallApi().execute();
				  	
			  }
	    });
	}
	
	class CallApi extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
		    HttpClient httpClient=DefaultHttp.getInstance();
			HttpPost httppost = new HttpPost("http://www.theama.info/curbweb/rest-auth/login/");
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("username", emailString.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password", passwordString.getText().toString()));
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            HttpResponse response = null;
            List<Cookie> cookies  = null;
			try {
				response = httpClient.execute(httppost, context);
				cookies = cookieStore.getCookies();
				
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
            try {
            	cook = cookies.get(0);
            	cook2 = cookies.get(1);
            	
				json = EntityUtils.toString(response.getEntity());
				if(json.indexOf("key")>-1){
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
			}
            
            return null;

		}
		

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success.equals(true)){
				try {
					String key = null;
					json = "[" + json + "]";
					JSONArray keyList =  new JSONArray(json);
					for (int i = 0; i < keyList.length(); i++) {
					    key = keyList.getJSONObject(i).getString("key");
					}
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
					Editor editor = pref.edit();
					editor.putString("key", key);
					editor.putString("cart", "[]");
					editor.putString("csrftoken", cook.getValue());
					editor.putString("sessionid", cook2.getValue());
					editor.commit();
					//Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(LoginActivity2.this, MainScreenActivity.class);
				    startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			else{
				Toast.makeText(getApplicationContext(), "Login error.Try again!", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.english) {
        	updateconfig("en");
        }
        else if(id == R.id.greek){
        	updateconfig("el");
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
		getSupportActionBar().setTitle(R.string.title_activity_login_activity2);
		invalidateOptionsMenu();
    }
}