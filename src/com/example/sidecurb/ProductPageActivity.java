package com.example.sidecurb;

import java.io.InputStream;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sidecurb.LoginActivity2.CallApi;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ProductPageActivity extends ActionBarActivity{
	private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
    private int mActivityTitle;
	private String sku;
	private String name;
	private String url;
	private String description;
	private String price;
	private String photo;
	private String categ;
	private String sid;
	private Button buy;
	private Intent intent;
	private String cart;
	private String shop;
	private String quantity;
	private String curshop;
	private int langSelected = -1;
	final Context context = this;
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(langSelected==-1)	
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_page);
		//spinner = (ProgressBar)findViewById(R.id.progress);
		//spinner.setVisibility(View.VISIBLE);	
	 	mDrawerList = (ListView)findViewById(R.id.navList);
	 	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle =  R.string.title_activity_product_page;
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        intent = getIntent();
        sku = intent.getStringExtra("sku");
        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        photo = intent.getStringExtra("photo");
        categ = intent.getStringExtra("categ");
        sid = intent.getStringExtra("sid");
        
        TextView prodDescription = (TextView)findViewById(R.id.description);
        prodDescription.setText(description);
        TextView prodPrice = (TextView)findViewById(R.id.price);
        prodPrice.setText(price);
        ImageView imageView  = (ImageView) findViewById(R.id.image);
        new DownloadImageTask(imageView).execute(photo);
        buy = (Button) findViewById(R.id.cartbtn);
	    buy.setOnClickListener(new OnClickListener()
	    {
			  public void onClick(View v)
			  {
				  	EditText qnt = (EditText)findViewById(R.id.qnt);
			        quantity = (String) qnt.getText().toString();
			        if(quantity.isEmpty() || quantity.equals(null)){
			        	quantity = "1";
			        }
				  	pref = getApplicationContext().getSharedPreferences("MyPref", 0);
				  	shop = pref.getString("shop",null);
				  	curshop = pref.getString("curshop",null);
				  	cart = pref.getString("cart", null);
				  	sid = curshop;
				  	if(shop!="" && !shop.equals(curshop)){
				  		AlertDialog alertCart = new AlertDialog.Builder(context)
							.setTitle("Delete entry")
							.setMessage(R.string.alertcart)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					  	        public void onClick(DialogInterface dialog, int which) { 
					  	            cart = "[]";
					  	            addToCart(quantity, cart);
					  	        }
							})
							.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					  	        public void onClick(DialogInterface dialog, int which) { 
					  	        	ProductPageActivity.this.finish();
					  	        }
					  	     })
					  	     .setIcon(android.R.drawable.ic_dialog_alert)
					  	     .show();
				  	}
				  	else{
				  		addToCart(quantity, cart);
				  	}
			  }
	    });
	}
	
	private void addDrawerItems() {
        String[] menuArray = { "Stores", "Orders", "My Cart", "My Account", "FAQ", "Log Out" };
        mAdapter = new DrawerAdapter(this, menuArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent ;
            	langSelected = -1;
            	if(position==0){
            		intent = new Intent(ProductPageActivity.this, MainScreenActivity.class);
            	}
            	else if(position == 1){
            		intent = new Intent(ProductPageActivity.this, OrdersActivity.class);
            	}
            	else if(position == 2){
            		intent = new Intent(ProductPageActivity.this, CartActivity.class);
            	}
            	else if(position == 3){
            		intent = new Intent(ProductPageActivity.this, AccountActivity.class);
            	}
            	else if(position == 4){
            		intent = new Intent(ProductPageActivity.this, FAQActivity.class);
            	}
            	else{
            		intent = new Intent(ProductPageActivity.this, MainActivity.class);
            	}
				startActivity(intent);
            }
        });
    }
	
	private void addToCart(String quantity, String cart){
		int flag = -1;
	  	try {
	  		JSONArray cartList =  new JSONArray(cart);
	  		JSONObject prodObj= new JSONObject();
	  		if(cartList.length() > 0){
	  			for (int i = 0; i < cartList.length(); i++) {
	  				String tempUrl = cartList.getJSONObject(i).getString("url");
	  				if(tempUrl.equals(url)){
	  					String tempQnt = cartList.getJSONObject(i).getString("qnt");
	  					int newQnt  = Integer.parseInt(tempQnt) + Integer.parseInt(quantity);
	  					cartList.getJSONObject(i).put("qnt", ""+newQnt+"");
	  					flag = i;
	  				}
	  			}
	  			if(flag < 0){
	  				prodObj.put("sku", sku);
				  	prodObj.put("name", name);
				  	prodObj.put("url", url);
				  	prodObj.put("photo", photo);
					prodObj.put("price", price);
					prodObj.put("qnt", quantity);
					cartList.put(prodObj);
	  			}
	  		}
	  		else{
		  		prodObj.put("sku", sku);
			  	prodObj.put("name", name);
			  	prodObj.put("url", url);
			  	prodObj.put("photo", photo);
				prodObj.put("price", price);
				prodObj.put("qnt", quantity);
				cartList.put(prodObj);
	  		}
			cart = cartList.toString();
			Editor editor = pref.edit();
			editor.remove("cart");
			editor.putString("cart", cart);
			editor.putString("shop", sid);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupDrawer() {
			mDrawerToggle = new ActionBarDrawerToggle(ProductPageActivity.this, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close ) {

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
        getMenuInflater().inflate(R.menu.product_page, menu);
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

    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
     	  ImageView bmImage;

     	  public DownloadImageTask(ImageView bmImage) {
     	      this.bmImage = bmImage;
     	  }

     	  protected Bitmap doInBackground(String... urls) {
     	      String urldisplay = urls[0];
     	      Bitmap mIcon11 = null;
     	      try {
     	        InputStream in = new java.net.URL(urldisplay).openStream();
     	        mIcon11 = BitmapFactory.decodeStream(in);
     	      } catch (Exception e) {
     	          Log.e("Error", e.getMessage());
     	          e.printStackTrace();
     	      }
     	      return mIcon11;
     	  }

     	  protected void onPostExecute(Bitmap result) {
     		  bmImage.setImageBitmap(result);
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
		getSupportActionBar().setTitle(R.string.title_activity_product_page);
		invalidateOptionsMenu();
    }
}
