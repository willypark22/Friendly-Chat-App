//package com.androidchatapp;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class AddUsers extends AppCompatActivity{
//    EditText userInput;
//    Button add;
//    String userName;
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add);
//
//        userInput = findViewById(R.id.inputUser);
//        add = findViewById(R.id.addUser);
//        add.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick (View v){
//                userName = userInput.getText().toString();
//                Toast.makeText(CreateGroupChat.this, "added user", Toast.LENGTH_LONG).show();
//                //mMessagesDatabaseReferenceM.child("groups").child(k).child("members").child("username").setValue(userName);
//                //mMessagesDatabaseReferenceM.child("users").child(userName).child("groups").child(k).setValue(k);
////                name = inputName.getText().toString();
////                reference1.setValue(name);
//
//            }
//        });
//    }
//
//
//}
//}
