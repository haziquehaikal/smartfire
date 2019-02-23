package com.picotech.smartfire.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.utils.SessionPreferences;
import com.picotech.smartfire.utils.NetworkPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_UpdateProfile extends AppCompatActivity {

    private EditText tv_name, tv_phone_no, tv_address; //tv_password;
    private String name, phone_no, address; //password;
    private ProgressDialog progressDialog;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private JSONObject updateprofile;
    public ListView listView;


    private Button savedetails, cancelsavedetails;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.updateprofile);

        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.firelog);

        tv_name = (EditText) findViewById(R.id.name);
        tv_phone_no = (EditText) findViewById(R.id.phone_no);
        tv_address = (EditText) findViewById(R.id.address);
//        tv_password = (EditText) findViewById(R.id.password);

        Bundle data = getIntent().getExtras();
        String user_id = data.getString("user_id");
        createMessage(user_id).show();

        savedetails = (Button) findViewById(R.id.savedetails);
        savedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateprofile();
            }
        });

        cancelsavedetails = (Button) findViewById(R.id.cancelsavedetails);
        cancelsavedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_UpdateProfile.this, Activity_ViewProfile.class);
                startActivity(intent);            }
        });

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);

        sessionPreferences = new SessionPreferences(getApplicationContext());
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

    public void updateprofile() {
        //initialize();
        if (!validate()) {
            Toast.makeText(this, "Update Profile failed", Toast.LENGTH_SHORT).show();
        } else {
            new doActivityUpdateProfile().execute();
        }
    }

//    public void onUpdateProfileSuccess() {
//        //after valid input has entered
//        Intent i = new Intent(Activity_UpdateProfile.this, Activity_ViewProfile.class);
//
//        name = tv_name.getText().toString();
//        i.putExtra("Name", name);
//        phone_no = tv_phone_no.getText().toString();
//        i.putExtra("Telephone number", phone_no);
//        address = tv_address.getText().toString();
//        i.putExtra("Address", address);
////        password = tv_password.getText().toString();
////        i.putExtra("Password", address);
//        startActivity(i);
//    }

    public boolean validate() {
        boolean valid = true;
        if (tv_name.getText().toString().isEmpty()) {
            tv_name.setError("Please enter your name");
            valid = false;
        }
        if (tv_phone_no.getText().toString().isEmpty()) {
            tv_phone_no.setError("Please enter correct telephone number");
            valid = false;
        }
        if (tv_address.getText().toString().isEmpty()) {
            tv_address.setError("Please enter correct address");
            valid = false;
        }
//        if (password.isEmpty()) {
//            tv_password.setError("Please enter new password");
//            valid = false;
//        }
        return valid;
    }

    public void initialize() {
        name = tv_name.getText().toString().trim();
        phone_no = tv_phone_no.getText().toString().trim();
        address = tv_address.getText().toString().trim();
//        password = tv_password.getText().toString().trim();

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

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    //execute in background
    public class doActivityUpdateProfile extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process

        @Override
        protected void onPreExecute() { //prepare initialize == constructor
            startPd();
            Bundle data = getIntent().getExtras();
            String user_id = data.getString("user_id");
            createMessage(user_id).show();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.getUpdateProfile()); //amik login api, initialize np object

            updateprofile = new JSONObject(); //construct JSON , initialize
            try {
                updateprofile.put("name", tv_name.getText().toString());
                updateprofile.put("phone_no", tv_phone_no.getText().toString());
                updateprofile.put("address", tv_address.getText().toString());
                updateprofile.put("user_id", user_id);
//                updateprofile.put("password", tv_password.getText().
// toString());
            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage(msg).show();
                Log.e("ERR_JSON_UPDATE_P_FIRST", "MSG: " + msg);
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) { //

            if (networkPreferences.postData(updateprofile)) {
                //closeDialog();
                return true;
            } else {
                // closeDialog();
                createMessage(networkPreferences.getErrorMessage()).show();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean stat) { //rcv boolean , after execution process complete
            stopPd();
            if (stat) {
                try {
                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse()); //baca
                    if (!resdata.getBoolean("error")) {
                        sessionPreferences.setSessionValue(
                                resdata.getString("email"),
                                resdata.getString("name"),
                                resdata.getString("user_id"),
                                resdata.getString("token_key"),
                                resdata.getString("address"),
                                resdata.getString("phone_no")
                        );
                        Intent intent = new Intent(Activity_UpdateProfile.this, Activity_ViewProfile.class);
                        startActivity(intent);
                    } else {
                        createMessage(resdata.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_IO_UPDATE_PROFILE: " + msg).show();
                    Log.e("ERR_IO_UPDATE_PROFILE", "MSG: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_JSON_UPDATE_PROFILE_SECOND: " + msg).show();
                    Log.e("ERR_JSON_UPDATE_SEC", "MSG: " + e.getMessage());
                }
            }
        }

    }


}