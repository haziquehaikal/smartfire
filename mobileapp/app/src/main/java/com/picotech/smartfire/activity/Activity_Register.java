package com.picotech.smartfire.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_Register extends AppCompatActivity {

    public static String TAG = "FIRE";

    private EditText et_deviceID, et_email, et_phoneNo, et_address, et_name;
    private String deviceID, email, phoneNo, address, name;
    private Button btnRegister, btnScan;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private ProgressDialog progressDialog;
    private JSONObject registerdata;
    private IntentIntegrator intentIntegrator;
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //listView = findViewById(R.id.firelog);


        et_deviceID = (EditText) findViewById(R.id.deviceID);
        et_email = (EditText) findViewById(R.id.email);
        et_phoneNo = (EditText) findViewById(R.id.phoneNo);
        et_address = (EditText) findViewById(R.id.address);
        et_name = (EditText) findViewById(R.id.name);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnScan = findViewById(R.id.btnScan);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();
            }
        });

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        sessionPreferences = new SessionPreferences(getApplicationContext());
        //backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentIntegrator = new IntentIntegrator(Activity_Register.this);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sessionPreferences.setFirebaseKey(token);
                        // Log and toast
                        String msg = sessionPreferences.getFirebaseKey();
                        Log.d(TAG, msg);
                        Toast.makeText(Activity_Register.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                et_deviceID.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void register() {
        initialize();
        if (!validate()) {
            Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show();
        } else {
            new doActivityRegister().execute();
            //onSignUpSuccess();
        }
    }

    public void onSignUpSuccess() {
        //after valid input has entered
        Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
        startActivity(intent);
    }

    //backbutton function
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public boolean validate() {
        boolean valid = true;
        if (deviceID.isEmpty() || deviceID.length() > 50) {
            et_deviceID.setError("Please enter correct device ID");
            valid = false;
        }
        if (email.isEmpty()) {
            et_email.setError("Please enter correct email");
            valid = false;
        }
        if (phoneNo.isEmpty()) {
            et_phoneNo.setError("Please enter correct phone number");
            valid = false;
        }
        if (address.isEmpty()) {
            et_address.setError("Please enter correct address");
            valid = false;
        }
        if (name.isEmpty()) {
            et_name.setError("Please enter your name");
            valid = false;
        }
        return valid;
    }

    public void initialize() {
        deviceID = et_deviceID.getText().toString().trim();
        email = et_email.getText().toString().trim();
        phoneNo = et_phoneNo.getText().toString().trim();
        address = et_address.getText().toString().trim();
        name = et_name.getText().toString().trim();

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
    public class doActivityRegister extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process
        @Override
        protected void onPreExecute() { //prepare initialize == constructor
            //showDialog();
            startPd();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.getRegisterApi()); //amik login api, initialize np object

            registerdata = new JSONObject(); //construct JSON , initialize
            try {
                registerdata.put("email", et_email.getText().toString()); //useremail= json object
                registerdata.put("device_id", et_deviceID.getText().toString());
                registerdata.put("phone_no", et_phoneNo.getText().toString());
                registerdata.put("address", et_address.getText().toString());
                registerdata.put("name", et_name.getText().toString());
                registerdata.put("mobile_token", sessionPreferences.getFirebaseKey());

            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage(msg).show();
                Log.e("ERR_JSON_LOGIN_FIRST", "MSG: " + msg);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) { //

            if (networkPreferences.postData(registerdata)) {
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
                    //Log.d("SAMPAI_SINI", networkPreferences.getHttpResponse());
                    JSONObject resdatareg = new JSONObject(networkPreferences.getHttpResponse()); //baca
                    if (!resdatareg.getBoolean("error")) {
                        Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
                        startActivity(intent);
                    } else {
                        createMessage(resdatareg.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_IO_REGISTER: " + msg).show();
                    Log.e("ERR_IO_REGISTER", "MSG: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_JSON_REGISTER_SECOND: " + msg).show();
                    Log.e("ERR_JSON_REGISTER_SEC", "MSG: " + e.getMessage());
                }
            }
        }

    }
}
