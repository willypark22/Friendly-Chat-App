package com.androidchatapp;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupChat extends AppCompatActivity{
    Firebase reference1;
    String name;
    EditText inputName;
    Button createButton1;
    FloatingActionButton fab3;
    String ok = "butt";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createButton1 = findViewById(R.id.createButton);
        inputName = findViewById(R.id.editText);
        createButton1.setText(ok);
        inputName.setText(ok);
        //Firebase.setAndroidContext(this);

        reference1 = reference1.child("Groups");
        fab3 = findViewById(R.id.fab1);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "work", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    Toast.makeText(CreateGroupChat.this, "created group", Toast.LENGTH_LONG).show();
                    name = inputName.getText().toString();
                    reference1.setValue(name);

                }


        });
        createButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(CreateGroupChat.this, "created group", Toast.LENGTH_LONG).show();
                name = inputName.getText().toString();
                reference1.setValue(name);

            }
        });

    }
}
    public static void main(String[] args) {
        System.out.println(ok);
    }
