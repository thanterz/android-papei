package com.example.sidecurb;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView.OnItemClickListener;

public class CartActivity extends ActionBarActivity {

	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
    private HttpEntity json;
	private int langSelected = -1;
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = R.string.title_activity_cart;
        getSupportActionBar().setTitle(mActivityTitle);
        //new CallApi().execute();
        addDrawerItems();
        setupDrawer();
        try {
			createProductsList();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
            		intent = new Intent(CartActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(CartActivity.this, CategoriesActivity.class);
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
	}
	
	private ArrayList<Product> generateData() throws JSONException, Throwable{
		
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	  	String cart = pref.getString("cart", null);
		
	  	JSONArray productsList =  new JSONArray(cart);
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		for (int i = 0; i < productsList.length(); i++) {
		    //JSONObject shopList = shopsList.getJSONObject(i);
		    String id = String.valueOf(i);// productsList.getJSONObject(i).getString("id");
		    String sku = productsList.getJSONObject(i).getString("sku");
	        String name = productsList.getJSONObject(i).getString("name");
	        String url = productsList.getJSONObject(i).getString("url");
	        String price = productsList.getJSONObject(i).getString("price");
		    String photo = productsList.getJSONObject(i).getString("photo");
	        String qnt = productsList.getJSONObject(i).getString("qnt");
	        
	        Product prod = new Product(id,sku,name,url,price,"",photo,"","",qnt); 
	        products.add(prod);
		}
	    return products;
	}
}

