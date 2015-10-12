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
 
public class FAQAdapter extends ArrayAdapter<FAQ> {
 
        private final Context context;
        private final ArrayList<FAQ> FAQArrayList;
 
        public FAQAdapter(Context context, ArrayList<FAQ> FAQArrayList) {
 
            super(context, R.layout.faqrow, FAQArrayList);
 
            this.context = context;
            this.FAQArrayList = FAQArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.faqrow, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView questionView 	= (TextView) rowView.findViewById(R.id.question);
            TextView answerView 	= (TextView) rowView.findViewById(R.id.answer);
            // 4. Set the text for textView 
            questionView.setText(FAQArrayList.get(position).getQuestion());
            answerView.setText(FAQArrayList.get(position).getAnswer());
            
            return rowView;
        }
}

