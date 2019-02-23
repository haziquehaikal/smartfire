package com.picotech.smartfire.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.picotech.smartfire.R;
import com.picotech.smartfire.adapters.Adapter_menu;
import com.picotech.smartfire.app.Endpoints;
import com.picotech.smartfire.models.FireLogModels;
import com.picotech.smartfire.utils.NetworkPreferences;
import com.picotech.smartfire.utils.SessionPreferences;

import org.json.JSONObject;

import static com.picotech.smartfire.R.*;
import static com.picotech.smartfire.app.Env.SYSTEM_ENVIRONMENT;

public class Activity_Home extends AppCompatActivity {

    private Button profilepage;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private JSONObject buzzeroff;


    public ListView listView;
    public FireLogModels fireLogModels;
    public Adapter_menu adapter;
    public ArrayList<String> item;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(layout.activity_menuhome_view);
        getSupportActionBar().setTitle("Home");
        listView = findViewById(id.homemenu);
        sessionPreferences = new SessionPreferences(getApplicationContext());
        //dummy data
        String[] state = new String[]{"Fire Log", "Live Streaming", "My Profile", "Call 999", "Turn Off Buzzer", "Logout"};


        item = new ArrayList<String>();
        adapter = new Adapter_menu(getApplicationContext(), item);
        for (int i = 0; i < state.length; i++) {
            item.add(state[i]);
        }
        adapter.notifyDataSetChanged();


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
//                    Intent intent = new Intent(Activity_Home.this, Activity_FireStatus.class);
//                    startActivity(intent);
                    Intent intent = new Intent(view.getContext(), Activity_SelectDevice.class);
                    startActivity(intent);
                } else if (i == 1) {
//                    Intent intent = new Intent(view.getContext(), Activity_SelectDevice.class);
//                    startActivity(intent);
                    Intent intent = new Intent(view.getContext(), Activity_Streaming.class);
                    startActivity(intent);
                } else if (i == 2) {
//                    Intent intent = new Intent(view.getContext(), Activity_Streaming.class);
//                    startActivity(intent);
                    Intent intent = new Intent(view.getContext(), Activity_ViewProfile.class);
                    startActivity(intent);
                } else if (i == 3) {
//                    Intent intent = new Intent(view.getContext(), Activity_ViewProfile.class);
//                    startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:999"));
                    if (ActivityCompat.checkSelfPermission(Activity_Home.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                } else if (i == 4) {
//
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:999"));
//                    if (ActivityCompat.checkSelfPermission(Activity_Home.this,
//                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    startActivity(intent);
                    new turnOffBuzzer().execute();

                } else if (i == 5) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(Activity_Home.this);
                    ab.setMessage(string.logoutyes);
                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sessionPreferences.logout();
                        };
                    });
                    ab.create().show();
                }
//                else if (i == 6) {
//
//                    AlertDialog.Builder ab = new AlertDialog.Builder(Activity_Home.this);
//                    ab.setMessage(string.logoutyes);
//                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            Intent in = new Intent(Activity_Home.this, SplashScreen.class);
//                            startActivity(in);
//                        }
//                    });
//                    ab.create().show();
//                }
            }
        });
    }

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    public class turnOffBuzzer extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute() { //prepare initialize == constructor
            //startPd();
            Endpoints endpoints = new Endpoints(SYSTEM_ENVIRONMENT, getApplicationContext()); //get application context
            networkPreferences = new NetworkPreferences(endpoints.turnOffBuzzer()); //amik login api, initialize np object
            SessionPreferences sessionPreferences = new SessionPreferences(getApplicationContext());
            //if dah login proper , uncomment this part
            String id = sessionPreferences.getUserDetails().get("user_id");
            buzzeroff = new JSONObject(); //construct JSON , initialize
            try {
                buzzeroff.put("user_id", id); //useremail= json object
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return networkPreferences.postData(buzzeroff);
        }


        @Override
        protected void onPostExecute(Boolean state) {
            //pastikan response dalam json array , ittreate the arraylengty and masukkan based on
            //cara aq masuikan dummy data

            try {
                JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse()); //baca
                if (!resdata.getBoolean("error")) {
                    createMessage("Buzzer has been turned off").show();
                } else {
                    createMessage(resdata.getString("message")).show();
                }
            } catch (IOException e) {
                String msg = e.getMessage();
                createMessage("ERR_IO_BUZZEROFF: " + msg).show();
                Log.e("ERR_IO_BUZZEROFF", "MSG: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage("ERR_JSON_BUZZER_SECOND: " + msg).show();
                Log.e("ERR_JSON_BUZZER_SECOND", "MSG: " + e.getMessage());
            }
        }


            }

}

