package com.google.firebase.udacity.friendlychat;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class mainChat extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;


    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private ImageView mTypingIndicator;
    private String mUsername, val, isTyping;
    private TextView mChat, mChatDescription;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private DatabaseReference mMessagesDatabaseReference, mIDR, mIDR2, mIDR3;
    private ChildEventListener mChildEventListener, mChildEventListener2;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("log","aaa");
        val = "false";
        mUsername = Main.USER;
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mIDR = mFirebaseDatabase.getReference().child("chats").child(MainLoggedIn.chatKey).child("typing");
        mIDR2 = mFirebaseDatabase.getReference().child("chats").child(MainLoggedIn.chatKey);
        mIDR3 = mFirebaseDatabase.getReference().child("chats").child(MainLoggedIn.chatKey).child("name");
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages").child(MainLoggedIn.chatKey);
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("photos");

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mTypingIndicator = findViewById(R.id.typingIndicator);
        mChat = findViewById(R.id.chatNameD);
        mChatDescription = findViewById(R.id.chatNameDescription);
        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        mIDR.setValue(val);
        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mTypingIndicator.setVisibility(View.INVISIBLE);
        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("chats").child(MainLoggedIn.chatKey).child("name");

        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mChatName = dataSnapshot.getValue().toString();
                Log.e("nameLog",mChatName);
                mChat.setText(mChatName);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference myRef2 = database.getReference().child("chats").child(MainLoggedIn.chatKey).child("description");

        myRef2.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mDescription = dataSnapshot.getValue().toString();
                Log.e("nameLog",mDescription);
                if(!mDescription.isEmpty()){
                    mChatDescription.setText(mDescription);
                }
                else{
                    mChatDescription.setText("");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        attachDatabaseReadListener();
        // Enable Send button when there's text to send
        Log.e("log","bbb");

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mIDR.setValue("true");
                    mSendButton.setEnabled(true);
                } else {
                    mIDR.setValue(val);
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        Log.e("log","ccc");
        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mMessageEditText.setText("");
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername, null);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mTypingIndicator.setVisibility(View.INVISIBLE);
                mIDR.setValue(val);
                mMessageEditText.setText("");
            }
        });
        Log.e("log","ddd");
    }

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
        if (resultCode == RESULT_OK) {
            // Sign-in succeeded, set up the UI
            Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            // Sign in was canceled by the user, finish the activity
            Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
            finish();
        }
    } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
        Uri selectedImageUri = data.getData();

        // Get a reference to store file at chat_photos/<FILENAME>
        StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

        // Upload file to Firebase Storage
        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        // Set the download URL to the message box, so that the user can send it to the database
                        FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString());
                        mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    }
                });
    }
}
        @Override
        protected void onPause(){
            super.onPause();
            mIDR.setValue(val);

        }
        @Override
        protected void onResume(){
            super.onResume();
        }

        private void attachDatabaseReadListener() {
            Log.e("log","eee");
            if (mChildEventListener == null) {
                mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                        mMessageAdapter.add(friendlyMessage);
                        Log.e("log","fff");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
            }
            if (mChildEventListener2 == null) {
                mChildEventListener2 = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        isTyping = dataSnapshot.getValue().toString();
                        Log.e("type",isTyping);
                        if(isTyping.equals("true")){
                            mTypingIndicator.setVisibility(View.VISIBLE);
                        }
                        else{
                            mTypingIndicator.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mIDR2.addChildEventListener(mChildEventListener2);
            }
        }






        private void detachDatabaseReadListener(){
            if (mChildEventListener != null){
                mMessagesDatabaseReference.removeEventListener(mChildEventListener);
                mChildEventListener = null;
            }
            if (mChildEventListener2 != null){
                mIDR2.removeEventListener(mChildEventListener2);
                mChildEventListener2 = null;
            }

        }
    }

