package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class listUsers extends AppCompatActivity {
    public static String mCID;
    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    public FirebaseListAdapter<User> adapter;
    private boolean nameExists;
    private String addition = " (You)";
    private String setText, k, key, chatName, chatDescription;
    private ListView readMessageList, mUsers;
    private DatabaseReference mRef;
    TextView text;
    EditText cName, mDescription;
    ImageButton mSet, mSetDescription;
    Button mCreate;
    Boolean descriptionExists = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mRef = FirebaseDatabase.getInstance().getReference();
        mSet = findViewById(R.id.createButton);
        mUsers = findViewById(R.id.listView);
        cName = findViewById(R.id.chat_name);
        mCreate = findViewById(R.id.createChatButton);
        mDescription = findViewById(R.id.chat_description);
        mSetDescription = findViewById(R.id.setDescription);
        chatName = cName.getText().toString();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .limitToLast(50);
        FirebaseListOptions<User> options = new FirebaseListOptions.Builder<User>()
                .setLayout(R.layout.list_user)
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseListAdapter<User>(options) {
            @Override
            protected void populateView(View v, User model, int position) {
                // Bind the Chat to the view

                text = v.findViewById(R.id.nameText);


                if (!Main.USER.equals(model.getName())) {
                    text.setText(model.getName());
                } else {
                    setText = model.getName() + addition;
                    text.setText(setText);

                }

            }
        };

        readMessageList = findViewById(R.id.listView);
        readMessageList.setAdapter(adapter);
        Log.e("test","44");
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatName = cName.getText().toString();
                if(chatName.isEmpty()){
                    Toast.makeText(listUsers.this, "Type chat name first", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast toast = Toast.makeText(listUsers.this, "Chat name set\nPlease add users now", Toast.LENGTH_LONG);
                    TextView tt = toast.getView().findViewById(android.R.id.message);
                    if (tt != null) tt.setGravity(Gravity.CENTER);
                    toast.show();

                    k = mRef.child("chats").push().getKey();
                    cName.getText().clear();

                }
            }
        });
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(listUsers.this, Main.class);
                startActivity(i);
            }
        });
        Log.e("test","22");
        mDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatDescription = mDescription.getText().toString();
            }
        });
        mSetDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                chatDescription = mDescription.getText().toString();
                if(chatDescription.isEmpty()){
                    Toast.makeText(listUsers.this, "Set a description first", Toast.LENGTH_LONG).show();
                }
                else{
                    descriptionExists = true;
                    Toast.makeText(listUsers.this, "Description set", Toast.LENGTH_LONG).show();
                    mDescription.getText().clear();
                }
            }
        });
        Log.e("test","33");

        mUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(chatName.isEmpty()){
                    Toast.makeText(listUsers.this, "Set a Chat Name First", Toast.LENGTH_LONG).show();

                }
                else {
                    Log.e("test","11");
                    View v;
                    text = view.findViewById(R.id.nameText);
                    key = adapter.getRef(position).getKey();
                    if (!Main.ID.equals(key)) {
                        v = parent.getChildAt(position);
                        v.setVisibility(View.GONE);

                        Toast.makeText(listUsers.this, "User Added", Toast.LENGTH_LONG).show();

                        mRef = FirebaseDatabase.getInstance().getReference();

                        mRef.child("chats").child(k).setValue(k);
                        mRef.child("chats").child(k).child("name").setValue(chatName);
                        if(descriptionExists){
                            mRef.child("chats").child(k).child("description").setValue(chatDescription);
                        }
                        else {
                            mRef.child("chats").child(k).child("description").setValue("");
                        }
                        mRef.child("users").child(Main.ID).child("chats").child(k).child("name").setValue(chatName);
                        mRef.child("users").child(key).child("chats").child(k).child("name").setValue(chatName);
                        mCID = k;

                    } else {
                        Toast.makeText(listUsers.this, "It's you!", Toast.LENGTH_LONG).show();
                    }
                }
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


}


