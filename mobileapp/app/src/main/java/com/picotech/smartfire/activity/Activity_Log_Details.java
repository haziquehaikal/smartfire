package com.picotech.smartfire.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.picotech.smartfire.R;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Activity_Log_Details extends AppCompatActivity {

    public TextView tv_reading_smoke, tv_reading_gas,
            tv_reading_lpg, tv_reading_temp; //iv_image_data;
    public ImageView iv_image_data;
    private String image_data, reading_smoke, reading_gas,
            reading_lpg, reading_temp;
    private SessionPreferences sessionPreferences;
    private NetworkPreferences networkPreferences;
    private ProgressDialog progressDialog;
    private JSONObject getreading;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.diplay_log_details);
        getSupportActionBar().setTitle("Fire Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_reading_lpg = (TextView) findViewById(R.id.reading_lpg);
        tv_reading_smoke = (TextView) findViewById(R.id.reading_smoke);
        tv_reading_gas = (TextView) findViewById(R.id.reading_gas);
        tv_reading_temp = (TextView) findViewById(R.id.reading_temp);
        iv_image_data = (ImageView) findViewById(R.id.image_data);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        tv_reading_lpg.setText(bundle.getString("lpg"));
        tv_reading_smoke.setText(bundle.getString("smk"));
        tv_reading_temp.setText(bundle.getString("temp"));
        //iv_image_data.setImageResource(Integer.parseInt((bundle.getString("imgdata"))));
        tv_reading_gas.setText(bundle.getString("gas"));


        byte[] decodedString = Base64.decode(bundle.getString("imgdata"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv_image_data.setImageBitmap(decodedByte);


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

    public void onResume() {
        super.onResume();

//        new doActivityGetReadings().execute();
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
//    public class doActivityGetReadings extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process
//        JSONObject data;
//
//        @Override
//        protected void onPreExecute() { //prepare initialize == constructor
//            //showDialog();
//            startPd();
//            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
//            networkPreferences = new NetworkPreferences(endpoints.getreadings()); //amik login api, initialize np object
//            String id = sessionPreferences.getUserDetails().get("user_id");
//            Bundle bundle = getIntent().getExtras();
//            data = new JSONObject();
//            try {
//                data.put("user_id", id);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) { //
//
//            if (networkPreferences.postData(data)) {
//                //closeDialog();
//                return true;
//            } else {
//                // closeDialog();
//                createMessage(networkPreferences.getErrorMessage()).show();
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean stat) { //rcv boolean, after execute process complete
//            stopPd();
//            if (stat) {
//                try {
//                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse()); //baca
//                    if (!resdata.getBoolean("error")) {
//                        tv_reading_gas.setText(resdata.getString("reading_gas"));
//                        tv_reading_lpg.setText(resdata.getString("reading_lpg"));
//                        tv_reading_smoke.setText(resdata.getString("reading_smoke"));
//                        tv_reading_temp.setText(resdata.getString("reading_temp"));
//                        tv_image_data.setText(resdata.getString("image_data"));
//                    } else {
//                        createMessage(resdata.getString("message")).show();
//                    }
//                } catch (IOException e) {
//                    String msg = e.getMessage();
//                    createMessage("ERR_IO_GETDETAIL: " + msg).show();
//                    Log.e("ERR_IO_GETDETAI", "MSG: " + e.getMessage());
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    String msg = e.getMessage();
//                    createMessage("ERR_JSON_GETDETAI_SECOND: " + msg).show();
//                    Log.e("ERR_JSON_GETDTLS_SECOND", "MSG: " + e.getMessage());
//                }
//            }
//        }
//
//    }
}
