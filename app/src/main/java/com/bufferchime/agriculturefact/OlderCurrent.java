package com.bufferchime.agriculturefact;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bufferchime.agriculturefact.materialdesgin.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OlderCurrent extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    protected BottomNavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    TextView heading;
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<NewsClass> ingots = new ArrayList<NewsClass>();
    ListView listview;
    String key=OlderPosts.getSelection();
    public static int selection;
    public static String categorylink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_current);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        View v = findViewById(R.id.top);
        TextView tv = (TextView) v.findViewById(R.id.textView4);
        tv.setText("Current Affairs");








        listview = (ListView) findViewById(R.id.listview);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processJson(object);
            }
        }).execute("https://spreadsheets.google.com/tq?key="+key);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                //    if(position==0){
                selection=position;
                categorylink=("https://spreadsheets.google.com/tq?key="+key);
                Intent appInfo = new Intent(OlderCurrent.this, Detailnews2.class);

                startActivity(appInfo);
            }

        });

    }

    //Navigationviewstart
    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }
    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        navigationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, Home.class));
                } else if (itemId == R.id.navigation_affairs) {
                    OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, OlderCurrent.class));
                } else if (itemId == R.id.navigation_notes) {
                    OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, Notes.class));
                } else if (itemId == R.id.navigation_tests) {
                    OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, MainActivity.class));
                } else if (itemId == R.id.navigation_contact) {
                    OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, Contact.class));
                }
                OlderCurrent.this.finish();
            }
        }, 300);
        return true;
    }
    private void updateNavigationBarState(){
        int actionId = R.id.navigation_affairs;
        selectBottomNavigationBarItem(actionId);
    }
    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {

        OlderCurrent.this.startActivity(new Intent(OlderCurrent.this, OlderPosts.class));
    }
//NavigationViewEnd


    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");


            for (int r = 0; r < rows.length(); r++) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int position = columns.getJSONObject(0).getInt("v");
                String name = columns.getJSONObject(1).getString("v");
                String today = columns.getJSONObject(3).getString("v");
                String difference = columns.getJSONObject(4).getString("v");
                String date = columns.getJSONObject(2).getString("v");

                NewsClass ingot1 = new NewsClass(position, name, today,difference,date);
                ingots.add(ingot1);

            }

            final NewsAdaptor adapter = new NewsAdaptor(OlderCurrent.this, R.layout.list_news_row, ingots);
            listview.setAdapter(adapter);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int getSelection(){
        return selection;
    }

    public static String getlink(){
        return categorylink;
    }


}
