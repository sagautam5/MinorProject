package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class StoreDB {

    final static String serverUrl = "http://www.onlineparikshya.com/tweets/phps/";
    HttpClient client;
    JSONArray json2;

    private ProgressDialog progressBar;
    Context parent = null;

    public void create(Context parent) {

        this.parent = parent;
        if (parent instanceof Activity) {
            progressBar = new ProgressDialog(parent);
            progressBar.setCancelable(true);
            progressBar.setMessage("Loading Tweets ...");
            progressBar.show();
        }
        client = new DefaultHttpClient();
        new Read().execute();

    }

    public JSONArray gotData () throws IOException, JSONException{
        UserDetails ud= getUserDetails();
        Log.d("typereturned", ud.type);
        String url = serverUrl + "retrieveTweets.php?token=" + ud.token + "&type="+ ud.type;
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
                json2 = gotData();
                return json2.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String st) {
            boolean writingSuccess = false;
            try {
                Log.d("storedb-onpostexecute", "post success");
                SQLHandler entry = new SQLHandler(parent);
                entry.open();
                entry.clear();
                JSONArray array = new JSONArray(st);
                for (int i=0; i<array.length(); ++i)
                {
                    JSONObject obj = array.getJSONObject(i);
                    String st_tweet = obj.getString("tweet");
                    String st_postedBy = obj.getString("username");
                    String st_date = obj.getString("date");
                    String st_time = obj.getString("time");
                    String st_tweetID = obj.getString("tweetID");
                    Log.d("messageJsonTweets", st_tweet+ st_postedBy + st_date + st_time + st_tweetID);
                    entry.createEntry(st_tweet, st_postedBy, st_date, st_time, st_tweetID);
                }
                entry.close();
                writingSuccess = true;
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                if (parent instanceof Activity) {
                    progressBar.dismiss();
                    if (writingSuccess) {
                        Intent i = new Intent(parent, MainDisplay.class);
                        parent.startActivity(i);
                        ((Activity) parent).finish();
                    }
                }
            }

        }
    }

    public UserDetails getUserDetails(){
        UserDetails ud = new UserDetails();


        try {

            SharedPreferences preferences = parent.getSharedPreferences("login_records", Context.MODE_PRIVATE);
            ud.token = preferences.getString("token", "");
            ud.name = preferences.getString("name", "");
            ud.type = preferences.getString("type", "");



        } catch (Exception f) {
            f.printStackTrace();
        }
        return ud;
    }

}
