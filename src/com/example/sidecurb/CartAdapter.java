package com.example.sidecurb;

import java.io.InputStream;
import java.util.ArrayList;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
 
public class CartAdapter extends ArrayAdapter<Product> {

        private final Context context;
        private final ArrayList<Product> productsArrayList;
 
        public CartAdapter(Context context, ArrayList<Product> productsArrayList) {
 
            super(context, R.layout.cartrow, productsArrayList);
 
            this.context = context;
            this.productsArrayList = productsArrayList;
        }
 
        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.cartrow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView name = (TextView) rowView.findViewById(R.id.name);
            name.setText(productsArrayList.get(position).getName().toString());

            TextView price = (TextView) rowView.findViewById(R.id.price);
            price.setText(productsArrayList.get(position).getPrice()+"€");
            
            EditText qnt = (EditText) rowView.findViewById(R.id.qnt);
            qnt.setText(productsArrayList.get(position).getQnt());
            
            TextView sum = (TextView) rowView.findViewById(R.id.sum);
            Float sums = Float.valueOf(productsArrayList.get(position).getPrice()) * Float.valueOf(productsArrayList.get(position).getQnt());
            sum.setText(""+sums+" "+"€");
            
            ImageView imageView  = (ImageView) rowView.findViewById(R.id.image);
            new DownloadImageTask(imageView).execute(productsArrayList.get(position).getPhoto());
            
            ImageView imageDlt  = (ImageView) rowView.findViewById(R.id.dlt);
            imageDlt.setOnClickListener(new OnClickListener() {
            	
                @Override
                public void onClick(View v) {
                	//Product s = productsArrayList.remove(position);   
                }
            });
            
            return rowView;
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
}


