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
import android.widget.Toast;

import com.bufferchime.agriculturefact.Notespack.CategoryNotes;
import com.bufferchime.agriculturefact.Notespack.note;
import com.bufferchime.agriculturefact.Notespack.noteadaptor;
import com.bufferchime.agriculturefact.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notes extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    protected BottomNavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<note> notes = new ArrayList<note>();
    ListView listview;
    public static String selection;
    public static String categorylink[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);


        //sheetparts
        listview = (ListView) findViewById(R.id.listview);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processJson(object);
            }
        }).execute("https://spreadsheets.google.com/tq?key=1BKa7C6kDs9I9x8gAwjt5u2KJf0p84dwCgx4_WG70924");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                        //    if(position==0){
                        selection=categorylink[position];
                        Intent appInfo = new Intent(Notes.this, CategoryNotes.class);

                        startActivity(appInfo);
                    }

                });

            }});




        Toast.makeText(this, "Double Click to Select Category", Toast.LENGTH_SHORT).show();




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
                    Notes.this.startActivity(new Intent(Notes.this, Home.class));
                } else if (itemId == R.id.navigation_affairs) {
                    Notes.this.startActivity(new Intent(Notes.this, OlderPosts.class));
                } else if (itemId == R.id.navigation_notes) {
                    Notes.this.startActivity(new Intent(Notes.this, Notes.class));
                } else if (itemId == R.id.navigation_tests) {
                    Notes.this.startActivity(new Intent(Notes.this, MainActivity.class));
                } else if (itemId == R.id.navigation_contact) {
                    Notes.this.startActivity(new Intent(Notes.this, Contact.class));
                }
                Notes.this.finish();
            }
        }, 300);
        return true;
    }
    private void updateNavigationBarState(){
        int actionId = R.id.navigation_notes;
        selectBottomNavigationBarItem(actionId);
    }
    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            super.onBackPressed();

            System.exit(0);

            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();

    }
//NavigationViewEnd


    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            categorylink = new String[25];
            int arrayval=0;
            for (int r = 0; r < rows.length(); r++) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int position = columns.getJSONObject(0).getInt("v");
                String name = columns.getJSONObject(1).getString("v");
                String link = columns.getJSONObject(2).getString("v");


                note note1 = new note(position, name, link);
                notes.add(note1);
                categorylink[arrayval++]=link;

            }

            final noteadaptor adapter = new noteadaptor(Notes.this, R.layout.list_item_row, notes);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String getSelection(){
        return selection;
    }


}
