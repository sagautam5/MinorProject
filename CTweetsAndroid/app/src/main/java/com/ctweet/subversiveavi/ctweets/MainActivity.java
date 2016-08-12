package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
//import android.content.DialogInterface;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    Button logIn, signUp;
    EditText UN, PW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UN = (EditText) findViewById(R.id.main_username_editText);
        PW  = (EditText) findViewById(R.id.main_password_editText);
        logIn= (Button) findViewById(R.id.main_sendGetReq_button);
        //signUp= (Button) findViewById(R.id.main_sendMakeReq_button);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] loginDetails = {UN.getText().toString(), PW.getText().toString()};
                GetMethod getMethod = new GetMethod();
                getMethod.onCreate(MainActivity.this, loginDetails);
            }
        });


        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(MainActivity.ALARM_SERVICE);
        int interval = 300000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
