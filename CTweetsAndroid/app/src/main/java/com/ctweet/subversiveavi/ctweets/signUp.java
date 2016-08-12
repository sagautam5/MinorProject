//package com.ctweet.subversiveavi.ctweets;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import java.io.FileInputStream;
//
//public class signUp extends Activity {
//
//    String Filename = "loginRecords";
//    String collected = "";
//
//    TextView writingSpace;
//    FileInputStream fis;
//
//
//    @Override
//    protected void onCreate (Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.templayoutforsignup);
//        try {
//            fis = openFileInput(Filename);
//            byte[] dataArray = new byte[fis.available()];
//            while (fis.read(dataArray) != -1) {
//                collected = new String(dataArray);
//            }
//            fis.close();
//        } catch (Exception f) {
//            f.printStackTrace();
//        } finally {
//            writingSpace = (TextView) findViewById(R.id.textViewTemp);
//            writingSpace.setText(collected);
//        }
//    }
//}
