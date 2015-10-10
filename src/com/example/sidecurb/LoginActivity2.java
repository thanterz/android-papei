package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class LoginActivity2 extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
		
		String emailString = getResources().getString(R.id.emailinput);
		
		
		class CallApi extends AsyncTask<Void, Void, Boolean> {
			
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO: attempt authentication against a network service.
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://www.theama.info/curbweb/rest-auth/login/");
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
				nameValuePair.add(new BasicNameValuePair("username", "thanterz"));
				nameValuePair.add(new BasicNameValuePair("password", "papei"));
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
		            HttpResponse response = httpclient.execute(httppost);
		            // write response to log
		            //Log.i("Http Post Response:skata", response.getEntity());
		            //JSONObject json = new JSONObject(response.getEntity());
		            String json = EntityUtils.toString(response.getEntity());		            
		            //JSONArray temp1 = new JSONArray(json_string);
		            Log.d("jsonobject", json);
		        } catch (ClientProtocolException e) {
		            // Log exception
		            e.printStackTrace();
		        } catch (IOException e) {
		            // Log exception
		            e.printStackTrace();
		        } 
				return null;

			}
			

			@Override
			protected void onPostExecute(final Boolean success) {
				
			}

		}
		new CallApi().execute();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
