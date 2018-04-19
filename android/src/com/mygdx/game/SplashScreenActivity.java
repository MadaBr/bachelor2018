package com.mygdx.game;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    Animation fromTop;
    Animation fromBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        ImageView circle1 = (ImageView)findViewById(R.id.circle1ImgV) ;
        ImageView circle2 = (ImageView)findViewById(R.id.circle2ImgV) ;
        ImageView circle3 = (ImageView)findViewById(R.id.circle3ImgV) ;

        fromTop = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.from_top);
        fromBottom = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.anim.from_bottom);
        fromTop.setDuration(1000);
        fromBottom.setDuration(1000);

        circle1.setAnimation(fromTop);
        circle2.setAnimation(fromBottom);
        circle3.setAnimation(fromTop);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CategoryActivity.nativeWords.clear();

                Iterable<DataSnapshot> words = dataSnapshot.child("categories").child("animals").getChildren();
                for(DataSnapshot ds : words){
                    CategoryActivity.nativeWords.add(ds.getValue().toString());
                }
                    //decomment to use translation
                GetWordTranslation translator = new GetWordTranslation();
                translator.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class GetWordTranslation extends android.os.AsyncTask <Void, Void, Map<String, String>>{

        //"https://glosbe.com/gapi/translate?from=eng&dest=kor&format=json&phrase="
        String origin = "https://glosbe.com/gapi/translate?from=";
        String from = LoginActivity.nativeLanguage;
        String destLinkConnector = "&dest=";
        String dest = LoginActivity.studyingLanguage;
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
                CategoryActivity.translatedPairs.put(pair.getKey(), pair.getValue());
            }

            //decomment to use translation
            Intent intent = new Intent(SplashScreenActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
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

                   for(int i = 0; i< CategoryActivity.nativeWords.size(); i++){
                    apiCallUrl = new URL(linkConfig.toString() + CategoryActivity.nativeWords.get(i) + prettyFormat);

                    /*conn = (HttpURLConnection) apiCallUrl.openConnection();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bfr = new BufferedReader(isr);
                    String line="";
                    StringBuilder builder = new StringBuilder();

                    while((line=bfr.readLine()) != null){
                        builder.append(line);
                    }

                    translations.put(CategoryActivity.nativeWords.get(i), parseJSON(builder.toString()));

                    bfr.close();
                    isr.close();
                    is.close();
                    conn.disconnect();*/
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
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
