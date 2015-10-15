package com.example.sidecurb;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ShopsAdapter extends ArrayAdapter<Shop> {
 
        private final Context context;
        private final ArrayList<Shop> shopsArrayList;
 
        public ShopsAdapter(Context context, ArrayList<Shop> shopsArrayList) {
 
            super(context, R.layout.shoprow, shopsArrayList);
 
            this.context = context;
            this.shopsArrayList = shopsArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.shoprow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView addressView = (TextView) rowView.findViewById(R.id.address);
            TextView nameView = (TextView) rowView.findViewById(R.id.name);
            TextView distanceView = (TextView) rowView.findViewById(R.id.distance);
            TextView timeView = (TextView) rowView.findViewById(R.id.time);
            ImageView imageView  = (ImageView) rowView.findViewById(R.id.image);
            // 4. Set the text for textView 
            addressView.setText(shopsArrayList.get(position).getAddress());
            nameView.setText(shopsArrayList.get(position).getName());
            distanceView.setText(shopsArrayList.get(position).getDistance());
            timeView.setText("Ready in 2 hours!");
            new DownloadImageTask(imageView).execute(shopsArrayList.get(position).getLogo());
            
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

