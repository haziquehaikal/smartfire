package com.picotech.smartfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picotech.smartfire.R;

public class test extends AppCompatActivity {
    private EditText et_email, et_password;
    private String email, password;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_login);
        et_email = (EditText) findViewById(R.id.email);
        et_password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    public void login() {
        initialize();
        if(!validate()) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
        else {
            onLoginSuccess();
        }
    }

    public void onLoginSuccess() {
        //after valid input has entered
        Intent intent = new Intent(test.this, Activity_Home.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;
        if(email.isEmpty()||email.length()>50) {
            et_email.setError("Please enter correct email address");
            valid = false;
        }
        if(password.isEmpty()) {
            et_password.setError("Please enter correct password");
            valid = false;
        }
        return valid;
    }

    public void initialize() {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

    }


}
