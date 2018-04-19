package com.mygdx.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.List;

/**
 * Created by Mada on 3/24/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TeachingRecyclerViewItem> {
    private Context context;
    private int layoutId;
    private List<TeachingItem> items;

    public RecyclerAdapter(Context context, int layoutId, List<TeachingItem> items) {
        this.context = context;
        this.layoutId = layoutId;
        this.items = items;
    }

    @Override
    public TeachingRecyclerViewItem onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.wtf("Teaching", "in adapter");
       View v = LayoutInflater.from(context).inflate(layoutId,parent,false);
        TeachingRecyclerViewItem rv = new TeachingRecyclerViewItem(v);
       return rv;
    }

    @Override
    public void onBindViewHolder(TeachingRecyclerViewItem holder, int position) {
        TeachingItem current_item = items.get(position);
        Log.wtf("Teaching", items.get(position).getImgURL());
        holder.translatedItem.setText(current_item.getTranslatedItem());
        holder.nativeItem.setText(current_item.getNativeItem());
        holder.nativeItem.setVisibility(View.INVISIBLE);

        Picasso.get()
                .load(current_item.getImgURL())
                .fit()
                .centerCrop()
                .into(holder.image);



       /* String base64Image = current_item.getImgURL().split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.image.setImageBitmap(decodedByte);*/
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class TeachingRecyclerViewItem extends  RecyclerView.ViewHolder{
       public TextView translatedItem, nativeItem;
        public ImageView image;
        public Button translateBtn;
        public LinearLayout linearL;

        public TeachingRecyclerViewItem(View itemView) {
            super(itemView);
            translatedItem = (TextView)itemView.findViewById(R.id.translatedItemTV);
            nativeItem = (TextView)itemView.findViewById(R.id.nativeItemTV);
            image = (ImageView)itemView.findViewById(R.id.teachingItemIMGV);
            translateBtn = (Button)itemView.findViewById(R.id.translateBTN);
            linearL = (LinearLayout)itemView.findViewById(R.id.teachingLLItem) ;
            linearL.getBackground().setAlpha(200);

            translateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeItem.setVisibility(View.VISIBLE);
                }
            });


        }
    }







    //View with controls from message layout
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView botMessage, userMessage;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            botMessage = (TextView)itemView.findViewById(R.id.botTV);
            userMessage = (TextView)itemView.findViewById(R.id.userTV);

        }
    }

}
