package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;


public class Main extends AppCompatActivity {
    FloatingActionButton mFab;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    public static String USER;
    public static String ID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference, mIDR;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public String mUsername, iD;
    public FirebaseListAdapter<Chat> adapter;
    private String setText, k, key;
    private ListView readMessageList, mUsers, mChat;
    TextView mText;
    private Boolean mLogged;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mUsername = ANONYMOUS;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFab = findViewById(R.id.floatingActionButton);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, listUsers.class);
                startActivity(i);

            }

        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName());
                    Toast.makeText(Main.this, "Logged in", Toast.LENGTH_LONG).show();


                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

        @Override
        protected void onPause(){
            super.onPause();
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

        }
        @Override
        protected void onResume(){
            super.onResume();
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }

        private void onSignedInInitialize(String username){
            mUsername = username;
            USER = username;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                iD = user.getUid();
                ID = iD;
                mIDR = mFirebaseDatabase.getReference();
                mIDR.child("users").child(iD).child("userName").setValue(mUsername);

                Intent i = new Intent(Main.this, MainLoggedIn.class);
                startActivity(i);


            }
        }
        private void onSignedOutCleanup(){
            mUsername = ANONYMOUS;
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
