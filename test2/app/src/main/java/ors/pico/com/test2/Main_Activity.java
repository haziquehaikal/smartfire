package ors.pico.com.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Activity extends AppCompatActivity {
    private EditText et_email, et_password;
    private TextView clickToRegister;
    private String email, password;
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
                Intent intent = new Intent(Main_Activity.this,
                        Activity_Register.class);
                startActivity(intent);
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
        Intent intent = new Intent(Main_Activity.this, Home.class);
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

    public void clickToRegister() {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

    }


}
