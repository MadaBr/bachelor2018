package com.mygdx.game;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Mada on 3/30/2018.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private Context context;
    private int layoutId;
    private List<Integer> icons;


    public CategoryRecyclerAdapter(Context context, int layoutId, List<Integer> icons){

        this.context = context;
        this.layoutId = layoutId;
        this.icons = icons;

    }
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        CategoryViewHolder holder = new CategoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        holder.icon.setImageResource(icons.get(position));
        String resourceName = context.getResources().getResourceEntryName(icons.get(position)).split("_")[0];
        holder.categoryName.setText(resourceName.toUpperCase());

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout categoryItem;
        public TextView categoryName;
        public ImageView icon;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView)itemView.findViewById(R.id.categoryNameTV);
            icon = (ImageView)itemView.findViewById(R.id.categoryImageV);
            categoryItem = (LinearLayout)itemView.findViewById(R.id.categoryItemLayout);

            categoryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CategoryChooserActivity.redirect.equals("learn")){
                        Intent intent = new Intent(v.getContext(),TeachingActivity.class);
                        v.getContext().startActivity(intent);
                    }
                    else if (CategoryChooserActivity.redirect.equals("play")){
                        Intent intent = new Intent("startSpaceGame");
                        v.getContext().startActivity(intent);
                    }
        }
    });

        }
    }
}
