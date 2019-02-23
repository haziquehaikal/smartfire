package com.picotech.smartfire.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.picotech.smartfire.R;
import com.picotech.smartfire.models.FireLogModels;

import java.util.ArrayList;

public class Adapter_FireLog extends BaseAdapter {

    public Context _context;
    public FireLogModels models;
    public ArrayList<FireLogModels> _items;

    //construture
    public Adapter_FireLog(Context context, ArrayList<FireLogModels> items) {
        this._context = context;
        this._items = items;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //dapatkanlog name (cn add more func like getLogTime  or getLogState))
    public String getLogName(int position) {
        return _items.get(position).state;
    }

    public String getTime(int position) {

        return _items.get(position).time;
    }

    public String getImageData (int position) {
        return _items.get(position).image_data;
    }

    public String getReadingSmoke (int position) {
        return _items.get(position).reading_smoke;
    }

    public String getReadingGas (int position) {
        return _items.get(position).reading_gas;
    }

    public String getReadingLPG (int position) {
        return _items.get(position).reading_lpg;
    }

    public String getReadingTemp (int position) {
        return _items.get(position).reading_temp;
    }

    //setup view for listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //init the view
        if (convertView == null) {
            convertView = LayoutInflater.from(_context).inflate(R.layout.list_row_view, parent, false);
        }

        //settext based on the view (ikutlah klo ade textview lagi then , add more

        TextView name = convertView.findViewById(R.id.textView_name);
        name.setText(getTime(position));

        return convertView;
    }
}
