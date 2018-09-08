package cobe.com.bejbikjum.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seller {

    private String uid;
    private String shopName;
    private String email;
    private String password;
    private String fbid;
    private String fullName;
    private String phoneNum;
    private ArrayList<String> itemTypes;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = new ArrayList<String>();
    }

    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum, String itemTypes) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = new ArrayList<String>(Arrays.asList(itemTypes.split("\\s*,\\s*")));
    }

    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum, ArrayList<String> itemTypes) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = itemTypes;
    }

    public Seller(){
        this.uid = "";
        this.shopName = "";
        this.email = "";
        this.password = "";
        this.fbid = "";
        this.fullName = "";
        this.phoneNum = "";
        this.itemTypes = new ArrayList<String>();

    }

    public ArrayList<String> getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(ArrayList<String> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public void addItemType(String itemTypeName){
        itemTypes.add(itemTypeName);
    }

    public void removeItemType(String itemTypeName){
        itemTypes.remove(itemTypeName);
    }

    public Map toMap(){
        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("shopName", shopName);
        user.put("email", email);
        user.put("password", password);
        user.put("fbid", fbid);
        user.put("fullName", fullName);
        user.put("phoneNum", phoneNum);
        user.put("itemTypes", itemTypes.toString());

        return user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String username) {
        this.shopName = username;
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
