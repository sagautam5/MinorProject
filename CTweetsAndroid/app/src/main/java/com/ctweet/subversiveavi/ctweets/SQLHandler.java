package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Abinash on 24-Aug-15.
 */
public class SQLHandler {

    public static final String key_rowId = "_id";
    public static final String key_tweet = "tweets_posted";
    public static final String key_postedBy = "posted_by";
    public static final String key_date = "date_posted_on";
    public static final String key_time = "time_posted_on";
    public static final String key_tweetID = "tweet_id";


    public static final String database_name = "TweetsDB";
    public static final String database_table = "tweetsTable";
    public static final int database_version = 3;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;



    private static class DBHelper extends SQLiteOpenHelper{

        public  DBHelper (Context context){
            super(context, database_name, null, database_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("activity", "storing");

            db.execSQL("CREATE TABLE " + database_table + " (" +
                            key_rowId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            key_tweet + " TEXT NOT NULL, " +
                            key_postedBy + " TEXT NOT NULL, " +
                            key_tweetID + " TEXT NOT NULL, " +
                            key_time + " TEXT NOT NULL, " +
                            key_date + " TEXT NOT NULL);"

            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("DROP TABLE IF EXISTS "+ database_table);
           onCreate(db);

        }
    }

    public SQLHandler(Context c){
        ourContext = c;
    }

    public SQLHandler open() throws SQLException{
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void clear() {

        ourHelper = new DBHelper(ourContext);
        ourHelper.onUpgrade(ourHelper.getWritableDatabase(), database_version, database_version);
    }

    public void close(){
        ourHelper.close();
    }

    public void createEntry(String st_tweet, String st_postedBy, String st_date, String st_time, String st_tweetID) {
        ContentValues cv = new ContentValues();
        cv.put(key_tweet, st_tweet);
        cv.put(key_postedBy, st_postedBy);
        cv.put(key_date, st_date);
        cv.put(key_time, st_time);
        cv.put(key_tweetID, st_tweetID);
        Log.d("writingDB", st_tweet + st_date + st_time + st_postedBy + st_tweetID);
        ourDatabase.insert(database_table, null, cv);



    }

    public ArrayList<DisplayInfo> getData(int parentNumber){
        String[] columns = new String[]{key_rowId, key_tweet, key_postedBy, key_date, key_time, key_tweetID};
        Cursor c = ourDatabase.query(database_table, columns, null, null, null, null, null);
        ArrayList<DisplayInfo> dInfos = new ArrayList<DisplayInfo>();
        //DisplayInfo dInfo = new DisplayInfo();

        //int iRow= c.getColumnIndex(key_rowId);
        int iTweets= c.getColumnIndex(key_tweet);
        int iPostedBy= c.getColumnIndex(key_postedBy);
        int iDate= c.getColumnIndex(key_date);
        int iTime= c.getColumnIndex(key_time);
        int iTweetId = c.getColumnIndex(key_tweetID);
        int i=0;
        if(parentNumber == 1) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                DisplayInfo dInfo = new DisplayInfo();
                dInfo.text1 = c.getString(iTweets) ;
                dInfo.text2 = c.getString(iPostedBy) +" "+ c.getString(iDate)+ " "+ c.getString(iTime);
                dInfo.tweetId = c.getString(iTweetId);
                dInfos.add(dInfo);
            }

        }else if(parentNumber == 2){
            DisplayInfo dInfo = new DisplayInfo();
            dInfo.text1 =  c.getString(iTweetId);
            dInfos.add(dInfo);
        }
        return dInfos;
    }
}
