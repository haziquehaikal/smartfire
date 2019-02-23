package com.picotech.smartfire.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_ChangePassword extends AppCompatActivity {

    public EditText et_temp_pw, et_password;
    public String temp_pw, password;
    private Button btnUpdateProfile;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private ProgressDialog progressDialog;
    private JSONObject chgpwddata;


    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_changepassword);
        getSupportActionBar().setTitle("Update Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_temp_pw = (EditText) findViewById(R.id.temp_pw);
        et_password = (EditText) findViewById(R.id.newpassword);
        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);

        Bundle data = getIntent().getExtras();
        String user_id = data.getString("user_id");
        createMessage(user_id).show();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTempPw();
            }
        });

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

        sessionPreferences = new SessionPreferences(getApplicationContext());
    }

    public void updateTempPw() {

       // initialize();
        if (!validate()) {
            Toast.makeText(this, "Change password failed", Toast.LENGTH_SHORT).show();
        } else {
            new doActivityChangePassword().execute();
        }
    }

    public void initialize() {
        temp_pw = et_temp_pw.getText().toString().trim();
        password = et_password.getText().toString().trim();
    }

    public void onUpdateProfileSuccess() {
        //after valid input has entered
        Intent i = new Intent(Activity_ChangePassword.this, Activity_ViewProfile.class);

        temp_pw = et_temp_pw.getText().toString();
        i.putExtra("Temporary password", temp_pw);
        password = et_password.getText().toString();
        i.putExtra("Password", password);
        startActivity(i);
    }

    public boolean validate() {
        boolean valid = true;
        if (et_temp_pw.getText().toString().isEmpty()) {
            et_temp_pw.setError("Please enter temporary password");
            valid = false;
        }
        if (et_password.getText().toString().isEmpty()) {
            et_password.setError("Please enter new password");
            valid = false;
        }
        return valid;
    }

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
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

    //execute in background
    public class doActivityChangePassword extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process

        @Override
        protected void onPreExecute() { //prepare initialize == constructor
            startPd();
            Bundle data = getIntent().getExtras();
            String user_id = data.getString("user_id");
            createMessage(user_id).show();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.tep()); //amik login api, initialize np object

            chgpwddata = new JSONObject(); //construct JSON , initialize
            try {
                chgpwddata.put("temp_pw", et_temp_pw.getText().toString());
                chgpwddata.put("password", et_password.getText().toString());
                chgpwddata.put("user_id", user_id);
            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage(msg).show();
                Log.e("ERR_JSON_CHGPW_FIRST", "MSG: " + msg);
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) { //

            if (networkPreferences.postData(chgpwddata)) {
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
                        sessionPreferences.setSessionValue(
                                resdata.getString("email"),
                                resdata.getString("name"),
                                resdata.getString("user_id"),
                                resdata.getString("token_key"),
                                resdata.getString("address"),
                                resdata.getString("phone_no")
                        );
                        Intent intent = new Intent(Activity_ChangePassword.this, Activity_Home.class);
                        startActivity(intent);
                    } else {
                        createMessage(resdata.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_IO_CHGPW: " + msg).show();
                    Log.e("ERR_IO_CHGPW", "MSG: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_JSON_CHGPW_SECOND: " + msg).show();
                    Log.e("ERR_JSON_CHGPW_SECOND", "MSG: " + e.getMessage());
                }
            }
        }

    }
}
