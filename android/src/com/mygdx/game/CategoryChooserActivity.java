package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CategoryChooserActivity extends AppCompatActivity {

    RecyclerView categoryRecyclerV;
    List<Integer> icons;
    public static String sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        Intent intent = getIntent();
        sender = intent.getStringExtra("sender");
        Log.wtf("DEBUGGING", "1.1 started by categoryActivity");
        icons = new ArrayList<>();
        icons.add(R.drawable.people_104);
        icons.add(R.drawable.animals_104);
        icons.add(R.drawable.actions_104);

        categoryRecyclerV = (RecyclerView) findViewById(R.id.categoryRV);
        categoryRecyclerV.hasFixedSize();

        GridLayoutManager GLManager = new GridLayoutManager(CategoryChooserActivity.this,2);
        categoryRecyclerV.setLayoutManager(GLManager);

        CategoryRecyclerAdapter categoryAdapter = new CategoryRecyclerAdapter(CategoryChooserActivity.this,R.layout.category_item_layout,icons);
        categoryRecyclerV.setAdapter(categoryAdapter);
    }
}
