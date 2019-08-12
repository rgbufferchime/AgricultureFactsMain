package com.bufferchime.agriculturefact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bufferchime.agriculturefact.Firebase.FirebaseDatabaseHelper;
import com.bufferchime.agriculturefact.Firebase.FirebaseUserEntity;
import com.bufferchime.agriculturefact.helper.Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = EditProfile.class.getSimpleName();

    private EditText editProfileName;

    private EditText editProfileCity;

    private EditText editProfilePhoneNumber,editEmail;
    public Session session1;



    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Checking for first time launch - before calling setContentView()
  /*      session1 = new Session(this);
        if (!session1.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        */
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile Information");

        editProfileName = (EditText)findViewById(R.id.profile_name);
        editProfileCity = (EditText)findViewById(R.id.profile_city);
        editProfilePhoneNumber = (EditText)findViewById(R.id.profile_phone);
        editEmail=(EditText)findViewById(R.id.profile_email);
        Button saveEditButton = (Button)findViewById(R.id.save_edit_button);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileName = editProfileName.getText().toString();
                String profileCountry = editProfileCity.getText().toString();
                String profilePhoneNumber = editProfilePhoneNumber.getText().toString();
                String profileEmail = editEmail.getText().toString();
                // update the user profile information in Firebase database.
                if(TextUtils.isEmpty(profileName) || TextUtils.isEmpty(profileCountry) || TextUtils.isEmpty(profilePhoneNumber)){
                    Helper.displayMessageToast(EditProfile.this, "All fields must be filled");
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Intent firebaseUserIntent = new Intent(EditProfile.this, phverify1.class);
                    startActivity(firebaseUserIntent);
                    finish();
                } else {
                    String userId = user.getProviderId();
                    String id = user.getUid();
                    //   String profileEmail = user.getPhoneNumber();

                    FirebaseUserEntity userEntity = new FirebaseUserEntity(id, profileEmail, profileName, profileCountry, profilePhoneNumber);
                    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                    firebaseDatabaseHelper.createUserInFirebaseDatabase(id, userEntity);

                    editProfileName.setText("");
                    editProfileCity.setText("");
                    editProfilePhoneNumber.setText("");
                    editEmail.setText("");
                    Intent signUpIntent = new Intent(EditProfile.this, OlderPosts.class);
                    startActivity(signUpIntent);

                }
            }
        });
    }

    private void launchHomeScreen() {
        session1.setFirstTimeLaunch(false);
        startActivity(new Intent(EditProfile.this, OlderPosts.class));
        finish();
    }

}