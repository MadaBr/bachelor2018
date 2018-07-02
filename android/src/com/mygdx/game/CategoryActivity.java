package com.mygdx.game;

import android.content.Intent;
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
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
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
   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //if user is signed in - show this activity
        //else redirect to login - onStart()
        mAuth = FirebaseAuth.getInstance();

        menuBar = (Toolbar) findViewById(R.id.menuToolbar);
        setSupportActionBar(menuBar);

        Button viewCategory = (Button)findViewById(R.id.viewCategoryBTN);
        resultTV = (TextView) findViewById(R.id.translationResultTV) ;

        learnBtn = (ImageButton) findViewById(R.id.learnBTN) ;

        playBtn = (ImageButton) findViewById(R.id.playBTN);
       // playBtn.setVisibility(View.INVISIBLE);

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
                Intent intent = new Intent(CategoryActivity.this, TeachingActivity.class);
                startActivity(intent);
            }
        });

        viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CategoryChooserActivity.class);
                startActivity(intent);
            }
        });

        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(CategoryActivity.this, AllUsersActivity.class);
               // startActivity(intent);
                 Intent intent = new Intent(CategoryActivity.this, ExploreTabActivity.class);
                 startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(LoginActivity.current_user == null){
            Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
