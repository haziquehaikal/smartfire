package com.picotech.smartfire.app;

import android.content.Context;

import com.picotech.smartfire.utils.SessionPreferences;

//specify API

public class Endpoints {

    private String url, protocol;
    private SessionPreferences sessionPreferences;

    public Endpoints(String env, Context _context) { //get current activity in mobile app. if login, login

        switch (env) {
            case "development":
                this.protocol = "http://";
//                this.url = "192.168.43.33/smartfire/api/public";
//                this.url = "172.20.10.6/smarthome/public";
//                this.url = "172.20.82.223/smarthome/public";
                this.url = "192.168.43.216/smarthome/public";
                break;

            case "staging":
                this.protocol = "https://"; 
                this.url = "staging.uni10smarthome.com";
                break;

            case "production":
                this.protocol = "https://";
                this.url = "main.uni10smarthome.com";
                break;
        }

        sessionPreferences = new SessionPreferences(_context);


    }

    private String getUserData(String key){
        return sessionPreferences.getUserDetails().get(key);
    }
    private String getProtocol() {
        return protocol;
    }

    private String getUrl() {
        return url;
    }

    public String getTempPassApi(){
        return getProtocol() + getUrl() + "/api/smartfire/mobile/changepass";
    }

    public String getLoginApi() {
        return getProtocol() + getUrl() + "/api/smartfire/mobile/login";
    }

//    public String doLogin() {
//        return getProtocol() + getUrl() + "/api/mobile/login";
//    }/
    public String getLogApi() {
        return getProtocol() + getUrl() + "/api/smartfire/mobile/log";
    }

    public String getRegisterApi() { return getProtocol() + getUrl() + "/api/smartfire/mobile/register"; }

    public String getProfileApi() { return getProtocol() + getUrl() + "/api/smartfire/mobile/profile/"; }

    public String getDeviceProfile() {
        return getProtocol() + getUrl() + "mobile/deviceprofile";
    }

    public String getUpdateProfile() { return getProtocol() + getUrl() + "/api/smartfire/mobile/updateProfile"; }

    public String getDeviceListApi(){
        return getProtocol() + getUrl() + "/api/smartfire/mobile/firedevice";
    }

    public String tep() { return getProtocol() + getUrl() + "/api/smartfire/mobile/updTempPW";
    }

    public String getreadings() { return getProtocol() + getUrl() + "/api/smartfire/mobile/getreadings";
    }
    public String turnOffBuzzer(){
        return getProtocol() + getUrl() + "/api/smartfire/mobile/turnOffBuzzer";
    }
}