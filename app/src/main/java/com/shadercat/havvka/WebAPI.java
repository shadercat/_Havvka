package com.shadercat.havvka;

import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebAPI {
    public static final String API = "https://havvka-server-vlad-f96.c9users.io";
    public static boolean CheckUserInfo(String email, String password) {
        if (email != null && password != null) {
            HashMap<String,String> values = new HashMap<>();
            values.put("user_email", email.trim());
            values.put("user_password",password);
            String response = performPostCall(API + "/users/" + email.trim() + "&" + password,values);
            boolean flag = false;
            try {
                flag = Converter.parseResponse(response);
            } catch (JSONException e){
                Log.e("JSONException", e.getMessage());
            }
            return flag;
        }
        return false;
    }

    public static boolean CreateAccount(String email, String password) {
        String call = performPostCall(API + "/users/register/" + email + "&" + password,null);
        boolean isSend = false;
        try {
            isSend = Converter.parseResponse(call);
        } catch (JSONException e){
            Log.e("JSONException",e.getMessage());
        }
        return isSend;
    }

    public static String getJson(String adress){
        HttpURLConnection connection = null;
        String responseFromServer = "";
        BufferedReader reader = null;
        try{
            URL url = new URL(adress);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            responseFromServer = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseFromServer;
    }

    public static String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    private static String post(String url){
        String response = null;
        try{
            URL url1 = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.close();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null){
                builder.append(inputLine);
            }
            reader.close();
            response = builder.toString();

        } catch (Exception e){
            Log.e("conn",e.getMessage());
        }
        return response;
    }
    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        if(params == null){
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
