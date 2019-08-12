package com.bufferchime.agriculturefact;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Detailnews2 extends AppCompatActivity {


    //  protected BottomNavigationView navigationView;
    TextView tv;
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<NewsClass> ingots = new ArrayList<NewsClass>();
    ListView listview;
    int key= OlderCurrent.getSelection();
    String link=OlderCurrent.getlink();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailnews);
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
        }).execute(link);
    }


    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }








    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");



            int r=key;
            JSONObject row = rows.getJSONObject(r);
            JSONArray columns = row.getJSONArray("c");

            int position = columns.getJSONObject(0).getInt("v");
            String name = columns.getJSONObject(1).getString("v");
            String today = columns.getJSONObject(3).getString("v");
            String difference = columns.getJSONObject(4).getString("v");
            String date = columns.getJSONObject(2).getString("v");
            NewsClass ingot1 = new NewsClass(position, name, today,difference,date);
            ingots.add(ingot1);



            final NewsAdaptor2 adapter = new NewsAdaptor2(Detailnews2.this, R.layout.list_news_row, ingots);
            listview.setAdapter(adapter);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}


