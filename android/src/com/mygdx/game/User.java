package com.mygdx.game;

/**
 * Created by Mada on 4/3/2018.
 */

public class User {
    private String username;
    private String email;
    private String password;
    private String nativeLanguage;
    private String studyingLanguage;


    public User() {
    }

    public User(String username, String email,  String password, String nativeLanguage, String studyingLanguage) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nativeLanguage = nativeLanguage;
        this.studyingLanguage = studyingLanguage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getStudyingLanguage() {
        return studyingLanguage;
    }

    public void setStudyingLanguage(String studyingLanguage) {
        this.studyingLanguage = studyingLanguage;
    }
}
