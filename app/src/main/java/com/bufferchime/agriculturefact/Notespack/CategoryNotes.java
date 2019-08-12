package com.bufferchime.agriculturefact.Notespack;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bufferchime.agriculturefact.AsyncResult;
import com.bufferchime.agriculturefact.Contact;
import com.bufferchime.agriculturefact.Currentaffairs;
import com.bufferchime.agriculturefact.DownloadWebpageTask;
import com.bufferchime.agriculturefact.Home;
import com.bufferchime.agriculturefact.NewPdfView;
import com.bufferchime.agriculturefact.Notes;
import com.bufferchime.agriculturefact.Pdfview;
import com.bufferchime.agriculturefact.Quiz;
import com.bufferchime.agriculturefact.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryNotes extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    protected BottomNavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<note> notes = new ArrayList<note>();
    ListView listview;
    String key=Notes.getSelection();
    TextView heading;
    public static String selection="0";
    public static String categorylink[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        heading=(TextView)findViewById(R.id.textView);


        //sheetparts
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
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                        //    if(position==0){
                        //selection=categorylink[position];
                        Intent appInfo = new Intent(CategoryNotes.this, Pdfview.class);
                        appInfo.putExtra("selection", categorylink[position]);

                        startActivity(appInfo);
                    }

                });

            }});




        Toast.makeText(this, "Click to Open notes", Toast.LENGTH_SHORT).show();












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
                    CategoryNotes.this.startActivity(new Intent(CategoryNotes.this, Home.class));
                } else if (itemId == R.id.navigation_affairs) {
                    CategoryNotes.this.startActivity(new Intent(CategoryNotes.this, Currentaffairs.class));
                } else if (itemId == R.id.navigation_notes) {
                    CategoryNotes.this.startActivity(new Intent(CategoryNotes.this, Notes.class));
                } else if (itemId == R.id.navigation_tests) {
                    CategoryNotes.this.startActivity(new Intent(CategoryNotes.this, Quiz.class));
                } else if (itemId == R.id.navigation_contact) {
                    CategoryNotes.this.startActivity(new Intent(CategoryNotes.this, Contact.class));
                }
                CategoryNotes.this.finish();
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


//NavigationViewEnd


    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            categorylink = new String[105];
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

            final noteadaptor adapter = new noteadaptor(CategoryNotes.this, R.layout.list_item_row, notes);
            listview.setAdapter(adapter);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static String getSelection(){
        return selection;
    }

}
