package com.example.sidecurb;

import java.util.ArrayList;

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
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.drawerrow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView drawerAction = (TextView) rowView.findViewById(R.id.name);
            drawerAction.setText(DrawerArrayList[position]);
            ImageView iconDrawer = (ImageView) rowView.findViewById(R.id.imageIcon);
            iconDrawer.setImageResource(R.drawable.redicon);
            
            return rowView;
        }
}


