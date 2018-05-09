package com.androidchatapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupChat extends AppCompatActivity{
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mMessagesDatabaseReferenceM;


    String name, userName;
    EditText inputName, add;
    Button createButton1;
    FloatingActionButton fab3;
    String ok = "parth";
    String k;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        createButton1 = findViewById(R.id.createButton);
        add = findViewById(R.id.inputUserName);
        inputName = findViewById(R.id.editText);

        //userInput = findViewById(R.id.inputUser);
        fab3 = findViewById(R.id.fab1);
        //add = findViewById(R.id.addUser);
        //createButton1.setText(ok);

        //inputName.setText(ok);
        //Firebase.setAndroidContext(this);

        Firebase.setAndroidContext(this);



        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Snackbar.make(view, "work", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(CreateGroupChat.this, "created group", Toast.LENGTH_LONG).show();

                name = inputName.getText().toString();

                mMessagesDatabaseReferenceM = mFirebaseDatabase.getReference();

                k = mMessagesDatabaseReferenceM.child("groups").push().getKey();
                mMessagesDatabaseReferenceM.child("groups").child(k).child("name").setValue(name);
                mMessagesDatabaseReferenceM.child("groups").child(k).child("members").child(UserDetails.username).setValue(UserDetails.username);
                mMessagesDatabaseReferenceM.child("users").child(UserDetails.username).child("groups").child(k).setValue(k);
                //setContentView(R.layout.activity_add);
                //startActivity(new Intent(CreateGroupChat.this, AddUsers.class));
                //addUsers();
                }
        });
        createButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(CreateGroupChat.this, "user added", Toast.LENGTH_LONG).show();
                userName = add.getText().toString();
                mMessagesDatabaseReferenceM.child("groups").child(k).child("members").child(userName).setValue(userName);
                mMessagesDatabaseReferenceM.child("users").child(userName).child("groups").child(k).setValue(k);
//                name = inputName.getText().toString();
//                reference1.setValue(name);

            }
        });
//        add.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                //userName = userInput.getText().toString();
//                Toast.makeText(CreateGroupChat.this, "added user", Toast.LENGTH_LONG).show();
//                //mMessagesDatabaseReferenceM.child("groups").child(k).child("members").child("username").setValue(userName);
//                //mMessagesDatabaseReferenceM.child("users").child(userName).child("groups").child(k).setValue(k);
////                name = inputName.getText().toString();
////                reference1.setValue(name);
//
//            }
//        });






    }
    //public void addUsers() {

//
//        String url = "https://chat-1d9a1.firebaseio.com/users.json";
//
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String s) {
//                doOnSuccess(s);
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("" + volleyError);
//            }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
//        rQueue.add(request);
//
 //   }
}

