package com.picotech.smartfire.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.picotech.smartfire.R;
import com.picotech.smartfire.adapters.Adapter_FireLog;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.app.Env;
import com.picotech.smartfire.models.FireLogModels;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Activity_Log extends AppCompatActivity {

    public ListView listView;
    public Adapter_FireLog adapter;
    public ArrayList<FireLogModels> firelogmnodel;
    public ProgressDialog progressDialog;
    private NetworkPreferences networkPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firelog_view);
        getSupportActionBar().setTitle("Fire Log");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.firelog);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);


        //listiew

        firelogmnodel = new ArrayList<>();
        adapter = new Adapter_FireLog(getApplicationContext(), firelogmnodel);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activity_Log.this, Activity_Log_Details.class);
                intent.putExtra("imgdata", adapter.getImageData(i));
                intent.putExtra("lpg", adapter.getReadingLPG(i));
                intent.putExtra("smk", adapter.getReadingSmoke(i));
                intent.putExtra("gas", adapter.getReadingGas(i));
                intent.putExtra("temp", adapter.getReadingTemp(i));

                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();

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

    public void startPd() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

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
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            Endpoints endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
            networkPreferences = new NetworkPreferences(endpoints.getLogApi());
            Bundle bundle = getIntent().getExtras();
            SessionPreferences sessionPreferences = new SessionPreferences(getApplicationContext());
            String id = sessionPreferences.getUserDetails().get("user_id");
            // Log.d("USR", bundle.getString("deviceid"));
            jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", id);

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
                            FireLogModels obj = new FireLogModels(
                                    logdata.getJSONObject(i).getString("triggred_at"),
                                    logdata.getJSONObject(i).getString("alert_level"),
                                    logdata.getJSONObject(i).getString("image_data"),
                                    logdata.getJSONObject(i).getString("reading_smoke"),
                                    logdata.getJSONObject(i).getString("reading_gas"),
                                    logdata.getJSONObject(i).getString("reading_lpg"),
                                    logdata.getJSONObject(i).getString("reading_temp")

                            );
                            firelogmnodel.add(obj);
                        }
                        adapter.notifyDataSetChanged();
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
