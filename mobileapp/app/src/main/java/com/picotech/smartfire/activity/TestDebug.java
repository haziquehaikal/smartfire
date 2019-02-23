package com.picotech.smartfire.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picotech.smartfire.R;
import com.picotech.smartfire.utils.SharedPref;

public class TestDebug extends AppCompatActivity implements View.OnClickListener {
    private EditText username, hostname, port, password;
    public SharedPref haha;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.troubleshooting_page);

        Button setip = findViewById(R.id.setip);
        setip.setOnClickListener(this);
        hostname = (EditText) findViewById(R.id.hostname);
        haha = new SharedPref(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setip:
                if(hostname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter IP address",Toast.LENGTH_LONG).show();
                }else{
                    haha.setIp(hostname.getText().toString());
                    Toast.makeText(getApplicationContext(),"IP: " + haha.getIp(),Toast.LENGTH_LONG).show();

                }
        }

    }


}
