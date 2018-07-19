package com.mygdx.game;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

/**
 * Created by Mada on 7/10/2018.
 */

public class Notification {
    String sender;
    String type;

    public Notification(){
        super();
    }

    public Notification(String sender, String type){
        this.sender = sender;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}