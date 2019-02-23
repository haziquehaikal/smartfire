package com.picotech.smartfire.utils;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class sshUtils {

    public SharedPref test;
    private Session _session;

    public sshUtils() {

    }


    private Session createSession(String ip) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            _session = jsch.getSession("pi", ip, 22);
            _session.setPassword("raspberry");
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            prop.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            _session.setConfig(prop);
            _session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        return _session;
    }

    public String sendCommand(String cmd, String ip) {
        Log.d("WOIWOI",ip);
        String result = null;
        try {
            createSession(ip);
            ChannelExec channelExec = (ChannelExec) _session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(cmd);
            channelExec.connect();
            result = responseReader(in);
            channelExec.disconnect();
            _session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }


    //Message handler
    private String responseReader(InputStream res) {
        StringBuilder buffer = new StringBuilder();
        byte[] tmp = new byte[1024];
        while (true) {
            try {
                while (res.available() > 0) {
                    int i = 0;
                    try {
                        i = res.read(tmp, 0, 1024);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (i < 0)
                        break;
                    Log.d("SERVER_RESPONSE", "RESPONSE:" + new String(tmp, 0, i));
                    return new String(tmp, 0, i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
