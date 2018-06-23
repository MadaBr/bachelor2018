package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

   public static List<String> nativeWords = new ArrayList<>();
   public static Map<String, String> translatedPairs = new HashMap<>();

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

                   // Intent intent = new Intent("startSpaceGame");
                    //startActivity(intent);
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



    /*  public class GetWordTranslation extends android.os.AsyncTask <Void, Void, Map<String, String>>{

        //"https://glosbe.com/gapi/translate?from=eng&dest=kor&format=json&phrase="
        String origin = "https://glosbe.com/gapi/translate?from=";
        String from="eng";
        String destLinkConnector = "&dest=";
        String dest ="kor";
        String linkConnector = "&format=json&phrase=";
        String prettyFormat = "&pretty=true";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Map<String, String> map) {
            StringBuilder str = new StringBuilder();
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, String> pair = it.next();
                translatedPairs.put(pair.getKey(), pair.getValue());
               // str.append(pair.getKey() + " = " + pair.getValue() + "\n");
            }

           //  resultTV.setText(str.toString());
             playBtn.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String, String> translations = new HashMap<>();
            URL apiCallUrl = null;
            HttpURLConnection conn =null;

            try{

                StringBuilder linkConfig = new StringBuilder();
                linkConfig.append(origin)
                        .append(from)
                        .append(destLinkConnector)
                        .append(dest)
                        .append(linkConnector);

                for(int i = 0; i< nativeWords.size(); i++){
                    apiCallUrl = new URL(linkConfig.toString()+nativeWords.get(i)+prettyFormat);

                    conn = (HttpURLConnection) apiCallUrl.openConnection();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bfr = new BufferedReader(isr);
                    String line="";
                    StringBuilder builder = new StringBuilder();

                    while((line=bfr.readLine())!=null){
                        builder.append(line);
                    }

                    translations.put(nativeWords.get(i),parseJSON(builder.toString()));

                    bfr.close();
                    isr.close();
                    is.close();
                    conn.disconnect();
                }
            }  catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch(IOException e){
            e.printStackTrace();}

            return translations;
        }

        public String parseJSON(String json){
            String finalTrnsaltion="default";
            try {
                JSONObject result = new JSONObject(json);
                JSONArray tucArray = result.getJSONArray("tuc");
                JSONObject translation = tucArray.getJSONObject(0);
                JSONObject phrase = translation.getJSONObject("phrase");
                finalTrnsaltion = phrase.getString("text");

            } catch(JSONException e){e.printStackTrace();}
            return finalTrnsaltion;
        }
    }*/


}
