package com.picotech.smartfire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.picotech.smartfire.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Adapter_menu extends BaseAdapter {

    public Context context;
    public ArrayList<String> list;

    public Adapter_menu(Context _context, ArrayList<String> list){
        this.context = _context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public String getMenuTime(int i) {
        return list.get(i).toString();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //init the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_row_view, parent, false);
        }

        //settext based on the view (ikutlah klo ade textview lagi then , add more
        TextView name = convertView.findViewById(R.id.textView_name);
        name.setText(getMenuTime(position));

        return convertView;
    }
}
