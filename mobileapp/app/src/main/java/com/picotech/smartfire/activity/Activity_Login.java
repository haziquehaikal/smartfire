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
import android.widget.TextView;
import android.widget.Toast;

import com.picotech.smartfire.R;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

//login page

public class Activity_Login extends AppCompatActivity {
    private EditText et_email, et_password;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private ProgressDialog progressDialog;
    private TextView clickToRegister;
    private String email, password;
    private JSONObject logindata;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activitymain);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        clickToRegister = (TextView) findViewById(R.id.clickToRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(intent);
            }
        });

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

        sessionPreferences = new SessionPreferences(getApplicationContext());

        if (sessionPreferences.checkLoginStatus()){
            Intent intent = new Intent(Activity_Login.this, Activity_Home.class);
            startActivity(intent);
        }

    }


    //
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

    public void login() {
        initialize();
        if (!validate()) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        } else {
            new doLogin().execute();
            //onLoginSuccess();
        }
    }

    public void onLoginSuccess() {
        //after valid input has entered
        Intent intent = new Intent(Activity_Login.this, Activity_Home.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;
        if (email.isEmpty() || email.length() > 50) {
            et_email.setError("Please enter correct email address");
            valid = false;
        }
        if (password.isEmpty()) {
            et_password.setError("Please enter correct password");
            valid = false;
        }
        return valid;
    }

    public void initialize() {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

    }

    public void clickToRegister() {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

    }

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    //execute in background
    public class doLogin extends AsyncTask<Void, Void, Boolean> { //void=input, void=, boolean=process

        @Override
        protected void onPreExecute() { //prepare initialize == constructor
            //showDialog();
            startPd();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.getLoginApi()); //amik login api, initialize np object

            logindata = new JSONObject(); //construct JSON , initialize
            try {
                logindata.put("email", et_email.getText().toString()); //useremail= json object
                logindata.put("password", et_password.getText().toString());
            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage(msg).show();
                Log.e("ERR_JSON_LOGIN_FIRST", "MSG: " + msg);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) { //

            if (networkPreferences.postData(logindata)) {
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
                    JSONObject resdatalogin = new JSONObject(networkPreferences.getHttpResponse()); //baca
                    if (!resdatalogin.getBoolean("error")) { //
                        if (resdatalogin.getInt("temp_pw") == 1) {
                            Intent intent = new Intent(Activity_Login.this, Activity_ChangePassword.class);
                            intent.putExtra("user_id", resdatalogin.getString("user_id"));
                            startActivity(intent);
                        } else {
                            sessionPreferences.setSessionValue(
                                    resdatalogin.getString("email"),
                                    resdatalogin.getString("name"),
                                    resdatalogin.getString("user_id"),
                                    resdatalogin.getString("token_key"),
                                    resdatalogin.getString("address"),
                                    resdatalogin.getString("phone_no")
                            );
                            sessionPreferences.setLoginStatus(true);
                            Intent intent = new Intent(Activity_Login.this, Activity_Home.class);
                            startActivity(intent);
                        }
                       } else {
                        createMessage(resdatalogin.getString("message")).show();
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



