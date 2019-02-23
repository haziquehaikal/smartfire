package com.picotech.smartfire.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.picotech.smartfire.R;
import com.picotech.smartfire.adapters.Adapter_FireLog;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.models.FireLogModels;
import com.picotech.smartfire.utils.NetworkPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_FireLog extends AppCompatActivity {

    public ListView listView;
    public FireLogModels fireLogModels;
    public Adapter_FireLog adapter;
    public ArrayList<FireLogModels> item;
    private ProgressDialog progressDialog;
    private NetworkPreferences networkPreferences;
    public JSONObject jsonObject;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firelog_view);

        getSupportActionBar().setTitle("Fire Log");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.firelog);

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);


        //listiew

        //dummy data
        String state[] = {"Absence of Fire"};
        String time[] = {"3:00"};
        String image_data[] = {"Normal"};

        //add item to arraylist (and push to adapter of listvview)
        item = new ArrayList<>();
//        for (int i = 0; i < state.length; i++) {
//            item.add(new FireLogModels(time[i], state[i], image_data[i]));
//        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(Activity_FireLog.this, Activity_Log_Details.class);
                    startActivity(intent);
                } else if (i == 1) {
                    Toast.makeText(new Activity_FireLog(), "Buzzer has been turned off.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        adapter = new Adapter_FireLog(getApplicationContext(), item);
        listView.setAdapter(adapter);

        //notify if data changes (klo pull dri network)
        adapter.notifyDataSetChanged();

    }

    public void startPd() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
    public void stopPd() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getLogData().execute();
    }


    public class getLogData extends AsyncTask<Void, Void, Boolean> {


        protected void onPreExecute() { //prepare initialize == constructor
            startPd();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
//            Log.d("RESDSDS",endpoints.getLogApi());
            networkPreferences = new NetworkPreferences(endpoints.getLogApi());
            jsonObject = new JSONObject();
            try {
                jsonObject.put("device_id", "device123");
            } catch (JSONException e) {
                String msg = e.getMessage();
                Log.d("ERR_JSON_GETLOG", msg);
            }

        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            if (networkPreferences.getData()) {

                return true;
            }
            return false;

        }


        @Override
        protected void onPostExecute(Boolean stat) {

            stopPd();

            try {
                Log.d("RESSSS", "" + networkPreferences.getHttpResponse());
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (stat) {
//                try {
//                    JSONObject res = new JSONObject(networkPreferences.getHttpResponse());
//                    if (!res.getBoolean("error")) {
//                        JSONArray loglist = res.getJSONArray("data");
//                        for (int i = 0; i < loglist.length(); i++) {
//                            FireLogModels log = new FireLogModels(
//                                    loglist.getJSONObject(i).getString("triggred_at")
//                                    , loglist.getJSONObject(i).getString("alert_level")
//                                    , loglist.getJSONObject(i).getString("image_data")
//                            );
//                            item.add(log);
//                        }
//                        adapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                jsonObject.getString("message"),
//                                Toast.LENGTH_LONG).show();
//                    }
//                } catch (IOException e) {
//                    Log.d("RESDSDS", "" + e.getMessage());
//                } catch (JSONException e) {
//                    Log.d("sdsdsdsd", "" + e.getMessage());
//                }
            }


        }
    }
}

