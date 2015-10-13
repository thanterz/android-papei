package com.example.sidecurb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class CategoryAdapter extends ArrayAdapter<Category> {
 
        private final Context context;
        private final ArrayList<Category> categoriesArrayList;
 
        public CategoryAdapter(Context context, ArrayList<Category> categoriesArrayList) {
 
            super(context, R.layout.categoryrow, categoriesArrayList);
 
            this.context = context;
            this.categoriesArrayList = categoriesArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.shoprow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView category = (TextView) rowView.findViewById(R.id.name);
            category.setText(categoriesArrayList.get(position).getName());
            
            return rowView;
        }
}


