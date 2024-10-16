package com.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userReference;

    private TextView profileName, profileUserName, profileEmail, profilePhone, profilePassword;
    private ImageView profileImage;
    private TextView titleName, titleUserName;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button contactUsButton = findViewById(R.id.contactUsButton);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactUsActivity();
            }
        });

        Button aboutUsButton = findViewById(R.id.aboutUsButton);
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutUsActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d("UserProfileActivity", "User is not null. Email: " + currentUser.getEmail());

            // Initialize Firebase Realtime Database reference
            DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("Users");

            String userIdentifier;

            // Check if the user's phone number is not null
            if (currentUser.getPhoneNumber() != null) {
                Log.d("UserProfileActivity", "User's phone number: " + currentUser.getPhoneNumber());
                userIdentifier = currentUser.getPhoneNumber();
            } else {
                Log.e("UserProfileActivity", "User's phone number is null");
                userIdentifier = currentUser.getUid();
            }

            // Continue with the initialization if userIdentifier is not null
            if (userIdentifier != null) {
                userReference = rootReference.child(userIdentifier);
                Log.d("UserProfileActivity", "userReference path: " + userReference);

                // Initialize TextViews and ImageView
                profileUserName = findViewById(R.id.ProfileUserName);
                profileEmail = findViewById(R.id.ProfilEmail);
                profilePhone = findViewById(R.id.ProfilePhone);
                profilePassword = findViewById(R.id.ProfilePassword);
                profileImage = findViewById(R.id.profileImage);
                titleName = findViewById(R.id.titleName);
                titleUserName = findViewById(R.id.titleUserName);
                logoutButton = findViewById(R.id.logout);

                // Read user details from the database
                readUserDetails();
            } else {
                Log.e("UserProfileActivity", "UserIdentifier is null");
            }
        } else {
            Log.e("UserProfileActivity", "User is null. Redirecting to login.");
            // Consider redirecting to login or handle the case where the user is not authenticated.
            // Example: startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
        }
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this,LogIn.class);
                startActivity(intent);
            }
        });
    }

    private void readUserDetails() {
        Log.d("UserProfileActivity", "Reading user details from the database.");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("UserProfileActivity", "Data snapshot exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    // Get user details from the database
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    // Update TextViews with user details
                    profileName.setText(name);
                    profileUserName.setText(username);
                    profileEmail.setText(email);
                    profilePhone.setText(phone);
                    profilePassword.setText(password);


                    titleName.setText(name);
                    titleUserName.setText(username);

                } else {
                    Log.e("UserProfileActivity", "Data snapshot does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserProfileActivity", "Error reading user details from the database", databaseError.toException());
            }
        });
    }

    public void openAboutUsActivity() {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void openContactUsActivity() {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }
}

