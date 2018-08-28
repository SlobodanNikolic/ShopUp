package cobe.com.bejbikjum.models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid;
    private String username;
    private String email;
    private String password;
    private String fbid;
    private String fullName;

    public User(String uid, String username, String email, String password, String fbid, String fullName) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
    }

    public User(){
        this.uid = "";
        this.username = "";
        this.email = "";
        this.password = "";
        this.fbid = "";
        this.fullName = "";
    }

    public Map toMap(){
        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);
        user.put("fbid", fbid);
        user.put("fullName", fullName);

        return user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
