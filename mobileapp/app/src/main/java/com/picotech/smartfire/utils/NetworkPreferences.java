package com.picotech.smartfire.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//exec networking process

public class NetworkPreferences {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private Response response;
    private String api_url, api_data, api_token;
    private String err_msg;

    public NetworkPreferences(String url) { //constructor: initialize initial value

        this.api_url = url; //pass
        // this.api_data = data;
        //this.api_token = token;
        client = new OkHttpClient();
    }

    //send data to server via POST - postData
    public boolean postData(JSONObject data) { //post using JSON data, create JSON object
        try {
            RequestBody body = RequestBody.create(JSON, data.toString()); //construct body convert data to json
            Request request = new Request.Builder() //create http process
                    .url(api_url)
                    .post(body)
                    .build();
            response = client.newCall(request).execute(); //send data
            return true;

        } catch (IOException e) {
            String msg = e.getMessage();
            setErrorMessage(msg);
            Log.e("ERR_NETPREF_POSTDATA", "ERR: " + msg);
            return false;
        }
    }


    //get data from user via GET
    public boolean getData() { //get URL

        try {
            Request request = new Request.Builder()
                    .url(api_url)
                    .build(); //build process
            response = client.newCall(request).execute();
            return true;
        } catch (IOException e) {
            String msg = e.getMessage();
            setErrorMessage(msg);
            Log.e("ERR_NETKPREF_GETDATA", "ERR: " + msg);
            return false;
        }
    }


    public String getHttpResponse() throws IOException { //read all HTTP response

        if (response.code() != 500) {
            String msgdata = response.body().string();

            if (msgdata != null) {
                return msgdata;
            } else {
                return "No response body available";
            }

        } else {
            Log.e("ERR_NETPREF_RESPONSE", "NOT SUCCESS");
            return "{\"error\":true,\"message\":\"Request Not success\"}";
        }
    }

    public int getHttpCode() {
        return response.code();
    }

    public String getErrorMessage() {
        return err_msg;
    }

    private void setErrorMessage(String msg) {
        this.err_msg = msg;
    }
}
