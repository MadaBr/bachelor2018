package com.mygdx.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TeachingActivity extends AppCompatActivity {
    public List<TeachingItem> items;
    public RecyclerView teachingRecyclerV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);

        items = new ArrayList<>();

        teachingRecyclerV = (RecyclerView) findViewById(R.id.teachingRV);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> retrievedItems = dataSnapshot.child("teaching").child("categories").child("people").getChildren();
                for(DataSnapshot ds : retrievedItems){
                    items.add(new TeachingItem(ds.child("img").getValue().toString(),ds.child(LoginActivity.studyingLanguage).getValue().toString(),
                                ds.child(LoginActivity.nativeLanguage).getValue().toString(),ds.child(LoginActivity.studyingLanguage).getKey().toString(),
                                ds.child(LoginActivity.nativeLanguage).getKey().toString()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager LLManager = new LinearLayoutManager(TeachingActivity.this);
        LLManager.setStackFromEnd(true);
        teachingRecyclerV.setLayoutManager(LLManager);

        final RecyclerAdapter adapter = new RecyclerAdapter(TeachingActivity.this,R.layout.teaching_item_layout,items);
        teachingRecyclerV.setAdapter(adapter);

    }
}
