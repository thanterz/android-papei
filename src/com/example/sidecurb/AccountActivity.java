package com.example.sidecurb;

import java.util.Locale;

import org.apache.http.HttpEntity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AccountActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private HttpEntity json;
	private int langSelected = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_account;
        getSupportActionBar().setTitle(mActivityTitle);
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Categories", "My Cart", "My Account", "FAQ" };
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
            		intent = new Intent(AccountActivity.this, CategoriesActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(AccountActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(AccountActivity.this, AccountActivity.class);
            	}
            	else{
            		intent = new Intent(AccountActivity.this, FAQActivity.class);
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
}

