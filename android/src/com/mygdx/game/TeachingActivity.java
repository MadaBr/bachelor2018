package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TeachingActivity extends AppCompatActivity {
   // public List<TeachingItem> items;
    public RecyclerView teachingRecyclerV;
    String category;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);

      //  items = new ArrayList<>();
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        ref = FirebaseDatabase.getInstance().getReference();
        teachingRecyclerV = (RecyclerView) findViewById(R.id.teachingRV);
        ref = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager LLManager = new LinearLayoutManager(TeachingActivity.this);
        //LLManager.setStackFromEnd(true);
        teachingRecyclerV.setLayoutManager(LLManager);

        //---------------

        FirebaseRecyclerAdapter<TeachingFirebaseItem,TeachingViewHolder> firebaseAdapter = new FirebaseRecyclerAdapter<TeachingFirebaseItem, TeachingViewHolder>(TeachingFirebaseItem.class, R.layout.teaching_item_layout, TeachingViewHolder.class, ref.child("teaching").child("categories").child(category)) {
            @Override
            protected void populateViewHolder(final TeachingViewHolder holder, TeachingFirebaseItem model, final int position) {
                Log.wtf("LANGUAGE", LoginActivity.studyingLanguage + " - " + LoginActivity.nativeLanguage);
                Log.wtf("LANGUAGE", model.getWordFromLanguage(LoginActivity.studyingLanguage));
                holder.translatedItem.setText(model.getWordFromLanguage(LoginActivity.studyingLanguage).toLowerCase());
                holder.nativeItem.setText(model.getWordFromLanguage(LoginActivity.nativeLanguage).toLowerCase());
                holder.nativeItem.setVisibility(View.INVISIBLE);

                Picasso.get()
                        .load(model.img)
                        .fit()
                        .centerCrop()
                        .into(holder.img);

            }

        };

      //  final RecyclerAdapter adapter = new RecyclerAdapter(TeachingActivity.this,R.layout.teaching_item_layout,items);
        teachingRecyclerV.setAdapter(firebaseAdapter);

    }

    public static class TeachingViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nativeItem, translatedItem;
        Button translateBtn;

        public TeachingViewHolder(View itemView) {
            super(itemView);
           img = (ImageView) itemView.findViewById(R.id.teachingItemIMGV);
           translatedItem = (TextView) itemView.findViewById(R.id.translatedItemTV);
           nativeItem = (TextView) itemView.findViewById(R.id.nativeItemTV);
           translateBtn = (Button) itemView.findViewById(R.id.translateBTN) ;

            translateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeItem.setVisibility(View.VISIBLE);
                }
            });

        }
    }
}


