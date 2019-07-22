package com.bufferchime.agriculturefact.Notespack;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bufferchime.agriculturefact.R;

import java.util.ArrayList;

public class noteadaptor extends ArrayAdapter<note> {

    Context context;
    private ArrayList<note> notes1;


    public noteadaptor(Context context, int textViewResourceId, ArrayList<note> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.notes1 = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_row, null);
        }
        note o = notes1.get(position);
        if (o != null) {
            //  TextView pos = (TextView) v.findViewById(R.id.position);
            TextView city = (TextView) v.findViewById(R.id.tv_cityname);


            //  pos.setText(String.valueOf(o.getPosition()));
            city.setText(String.valueOf(o.getName()));
        }
        return v;
    }
}
