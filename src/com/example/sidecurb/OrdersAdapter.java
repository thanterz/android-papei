package com.example.sidecurb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrdersAdapter extends ArrayAdapter<Order> {

	private final Context context;
    private final ArrayList<Order> ordersArrayList;

    public OrdersAdapter(Context context, ArrayList<Order> ordersArrayList) {

        super(context, R.layout.orderrow, ordersArrayList);

        this.context = context;
        this.ordersArrayList = ordersArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater 
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.orderrow, parent, false);

        // 3. Get the two text view from the rowView
        TextView ordernoView = (TextView)rowView.findViewById(R.id.orderno);
        TextView pickView = (TextView)rowView.findViewById(R.id.pick);
        TextView dateTextView = (TextView)rowView.findViewById(R.id.dateordered);
        // 4. Set the text for textView 
        ordernoView.setText(ordersArrayList.get(position).getPurchase_no());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
        String time = new Date().toString();
		try {
			time = sdf.parse(ordersArrayList.get(position).getDateString().toString()).toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        dateTextView.setText(time);
        pickView.setText("No pick up yet");
        
        return rowView;
    }

}
