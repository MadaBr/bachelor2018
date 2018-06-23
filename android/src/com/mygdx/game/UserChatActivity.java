package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ai.api.model.AIRequest;

public class UserChatActivity extends AppCompatActivity {
    private static List<ChatMessage> usersMessages;
    private String destination_UID;
    private String userMessage;
    private DatabaseReference ref;

    private RecyclerView messagesRecyclerView;
    private EditText userMessageET;
    private ImageView sendMessageIMV;

    private FirebaseRecyclerAdapter<ChatMessage, RecyclerAdapter.RecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        Intent intent = getIntent();
        destination_UID = intent.getStringExtra("destination_UID");

        messagesRecyclerView = findViewById(R.id.userChatRV);
        userMessageET = findViewById(R.id.userChatMessageET);
        sendMessageIMV = findViewById(R.id.userChatsendIMV);

        usersMessages = new ArrayList<>();

        messagesRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager LLayoutManager = new LinearLayoutManager(UserChatActivity.this);
        messagesRecyclerView.setLayoutManager(LLayoutManager);


        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        //ref.child("UserChat").child(LoginActivity.current_user.getUid()).removeValue();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, RecyclerAdapter.RecyclerViewHolder>(ChatMessage.class,R.layout.messages_layout,RecyclerAdapter.RecyclerViewHolder.class,ref.child("UserChat").child(LoginActivity.current_user.getUid()).child(destination_UID)) {
            @Override
            protected void populateViewHolder(RecyclerAdapter.RecyclerViewHolder holder, ChatMessage model, int position) {
                if(model.getParticipant().equals(LoginActivity.current_user.getUid())){
                    holder.userMessage.setVisibility(View.VISIBLE);
                    holder.userMessage.setText(model.getMessage());
                    holder.botMessage.setVisibility(View.INVISIBLE);
                    Log.wtf("TAG", "showed userSender message");
                }
                else{
                    holder.userMessage.setVisibility(View.INVISIBLE);
                    holder.botMessage.setVisibility(View.VISIBLE);
                    holder.botMessage.setText(model.getMessage());
                    Log.wtf("TAG", "showed bot message");
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCounter = adapter.getItemCount();
                int lastVisiblePosition = LLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCounter - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                   messagesRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        messagesRecyclerView.setAdapter(adapter);


        sendMessageIMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMessage = userMessageET.getText().toString();
                if( !userMessage.equals("")){
                    userMessageET.setText(""); //.child(LoginActivity.current_user.getUid())
                    ref.child("UserChat").child(LoginActivity.current_user.getUid()).child(destination_UID).push().setValue(new ChatMessage(userMessage, LoginActivity.current_user.getUid()));
                    ref.child("UserChat").child(destination_UID).child(LoginActivity.current_user.getUid()).push().setValue(new ChatMessage(userMessage, LoginActivity.current_user.getUid()));
                }
            }
        });
    }
}
