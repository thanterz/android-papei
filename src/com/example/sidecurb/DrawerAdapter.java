package com.example.sidecurb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class DrawerAdapter extends ArrayAdapter<String> {
 
        private final Context context;
        private final String[] DrawerArrayList;

		public  DrawerAdapter(Context context, String[] menuArray) {
 
            super(context, R.layout.drawerrow, menuArray);
 
            this.context = context;
            this.DrawerArrayList = menuArray;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.drawerrow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView drawerAction = (TextView) rowView.findViewById(R.id.name);
            
            ImageView iconDrawer = (ImageView) rowView.findViewById(R.id.imageIcon);
            if(position == 0){
            	drawerAction.setText(R.string.title_activity_main_screen);
            	iconDrawer.setImageResource(R.drawable.store);
            }
            else if (position == 1){
            	drawerAction.setText(R.string.title_activity_orders);
            	iconDrawer.setImageResource(R.drawable.ordericon);
            }
            else if (position == 2){
            	drawerAction.setText(R.string.title_activity_cart);
            	iconDrawer.setImageResource(R.drawable.fullcart);	
            }
            else if (position == 3){
            	drawerAction.setText(R.string.title_activity_account);
            	iconDrawer.setImageResource(R.drawable.kalathi);
            }
            else if (position == 4){
            	drawerAction.setText(R.string.title_activity_faq);
            	iconDrawer.setImageResource(R.drawable.redicon);
        	}
            else if(position == 5){
            	drawerAction.setText(R.string.logout);
            	iconDrawer.setImageResource(R.drawable.exit);
            }
            
            return rowView;
        }
}


