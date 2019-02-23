package com.picotech.smartfire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.picotech.smartfire.R;
import com.picotech.smartfire.utils.SessionPreferences;

public class Activity_ViewProfile extends AppCompatActivity {

    private TextView tv_username, tv_phoneNo, tv_address;
    private String username, phoneNo, address;
    private Button updateprofile;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.viewprofile);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_username = (TextView) findViewById(R.id.username);
        tv_address = (TextView) findViewById(R.id.address);
        tv_phoneNo = (TextView) findViewById(R.id.phoneNo);

        //get data from the android session


        //createMessage(user_id).show();

        updateprofile = (Button) findViewById(R.id.updateprofile);
        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = sessionPreferences.getUserDetails().get("user_id");
                Intent intent = new Intent(Activity_ViewProfile.this, Activity_UpdateProfile.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });
//        private Snackbar createMessage(String msg) {
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
//            return snackbar;
//        }
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
    @Override
    public void onResume() {
        super.onResume();
        sessionPreferences = new SessionPreferences(getApplicationContext());

        username = sessionPreferences.getUserDetails().get("name");
        tv_username.setText(username);

        address = sessionPreferences.getUserDetails().get("address");
        tv_address.setText(address);

        phoneNo = sessionPreferences.getUserDetails().get("phone_no"); //database column
        tv_phoneNo.setText(phoneNo); //variable @ viewprofile.xml
    }
}