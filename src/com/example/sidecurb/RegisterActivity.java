package com.example.sidecurb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.sidecurb.LoginActivity2.CallApi;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.R.string;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

	private EditText usernameText;
	private EditText pass1Text;
	private EditText pass2Text;
	private EditText emailText;
	private String jsonstring;
	private int langSelected = -1;
    private int mActivityTitle;
    private Boolean validBoolean=false;
    private Boolean identicalBoolean = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		usernameText = (EditText)findViewById(R.id.userinput);
		pass1Text= (EditText)findViewById(R.id.passwordinput1);
		pass2Text = (EditText)findViewById(R.id.passwordinput2);
		emailText = (EditText)findViewById(R.id.emailinput);
		Button register =  (Button)findViewById(R.id.registerbt);
		mActivityTitle = R.string.title_activity_register;
	  
		
		usernameText.addTextChangedListener(new TextWatcher() { 
		    public void afterTextChanged(Editable s) { 
			final String username = usernameText.getText().toString().trim();

		    if (username.length() > 3)
		        { 
		            //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
		            // or
		            //textView.setText("valid email");
		            String uri2 = "@drawable/green_exc";
		            int imageResource = getResources().getIdentifier(uri2, null, getPackageName());

		             ImageView imageview2= (ImageView)findViewById(R.id.excimageuser);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview2.setImageDrawable(res); 
		             Boolean validBoolean=true;
		        }
		        else
		        {

		             //Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
		            //or
		            //textView.setText("invalid email");
		             String uri = "@drawable/red_exc";  // where myresource.png is the file
                     // extension removed from the String

		             int imageResource = getResources().getIdentifier(uri, null, getPackageName());

		             ImageView imageview= (ImageView)findViewById(R.id.excimageuser);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview.setImageDrawable(res); 
		        }
		    } 
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    // other stuffs 
		    } 
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    // other stuffs 
		    } 
		});
		
		
		final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		emailText.addTextChangedListener(new TextWatcher() { 
		    public void afterTextChanged(Editable s) { 
			final String email = emailText.getText().toString().trim();

		    if (email.matches(emailPattern) && s.length() > 0)
		        { 
		            //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
		            // or
		            //textView.setText("valid email");
		            String uri2 = "@drawable/green_exc";
		            int imageResource = getResources().getIdentifier(uri2, null, getPackageName());

		             ImageView imageview2= (ImageView)findViewById(R.id.excimage);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview2.setImageDrawable(res); 
		             Boolean validBoolean=true;
		        }
		        else
		        {
		             //Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
		            //or
		            //textView.setText("invalid email");
		             String uri = "@drawable/red_exc";  // where myresource.png is the file
                     // extension removed from the String

		             int imageResource = getResources().getIdentifier(uri, null, getPackageName());

		             ImageView imageview= (ImageView)findViewById(R.id.excimage);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview.setImageDrawable(res); 
		        }
		    } 
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    // other stuffs 
		    } 
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    // other stuffs 
		    } 
		});
		
		
		
		getSupportActionBar().setTitle(mActivityTitle);        
        register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
				//Intent intent = new Intent(RegisterActivity.this, MainScreenActivity.class);
				//startActivity(intent);
				if(pass1Text.getText().toString().equals( pass2Text.getText().toString())){
					String uri2 = "@drawable/green_exc";
		            int imageResource = getResources().getIdentifier(uri2, null, getPackageName());

		             ImageView imageview2= (ImageView)findViewById(R.id.excimagepass2);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview2.setImageDrawable(res); 
				}
				else{
					String uri2 = "@drawable/red_exc";
		            int imageResource = getResources().getIdentifier(uri2, null, getPackageName());

		             ImageView imageview2= (ImageView)findViewById(R.id.excimagepass2);
		             Drawable res = getResources().getDrawable(imageResource);
		             imageview2.setImageDrawable(res); 
				}
				if(validBoolean==true && identicalBoolean==true){
					new CallApi().execute();
				}
				else{
					Toast.makeText(getApplicationContext(), "check your exclamation marks", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
	}
	
class CallApi extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.theama.info/curbweb/rest-auth/registration/");
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
			nameValuePair.add(new BasicNameValuePair("username", usernameText.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password1", pass1Text.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password2", pass2Text.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("email", emailText.getText().toString()));
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
				jsonstring = EntityUtils.toString(response.getEntity());
				if(jsonstring.indexOf("key")>-1){
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
				Intent intent = new Intent(RegisterActivity.this, MainScreenActivity.class);
			    startActivity(intent);
				//Log.d("register",jsonstring);
            }
			else{
				Toast.makeText(getApplicationContext(), "check your exclamation marks", Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
		getSupportActionBar().setTitle(R.string.title_activity_register);
		invalidateOptionsMenu();
    }
}
