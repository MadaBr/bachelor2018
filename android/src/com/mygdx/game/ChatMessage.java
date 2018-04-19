package com.mygdx.game;

/**
 * Created by Mada on 3/24/2018.
 */

public class ChatMessage {
    private String message, participant;

    public ChatMessage(String message, String participant) {
        this.message = message;
        this.participant = participant;
    }

    public ChatMessage(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }
}
