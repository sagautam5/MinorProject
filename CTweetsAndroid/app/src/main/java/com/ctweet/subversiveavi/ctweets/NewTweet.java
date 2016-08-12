package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONObject;

//import java.io.BufferedReader;
//import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.io.InputStreamReader;
//import java.net.URI;


public class NewTweet {

    final static String serverUrl = "http://www.onlineparikshya.com/tweets/phps/";
    //final static String serverUrl = "http://10.0.0.2/minor/";
    HttpClient client;

    private UserDetails ud;
    JSONArray json2;
    String stemp_tweetID = "";

    Activity parent = null;

    public void create(UserDetails ud) {

        this.ud = ud;
        client = new DefaultHttpClient();
        new Read().execute();

    }

    public boolean check(Context context) {
        DisplayInfo d;
        ArrayList<DisplayInfo> dI = new ArrayList<DisplayInfo>();
        SQLHandler _sql= new SQLHandler(context);
        String PreviousDataTop = null;
        try {
            _sql.open();
            dI = _sql.getData(2);
            d = dI.get(1);
            PreviousDataTop= d.text1;
            _sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String NewDataTop="";
        NewDataTop += stemp_tweetID;
        if(NewDataTop.equalsIgnoreCase(PreviousDataTop)) {
            return true;
        }
        return false;

    }

    public JSONArray gotData (UserDetails ud) throws IOException, JSONException{
        String url = serverUrl + "retrieveTweets.php?token=" + ud.token + "&type=" + ud.type;
        HttpGet get = new HttpGet(url);
        HttpResponse r = client.execute(get);
        int status = r.getStatusLine().getStatusCode();
        if (status == 200){
            HttpEntity e = r.getEntity();
            String data = EntityUtils.toString(e);
            JSONArray returnedData = new JSONArray(data);
            return returnedData;
        }else{
            return null;
        }
    }

    public class Read extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                json2 = gotData(ud);
                return json2.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String st) {

            try{
                JSONArray array = new JSONArray(st);

                JSONObject obj = array.getJSONObject(0);
                stemp_tweetID = obj.getString("tweetID");

                } catch(Exception e){
                e.printStackTrace();
                }
            }

        }

    }

