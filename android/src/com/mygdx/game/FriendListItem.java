package com.mygdx.game;

/**
 * Created by Mada on 4/22/2018.
 */

public class FriendListItem {
    private String userUID;

    public FriendListItem(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

}
