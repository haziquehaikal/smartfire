package com.picotech.smartfire.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.picotech.smartfire.activity.Activity_Login;
import com.picotech.smartfire.activity.Activity_FireStatus;
import com.picotech.smartfire.activity.Activity_Home;

import java.util.HashMap;

public class SessionPreferences {

    private final static String PREF_NAME = "DBBOXLogin";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_NAME = "ayam";
    private final static String KEY_API_TOKEN = "token";
    private final static String KEY_USERID = "user_id";
    private final static String KEY_ADDR = "address";
    private final static String KEY_PHONENO = "phone_no";
    private final static String KEY_PASSWORD = "password";
    private final static String KEY_EMAIL = "email";
    // private final static String KEY_API_TOKEN = "api_token";
    private final static String KEY_ISLOGGEDIN = "false";

   private final static String KEY_READING_HEAT = "reading_heat";
   private final static String KEY_READING_SMOKE = "reading_smoke";
   private final static String KEY_READING_GAS = "reading_gas";
//    private final static String KEY_STATUS = "status";
    //private final static String KEY_FIREDEVICE_ID = "firedevice_id";

    private int PRIVATE_MODE = 0;

    private Context _context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionPreferences(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirebaseKey(String key){
        editor.putString("FIREKEY",key);
        editor.commit();
    }

    public String getFirebaseKey(){
        return pref.getString("FIREKEY",null);
    }

    public void setSessionValue(String email, String name, String userid, String token, String addr, String no){
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_API_TOKEN, token);
        editor.putString(KEY_ADDR, addr);
        editor.putString(KEY_PHONENO, no);

        editor.commit();
    }

//    public void setDisplayReading(String reading_gas, String reading_smoke, String reading_heat) {
//        editor.putString(KEY_READING_GAS, reading_gas);
//        editor.putString(KEY_READING_SMOKE, reading_smoke);
//        editor.putString(KEY_READING_HEAT, reading_heat);
//        editor.putString(KEY_STATUS, status);
//        editor.putString(KEY_FIREDEVICE_ID, firedevice_id);

//        editor.commit();
//    }

        public void setLoginStatus(Boolean status) {
        editor.putBoolean(KEY_ISLOGGEDIN, status);
        editor.commit();
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> userdata = new HashMap<>();
        userdata.put("name", pref.getString(KEY_NAME, "Awesome Customer"));
        userdata.put("email", pref.getString(KEY_EMAIL, null));
        userdata.put("address", pref.getString(KEY_ADDR, null));
        userdata.put("phone_no", pref.getString(KEY_PHONENO, null));
        userdata.put("user_id", pref.getString(KEY_USERID, null));
        userdata.put("api_token", pref.getString(KEY_API_TOKEN, null));
        return userdata;
    }


    public HashMap<String, String> displayreading() {
        HashMap<String, String> displayreading = new HashMap<>();
        //displayreading.put("reading_heat", "9");
        displayreading.put("reading_heat", pref.getString(KEY_READING_HEAT, null));
        displayreading.put("reading_smoke", pref.getString(KEY_READING_SMOKE, null));
        displayreading.put("reading_gas", pref.getString(KEY_READING_GAS, null));
//        userdata.put("status", pref.getString(KEY_STATUS, null));
//        userdata.put("firedevice_id", pref.getString(KEY_FIREDEVICE_ID, null));

        displayreading.put("name", pref.getString(KEY_NAME, "Awesome Customer"));
        displayreading.put("email", pref.getString(KEY_EMAIL, null));
        displayreading.put("address", pref.getString(KEY_ADDR, null));
        displayreading.put("phone_no", pref.getString(KEY_PHONENO, null));
        displayreading.put("user_id", pref.getString(KEY_USERID, null));
        displayreading.put("api_token", pref.getString(KEY_API_TOKEN, null));

        return displayreading;
    }


    public boolean checkLoginStatus() {

        return pref.getBoolean(KEY_ISLOGGEDIN, false);
    }


    public void logout() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }

//    public static class SaveSharedPreference
//    {
//        static final String PREF_USER_NAME= "username";
//
//        static SharedPreferences getSharedPreferences(Context ctx) {
//            return PreferenceManager.getDefaultSharedPreferences(ctx);
//        }
//
//        public static void setUserName(Context ctx, String userName)
//        {
//            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
//            editor.putString(PREF_USER_NAME, userName);
//            editor.commit();
//        }
//
//        public static String getUserName(Context ctx)
//        {
//            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
//        }
//    }

}
