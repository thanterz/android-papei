package com.example.sidecurb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FAQActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private HttpEntity json;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "FAQ";
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();
        try {
			createFAQList();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
            	if(position==0){
            		intent = new Intent(FAQActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(FAQActivity.this, CategoriesActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(FAQActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(FAQActivity.this, AccountActivity.class);
            	}
            	else{
            		intent = new Intent(FAQActivity.this, FAQActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(FAQActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
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
        getMenuInflater().inflate(R.menu.faq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void createFAQList () throws Throwable{
    	FAQAdapter adapter = new FAQAdapter(this, generateData());
		 
        ListView listView = (ListView) findViewById(R.id.faqList);
 
        listView.setAdapter(adapter);
    }
 
    private ArrayList<FAQ> generateData() throws JSONException, Throwable{
    	
		ArrayList<FAQ> FAQ = new ArrayList<FAQ>();
	    FAQ.add(new FAQ("How does Curbside work?", "The Curbside app allows you to find and buy products available at nearby stores such as Target. When your order is ready to pick up, we notify you."));
	    FAQ.add(new FAQ("What does it cost?", "Curbside is free and we never mark up from the retailer."));
	    FAQ.add(new FAQ("Where is Curbside available?", "Currently Curbside is available in the San Francisco Bay Area, New Jersey/New York, and LA/Glendale areas."));
	    FAQ.add(new FAQ("Can I use my club / loyalty card for the retailer I’m purchasing from?", "We support selected Target REDcards (see below)."));
	    FAQ.add(new FAQ("How do I cancel an order?", "If you need to cancel an order please email Curbside customer service and we’ll help you cancel it."));
	    FAQ.add(new FAQ("Can I make changes to pickups after I’ve placed them?", "Once an order is placed, no changes can be made. If you have a problem with an order please email Curbside customer service."));
	    FAQ.add(new FAQ("Can I change the pickup location after my pickup has been placed?", "Not at this time. If you need to cancel an order please email Curbside customer service and we’ll cancel it for you."));
	    FAQ.add(new FAQ("Can I have someone else pick up for me?", "Many of our locations allow you to have someone pickup on your behalf. Before placing a pickup, click the Handoff link to complete the setup. The person you select needs a Curbside account and will be asked to show photo ID at time of pickup."));
	    
        return FAQ;
    }
}

