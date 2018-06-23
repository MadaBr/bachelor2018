package com.mygdx.game;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AllUsers_FRAGMENT extends Fragment {

    private static final int NOT_FRIENDS = 0, FRIENDS =1, REQ_SENT=2, REQ_RECV=3;
    public static String TAG = "ALL_USERS";

    RecyclerView usersRV;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DatabaseReference friendRequestListRef;
    DatabaseReference friendListRef;
    DatabaseReference notificationRef;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_users_tab,container,false);

        context = view.getContext();

        usersRV = (RecyclerView) view.findViewById(R.id.allUsersTAB_RV);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        friendRequestListRef = firebaseDatabase.getReference().child("Friend_Requests");
        friendListRef = firebaseDatabase.getReference().child("Friend_list");
        notificationRef = firebaseDatabase.getReference().child("Notifications");
        ref.keepSynced(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        usersRV.setLayoutManager(layoutManager);

        FirebaseRecyclerAdapter<User,AllUsersActivity.UserRecyclerViewItem> adapter = new FirebaseRecyclerAdapter<User, AllUsersActivity.UserRecyclerViewItem>(User.class, R.layout.all_users_recycler_item, AllUsersActivity.UserRecyclerViewItem.class, ref.child("Users")) {
            @Override
            protected void populateViewHolder(final AllUsersActivity.UserRecyclerViewItem holder, User model, final int position) {
                final String user_UID = getRef(position).getKey();
                holder.username.setText(model.getUsername());
                holder.userEmail.setText(model.getEmail());

                if (!user_UID.equals(LoginActivity.current_user.getUid())) {
                    final Button item_button = holder.friendRequest;
                    final AllUsersActivity.UserFriendshipState friendship_state = new AllUsersActivity.UserFriendshipState(0);

                    checkFriendshipStatus(user_UID, item_button, friendship_state);

                    holder.username.setText(model.getUsername());


                    holder.friendRequest.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            // --------------------------- NOT FRIENDS --------------------------
                            if (friendship_state.getFriendshipState() == NOT_FRIENDS) {
                                sendFriendRequest(user_UID, item_button, friendship_state);
                            }
                            // --------------------------- CANCEL REQUEST --------------------------
                            if (friendship_state.getFriendshipState() == REQ_SENT) {
                                cancelFriendRequest(user_UID, item_button, friendship_state);
                            }
                            if (friendship_state.getFriendshipState() == REQ_RECV) {
                                acceptFriendRequest(user_UID,item_button, friendship_state);
                            }
                            if(friendship_state.getFriendshipState() == FRIENDS){
                                unfriend(user_UID, item_button, friendship_state);
                            }
                        }
                    });
                }
                else{
                    holder.friendRequest.setVisibility(View.INVISIBLE);
                }
            }

        };

        usersRV.setAdapter(adapter);

        return  view;
    }


    public void checkFriendshipStatus(String userID, Button itemButton, AllUsersActivity.UserFriendshipState friendshipState){
        final String user = userID;
        final Button requestButton = itemButton;
        final AllUsersActivity.UserFriendshipState friendship_state = friendshipState;

        friendRequestListRef.child(LoginActivity.current_user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user)){

                            if(dataSnapshot.child(user).child("request_state").getValue().toString().equals("sent")){
                                friendship_state.setFriendshipState(REQ_SENT);
                                requestButton.setText("Cancel request");
                            }
                            if (dataSnapshot.child(user).child("request_state").getValue().toString().equals("received")){
                                friendship_state.setFriendshipState(REQ_RECV);
                                requestButton.setText("Accept request");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        friendListRef.child(LoginActivity.current_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user)){
                    friendship_state.setFriendshipState(FRIENDS);
                    requestButton.setText("Unfriend");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendFriendRequest(String userID, Button itemButton, AllUsersActivity.UserFriendshipState friendshipState){
        final String user = userID;
        final Button requestButton = itemButton;
        final AllUsersActivity.UserFriendshipState friendship_state = friendshipState;

        friendRequestListRef.child(LoginActivity.current_user.getUid())
                .child(user)
                .child("request_state").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            friendRequestListRef.child(user)
                                    .child(LoginActivity.current_user.getUid())
                                    .child("request_state").setValue("received")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show();
                                            friendship_state.setFriendshipState(REQ_SENT);
                                            requestButton.setText("Cancel request");

                                            Map<String, String> notificationData = new HashMap<>();
                                            notificationData.put("sender",LoginActivity.current_user.getUid());
                                            notificationData.put("type","friend_request");

                                            notificationRef.child(user).push().setValue(notificationData);

                                        }
                                    });
                        }
                        else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cancelFriendRequest(String userID, Button itemButton, AllUsersActivity.UserFriendshipState friendshipState){
        final String user = userID;
        final Button requestButton = itemButton;
        final AllUsersActivity.UserFriendshipState friendship_state = friendshipState;

        friendRequestListRef.child(LoginActivity.current_user.getUid())
                .child(user).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            friendRequestListRef.child(user)
                                    .child(LoginActivity.current_user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Request cancelled!", Toast.LENGTH_SHORT).show();
                                            friendship_state.setFriendshipState(NOT_FRIENDS);
                                            requestButton.setText("Add");
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void acceptFriendRequest(String userID, Button itemButton, AllUsersActivity.UserFriendshipState friendshipState){
        final String user = userID;
        final Button requestButton = itemButton;
        final AllUsersActivity.UserFriendshipState friendship_state = friendshipState;
        final String current_date = DateFormat.getDateInstance().format(new Date());

        friendListRef.child(LoginActivity.current_user.getUid())
                .child(user).setValue(current_date)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            friendListRef.child(user)
                                    .child(LoginActivity.current_user.getUid()).setValue(current_date)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            friendRequestListRef.child(LoginActivity.current_user.getUid())
                                                    .child(user).removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            friendRequestListRef.child(user)
                                                                    .child(LoginActivity.current_user.getUid()).removeValue()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            friendship_state.setFriendshipState(FRIENDS);
                                                                            requestButton.setText("Unfriend");
                                                                            Toast.makeText(context, "Request accepted!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void unfriend(String userID, Button itemButton, AllUsersActivity.UserFriendshipState friendshipState){
        final String user = userID;
        final Button requestButton = itemButton;
        final AllUsersActivity.UserFriendshipState friendship_state = friendshipState;

        friendListRef.child(LoginActivity.current_user.getUid())
                .child(user).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            friendListRef.child(user)
                                    .child(LoginActivity.current_user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            friendship_state.setFriendshipState(NOT_FRIENDS);
                                            requestButton.setText("Add");
                                            Toast.makeText(context,"Succsessfully unfriended!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public static class UserRecyclerViewItem extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profilePicture;
        public Button friendRequest;

        public UserRecyclerViewItem(View itemView) {
            super(itemView);
            username = (TextView)itemView.findViewById(R.id.usernameTV);
            profilePicture = (ImageView) itemView.findViewById(R.id.recyclerItemProfilePicture);
            friendRequest = (Button) itemView.findViewById(R.id.sendRequestButton);

        }
    }

    public static class UserFriendshipState{

        private int friendshipState;

        public UserFriendshipState(int state){
            friendshipState = state;
        }
        public int getFriendshipState(){
            return friendshipState;
        }

        public void setFriendshipState(int state){
            this.friendshipState = state;
        }
    }
}
