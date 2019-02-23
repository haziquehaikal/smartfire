package com.picotech.smartfire.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ListView;


import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_FireStatus extends AppCompatActivity {

    private TextView tv_reading_lpg, tv_reading_smoke, tv_reading_gas;
    private String reading_heat, reading_smoke, reading_gas;
    //private Button turnoffbuzzer;
    private SessionPreferences sessionPreferences;
    private NetworkPreferences networkPreferences;
    private ProgressDialog progressDialog;
    private JSONObject getreading;
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_firestatus);
        getSupportActionBar().setTitle("Fire Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.firelog);

        tv_reading_lpg = (TextView) findViewById(R.id.reading_lpg);
        tv_reading_smoke = (TextView) findViewById(R.id.reading_smoke);
        tv_reading_gas = (TextView) findViewById(R.id.reading_gas);

        sessionPreferences = new SessionPreferences(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

    }

    public void startPd() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
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


    public void getreading() {
        //initialize();

        // onGetReadingSuccess();
    }

    public void onResume() {
        super.onResume();
        new doActivityGetReadings().execute();
    }


    public void stopPd() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    public class doActivityGetReadings extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process
        JSONObject data;

        @Override
        protected void onPreExecute() { //prepare initialize == constructor
            //showDialog();
            startPd();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.getreadings()); //amik login api, initialize np object
            String id = sessionPreferences.getUserDetails().get("user_id");
            data = new JSONObject();
            try {
                data.put("user_id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) { //

            if (networkPreferences.postData(data)) {
                //closeDialog();
                return true;
            } else {
                // closeDialog();
                createMessage(networkPreferences.getErrorMessage()).show();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean stat) { //rcv boolean, after execute process complete
            stopPd();
            if (stat) {
                try {
                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse()); //baca
                    if (!resdata.getBoolean("error")) {
                        tv_reading_gas.setText(resdata.getString("reading_gas"));
                        tv_reading_lpg.setText(resdata.getString("reading_lpg"));
                        tv_reading_smoke.setText(resdata.getString("reading_smoke"));
                    } else {
                        createMessage(resdata.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_IO_LOGIN: " + msg).show();
                    Log.e("ERR_IO_LOGIN", "MSG: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_JSON_LOGIN_SECOND: " + msg).show();
                    Log.e("ERR_JSON_LOGIN_SECOND", "MSG: " + e.getMessage());
                }
            }
        }

    }
}