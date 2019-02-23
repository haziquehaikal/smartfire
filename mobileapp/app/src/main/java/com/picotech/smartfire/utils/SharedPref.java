package com.picotech.smartfire.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public String ipaddr;
    private static final String PREF_NAME = "OXCartLogin";
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context _context;

    public SharedPref(Context context) {
        this._context = context;
        this.pref = _context.getSharedPreferences(PREF_NAME, 0);
        this.editor = pref.edit();

    }

    public void setIp(String ip){
        editor.putString("IPADDR",ip);
        editor.commit();
    }


    public String getIp(){
        return pref.getString("IPADDR",null);
    }
}
