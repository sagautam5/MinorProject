package com.ctweet.subversiveavi.ctweets;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;



import java.sql.SQLException;
import java.util.ArrayList;


public class MainDisplay extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<DisplayInfo> dIf = new ArrayList<DisplayInfo>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView myListView;
    ArrayAdapter<DisplayInfo> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("activity", "reached display");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_main);

        fetchTweets();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


        myListView = (ListView)findViewById(R.id.listViewDisplay);
        adapter = new ArrayAdapter<DisplayInfo>(MainDisplay.this, android.R.layout.simple_list_item_2, android.R.id.text1, dIf) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(dIf.get(position).text1);
                text2.setText(dIf.get(position).text2);

                return view;
            }
        };
        myListView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final DisplayInfo di = adapter.getItem(position);
               // di.
                PopupMenu popup = new PopupMenu(MainDisplay.this, view);
                popup.getMenuInflater().inflate(R.menu.tweet_menu, popup.getMenu());

                SharedPreferences preferences = getSharedPreferences("login_records", Context.MODE_PRIVATE);
                String type = preferences.getString("type", "");
                if (type.equalsIgnoreCase("manager"))
                    type="Cook";
                else
                    type = "Manager";
                popup.getMenu().getItem(2).setTitle(type);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId())
                        {
                            case 0:
                                Acknowledge tmp = new Acknowledge();
                                tmp.create(MainDisplay.this, di.tweetId);
                                break;
                            case 2:
                                ChangeType tmp2 = new ChangeType();
                                tmp2.create(MainDisplay.this, di.tweetId);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
    @Override
    public void onRefresh() {
        fetchTweets();
        myListView.setAdapter(adapter);
    }


    private void fetchTweets() {

        // showing refresh animation before making http call
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);

        SQLHandler _sql = new SQLHandler(MainDisplay.this);

        try {
            _sql.open();
            dIf = _sql.getData(1);
           // for (int ii=0; ii<=10; ii++){
                Log.d("dIf.array.list",dIf.get(3).toString());
            //}
            _sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);
    }
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
