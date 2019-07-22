package com.bufferchime.agriculturefact;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.bufferchime.agriculturefact.activity.MainActivity;

public class Contact extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    protected BottomNavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
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
                    Contact.this.startActivity(new Intent(Contact.this, Home.class));
                } else if (itemId == R.id.navigation_affairs) {
                    Contact.this.startActivity(new Intent(Contact.this, OlderPosts.class));
                } else if (itemId == R.id.navigation_notes) {
                    Contact.this.startActivity(new Intent(Contact.this, Notes.class));
                } else if (itemId == R.id.navigation_tests) {
                    Contact.this.startActivity(new Intent(Contact.this, MainActivity.class));
                } else if (itemId == R.id.navigation_contact) {
                    Contact.this.startActivity(new Intent(Contact.this, Contact.class));
                }
                Contact.this.finish();
            }
        }, 300);
        return true;
    }
    private void updateNavigationBarState(){
        int actionId = R.id.navigation_contact;
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

}
