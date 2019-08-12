package com.bufferchime.agriculturefact;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bufferchime.agriculturefact.Firebase.FirebaseDatabaseHelper;
import com.bufferchime.agriculturefact.Firebase.FirebaseStorageHelper;
import com.bufferchime.agriculturefact.helper.AppController;
import com.bufferchime.agriculturefact.helper.Helper;
import com.bufferchime.agriculturefact.helper.SimpleDividerItemDecoration;
import com.bufferchime.agriculturefact.materialdesgin.MainActivity;

import java.util.List;

public class Contact extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    protected BottomNavigationView navigationView;
    Boolean doubleBackToExitPressedOnce = false;



    private ImageView profilePhoto;

    private TextView profileName;

    private TextView city;

    private TextView userStatus;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private String id;

    private static final int REQUEST_READ_PERMISSION = 120;

    public Button edit;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);






        //profilestart






        recyclerView = (RecyclerView)findViewById(R.id.profile_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        ((AppController)this.getApplication()).getFirebaseAuth();
        id = ((AppController)this.getApplication()).getFirebaseUserAuthenticateId();

        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        firebaseDatabaseHelper.isUserKeyExist(id, this, recyclerView);

        //profileend






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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("user id has entered onActivityResult ");
        if (requestCode == Helper.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);
            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            storageHelper.saveProfileImageToCloud(id, selectedImageUri, profilePhoto);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting this permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void fb(View view) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String url = "https://www.facebook.com/"+"agriculturefacts.india";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+url));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url = "https://facebook.com/"+"agriculturefacts.india";
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url));
        }
        this.startActivity(intent);
    }

    public void ytube(View view) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            String url = "http://instagram.com/_u/xxx";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/agriculturefacts"));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url = "https://www.youtube.com/c/agriculturefacts";
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url));
        }
        this.startActivity(intent);
    }

    public void twitter(View view) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            String url = "http://instagram.com/_u/xxx";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/agriculturefact"));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url = "https://twitter.com/agriculturefact";
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url));
        }
        this.startActivity(intent);
    }

    public void insta(View view) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            String url = "http://instagram.com/_u/xxx";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/agriculturefact/"));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url = "https://www.instagram.com/agriculturefact/";
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url));
        }
        this.startActivity(intent);
    }

    public void bufferchime(View view) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            String url = "http://instagram.com/_u/xxx";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/bufferchime/"));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url = "https://www.instagram.com/bufferchime/";
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url));
        }
        this.startActivity(intent);
    }

    public void feedback(View view) {
        Intent intent = null;
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSfWZft4eXC3EJn8FByclGJplSElK55E-NbRuNHsqnLAvC8uyg/viewform?usp=sf_link";
        intent = new Intent(Intent.ACTION_VIEW);
        intent .setData(Uri.parse(url));
        this.startActivity(intent);
    }

    public void rateus(View view) {
        Intent intent = null;
        String url = "market://details?id=" + getPackageName();
        intent = new Intent(Intent.ACTION_VIEW);
        intent .setData(Uri.parse(url));
        this.startActivity(intent);
    }

    public void share(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(i);
    }

    public void privacy(View view) {
        Intent i = new Intent(Contact.this, Privacy.class);
        startActivity(i);
    }
}
