package com.mygdx.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

   public static List<String> nativeWords = new ArrayList<>();
   public static Map<String, String> translatedPairs = new HashMap<>();

   public Toolbar menuBar;
   public TextView resultTV;
   public ImageButton playBtn, chatBtn, learnBtn, allUsers;
   public ImageView messageImv, requestImv;
   private FirebaseAuth mAuth;

   DatabaseReference notificationRef, usersRef;


   NotificationBadge requestBadge, messageBadge;
   ListView requestNotificationsListView, messageNotificationsListView;
   AlertDialog requestNotificationsDialog, messageNotificationsDialog;

    String saved_email="", saved_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mAuth = FirebaseAuth.getInstance();

        //SharedPreferences preferences = getApplicationContext().getSharedPreferences("login",0);
        //saved_email = preferences.getString("email", "");
        //saved_password = preferences.getString("password", "");

        menuBar = (Toolbar) findViewById(R.id.menuToolbar);
        setSupportActionBar(menuBar);

        requestNotificationsListView = new ListView(this);
        requestImv = (ImageView)findViewById(R.id.requestNotification);
        requestBadge = (NotificationBadge) findViewById(R.id.requestNotificationBadge) ;

        learnBtn = (ImageButton) findViewById(R.id.learnBTN) ;
        playBtn = (ImageButton) findViewById(R.id.playBTN);
        chatBtn = (ImageButton) findViewById(R.id.chatBTN) ;
        allUsers = (ImageButton) findViewById(R.id.userListBTN);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("DEBUGGING", "1. categoryActivity");
                 Intent intent = new Intent(CategoryActivity.this, CategoryChooserActivity.class);
                 intent.putExtra("sender", "game");
                 startActivity(intent);
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ChatBotActivity.class);
                startActivity(intent);

            }
        });

        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CategoryChooserActivity.class);
                intent.putExtra("sender", "study");
                startActivity(intent);
            }
        });


        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ExploreTabActivity.class);
                startActivity(intent);
            }
        });

        requestImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNotificationsDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       if (LoginActivity.current_user == null) {
            Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(LoginActivity.current_user.getUid());
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

            notificationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int number=0;
                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot ds : snapshots) {
                        number += 1;
                    }
                    requestBadge.setNumber(number);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseListAdapter<Notification> firebaseListAdapter = new FirebaseListAdapter<Notification>(this, Notification.class, R.layout.notification_item, notificationRef) {
                @Override
                protected void populateView(View v, Notification model, int position) {
                        final String current_sender = model.getSender();
                        final String current_type = model.getType();
                        final User sender_user = new User();
                        final TextView notification = (TextView) v.findViewById(R.id.notificationTV);
                        Log.wtf("TYPE", current_type);
                        usersRef.child(current_sender).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                sender_user.setUsername(dataSnapshot.child("username").getValue().toString());
                                sender_user.setEmail(dataSnapshot.child("email").getValue().toString());

                                if (current_type.equals("friend_request")) {
                                    sender_user.setNativeLanguage(dataSnapshot.child("nativeLanguage").getValue().toString());
                                    sender_user.setStudyingLanguage(dataSnapshot.child("studyingLanguage").getValue().toString());
                                }

                                if (current_type.equals("friend_request")) {
                                    notification.setText("Friend Request: " + sender_user.getUsername() + " (" + sender_user.getEmail()
                                            + ") native in " + sender_user.getNativeLanguage() + ", studying "
                                            + sender_user.getStudyingLanguage());
                                }
                                else {
                                    notification.setText("New Message: " + sender_user.getUsername() + " (" + sender_user.getEmail() + ")");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestBadge.clear();
                }
            });
            builder.setNegativeButton("Clear Notifications", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notificationRef.removeValue();
                    requestBadge.clear();
                }
            });
            builder.setView(requestNotificationsListView);
            requestNotificationsDialog = builder.create();
            requestNotificationsListView.setAdapter(firebaseListAdapter);

           /* FirebaseListAdapter<Notification> firebaseListAdapter2 = new FirebaseListAdapter<Notification>(this, Notification.class, R.layout.notification_item, notificationRef) {
                @Override
                protected void populateView(View v, Notification model, int position) {
                    if (! model.getType().equals("friend_request")) {
                        final String current_sender = model.getSender();
                        final String current_type = model.getType();
                        final User sender_user = new User();
                        final TextView notification = (TextView) v.findViewById(R.id.notificationTV);
                        usersRef.child(current_sender).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                sender_user.setUsername(dataSnapshot.child("username").getValue().toString());
                                sender_user.setEmail(dataSnapshot.child("email").getValue().toString());
                                if (current_type.equals("friend_request")) {
                                    sender_user.setNativeLanguage(dataSnapshot.child("nativeLanguage").getValue().toString());
                                    sender_user.setStudyingLanguage(dataSnapshot.child("studyingLanguage").getValue().toString());
                                }

                               // if (current_type.equals("friend_request")) {
                               //     notification.setText("Friend Request: " + sender_user.getUsername() + " (" + sender_user.getEmail()
                               //             + ") native in " + sender_user.getNativeLanguage() + ", studying "
                              //              + sender_user.getStudyingLanguage());
                                //} else {
                                     notification.setText("New Message: " + sender_user.getUsername() + " (" + sender_user.getEmail() + ")");
                               // }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            };




            AlertDialog.Builder builder2 = new AlertDialog.Builder(CategoryActivity.this);
            builder.setPositiveButton("OK", null);
            builder.setView(messageNotificationsListView);
            messageNotificationsDialog = builder2.create();
            messageNotificationsListView.setAdapter(firebaseListAdapter2);*/

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_explore_tab, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings: {}

            case R.id.action_logout:{
                Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
