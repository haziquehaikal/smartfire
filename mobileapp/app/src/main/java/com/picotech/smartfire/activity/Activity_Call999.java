package com.picotech.smartfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.picotech.smartfire.R;

public class Activity_Call999 extends AppCompatActivity {

    private Button call999;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_call999);
        getSupportActionBar().setTitle("Call 999");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        call999 = (Button) findViewById(R.id.call999);
        call999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Call999.this, Activity_Home.class);
                startActivity(intent);
            }
        });
    }
}