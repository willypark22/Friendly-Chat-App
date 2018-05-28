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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainLoggedIn extends AppCompatActivity {
    private TextView mText;
    private FirebaseListAdapter<Chat> adapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    ListView mChat;

    public String key;
    public static String chatKey;
    String t;
    FloatingActionButton mFab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.e("log", "1");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        t = user.getUid();

        mFab = findViewById(R.id.floatingActionButton);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainLoggedIn.this, listUsers.class);
                startActivity(i);

            }

        });
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(t)
                .child("chats")
                .limitToLast(50);
        FirebaseListOptions<Chat> options = new FirebaseListOptions.Builder<Chat>()
                .setLayout(R.layout.list_chat)
                .setQuery(query, Chat.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseListAdapter<Chat>(options) {
            @Override
            protected void populateView(View v, Chat model, int position) {
                // Bind the Chat to the view

                mText = v.findViewById(R.id.chatTextView);
                //t = model.getName();
                mText.setText(model.getName());
            }
        };
        mChat = findViewById(R.id.chatListView);
        mChat.setAdapter(adapter);

        mChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                key = adapter.getRef(position).getKey();
                chatKey = key;
                Intent i = new Intent(MainLoggedIn.this, mainChat.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainLoggedIn.this, Main.class));
                                finish();
                            }
                        });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




