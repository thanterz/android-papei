package com.example.sidecurb;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends ActionBarActivity {

	private int langSelected = -1;
    private int mActivityTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mActivityTitle = R.string.title_activity_register;
        getSupportActionBar().setTitle(mActivityTitle);
		TextView register =  (TextView)findViewById(R.id.loginbtn);
        
        register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
				Intent intent = new Intent(RegisterActivity.this, MainScreenActivity.class);
				startActivity(intent);
			}
		});
		
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
