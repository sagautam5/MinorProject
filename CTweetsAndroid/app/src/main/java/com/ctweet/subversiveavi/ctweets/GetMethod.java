package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class GetMethod {

    final static String serverUrl = "http://www.onlineparikshya.com/tweets/phps/";

    HttpClient client;
    String [] loginDetails;
    JSONArray json;

    private ProgressDialog progressBar;
    private AlertDialog.Builder loginFailedAlert;
    Activity parent;

    public void onCreate(Activity parent, String[] loginDetails) {
        this.parent = parent;
        loginFailedAlert = new AlertDialog.Builder(parent);
        progressBar = new ProgressDialog(parent);
        progressBar.setCancelable(false);
        progressBar.setMessage("Logging In ...");
        progressBar.show();

        this.loginDetails = loginDetails;
        client = new DefaultHttpClient();
        new Read().execute(); /*"something" as parameter?*/

    }

    public JSONArray gotData (String UserName, String Password) throws /*ClientProtocolException,*/ IOException, JSONException{
        String url = serverUrl + "verify.php?username=" + UserName + "&password=" + Password;
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
                json = gotData(loginDetails[0], loginDetails[1]);
                Log.d("json-return", json.toString());
                return json.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        String st_token;
        @Override
        protected void onPostExecute(String st) {
            //write json to file here
//            String Filename = "loginRecords";
            loginFailedAlert.setMessage("Login Failed!.");
            loginFailedAlert.setCancelable(true);
            JSONObject obj=null;

            try {

                JSONArray array = new JSONArray(st);
                obj = array.getJSONObject(0);

                st_token = obj.getString("token");
                SharedPreferences preferences = parent.getSharedPreferences("login_records", Context.MODE_PRIVATE);
                preferences.edit()
                        .putString("name", obj.getString("name"))
                        .putString("type", obj.getString("type"))
                        .putString("token", obj.getString("token"))
                        .apply();

            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressBar.dismiss();
                if (st_token !=null){
                    StoreDB db = new StoreDB();
                    db.create(GetMethod.this.parent);
                }else{
                    AlertDialog alert11 = loginFailedAlert.create();
                    alert11.show();
                }
            }

        }


    }

}
