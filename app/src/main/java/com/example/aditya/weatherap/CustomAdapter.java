package com.example.aditya.weatherap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Day> {
    ArrayList<Day> lst=new ArrayList<Day>();
    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        lst=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row, null);
        ImageView img=(ImageView)v.findViewById(R.id.imageView);
        TextView txtvwdt=(TextView)v.findViewById(R.id.txtvwdt);
        TextView txtvwtemp=(TextView)v.findViewById(R.id.txtvwtemp);

        txtvwdt.setText(lst.get(position).getDate());
        txtvwtemp.setText(lst.get(position).getTemp());

        return v;
    }
}
