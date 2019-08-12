package com.bufferchime.agriculturefact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdaptor extends ArrayAdapter<NewsClass> {

    Context context;
    private ArrayList<NewsClass> ingots;


    public NewsAdaptor(Context context, int textViewResourceId, ArrayList<NewsClass> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.ingots = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.new_list_news_row, null);
        }
        NewsClass o = ingots.get(position);
        if (o != null) {
            //  TextView pos = (TextView) v.findViewById(R.id.position);

            TextView price = (TextView) v.findViewById(R.id.tv_price);
            TextView city = (TextView) v.findViewById(R.id.tv_cityname);
            ImageView ud =(ImageView)  v.findViewById(R.id.imageView3);


            // pos.setText(String.valueOf(o.getPosition()));
            price.setText(String.valueOf(o.getDate2()));
            city.setText(String.valueOf(o.getName()));
            Picasso.with(context).load(String.valueOf(o.getDate())).into(ud);





        }
        return v;
    }
}
