package cobe.com.bejbikjum.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cobe.com.bejbikjum.helpers.StringListConverter;

@Entity
public class Seller {

    @NonNull
    @PrimaryKey private String uid;
    private String shopName;
    private String email;
    private String password;
    private String fbid;
    private String fullName;
    private String phoneNum;
    @TypeConverters(StringListConverter.class)
    private StringList itemTypes;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Ignore
    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = new StringList();
    }

    @Ignore
    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum, String itemTypes) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = new StringList(itemTypes);
    }


    public Seller(String uid, String shopName, String email, String password, String fbid, String fullName, String phoneNum, StringList itemTypes) {
        this.uid = uid;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
        this.fbid = fbid;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.itemTypes = itemTypes;
    }

    @Ignore
    public Seller(){
        this.uid = "";
        this.shopName = "";
        this.email = "";
        this.password = "";
        this.fbid = "";
        this.fullName = "";
        this.phoneNum = "";
        this.itemTypes = new StringList();

    }

    public StringList getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(StringList itemTypes) {
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
