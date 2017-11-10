package com.devapps.igor.Screens.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.Login.LoginActivity;
import com.devapps.igor.Screens.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by eduar on 04/05/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDatabaseReference = Database.getUsersReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    checkIfEmailVerified(user);
                } else {
                    // User is signed out
                    onLoginFailed();
                }
                // ...
            }
        };

    }

    private void checkIfEmailVerified(FirebaseUser user) {

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            onLoginSuccess(user);
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            onLoginFailed();
            //restart this activity

        }
    }

    private void onLoginSuccess(FirebaseUser user){
        Database.getUsersReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile userProfile = dataSnapshot.getValue(Profile.class);
                if(userProfile != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("userProfile", userProfile);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void onLoginFailed(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
