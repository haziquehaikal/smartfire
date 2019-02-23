package com.picotech.smartfire.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;


import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.app.Env;
import com.picotech.smartfire.models.DeviceModel;
import com.picotech.smartfire.models.FireLogModels;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Activity_SelectDevice extends AppCompatActivity {

    public ListView devicelist;
    public ArrayList<DeviceModel> model;
    public DeviceAdapter adapter;
    public NetworkPreferences networkPreferences;
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist_view);
        getSupportActionBar().setTitle("Fire Log");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.firelog);

        devicelist = findViewById(R.id.devicelist);
        model = new ArrayList<>();
        adapter = new DeviceAdapter(getApplicationContext(), model);
        devicelist.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        devicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Activity_SelectDevice.this, Activity_Log.class);
                intent.putExtra("deviceid", model.get(position).devicename);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

    public class DeviceAdapter extends BaseAdapter {

        public ArrayList<DeviceModel> list;
        public Context context;


        public DeviceAdapter(Context activity, ArrayList<DeviceModel> list) {
            this.context = activity;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }


        public String getDeviceId(int position) {

            return list.get(position).devicename;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //init the view
            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.dlist_row_view, parent, false);
            }

            //settext based on the view (ikutlah klo ade textview lagi then , add more
            TextView name = convertView.findViewById(R.id.textView_name);
            name.setText(getDeviceId(position));

//            TextView image_data = convertView.findViewById(R.id.textView_name);
//            name.setText(getDeviceId(position));
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        model.clear();
        new getDeviceList().execute();
    }


    public class getDeviceList extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            Endpoints endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
            networkPreferences = new NetworkPreferences(endpoints.getDeviceListApi());
            SessionPreferences sessionPreferences = new SessionPreferences(getApplicationContext());
            //if dah login proper , uncomment this part
             String id = sessionPreferences.getUserDetails().get("user_id");
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", id);
            } catch (JSONException e) {
                String msg = e.getMessage();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Log.e("ERR_JSON_GETLOG", msg);
            }

        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            return networkPreferences.postData(jsonObject);
        }


        @Override
        protected void onPostExecute(Boolean stat) {

            if (stat) {
                try {
                    JSONObject response = new JSONObject(networkPreferences.getHttpResponse());
                    if (!response.getBoolean("error")) {
                        JSONArray logdata = response.getJSONArray("data");
                        for (int i = 0; i < logdata.length(); i++) {
                            DeviceModel obj = new DeviceModel(
                                    logdata.getJSONObject(i).getString("firedevice_id")
                            );
                            model.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"),
                                Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (IOException e) {
                    String msg = e.getMessage();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Log.e("ERR_IO_GETLOG", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
