package cobe.com.bejbikjum.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cobe.com.bejbikjum.helpers.StringListConverter;

@Entity
public class Item {

    private String urlStandard;
    @NonNull @PrimaryKey
    private String name;
    private String timestamp;
    @TypeConverters(StringListConverter.class)
    private StringList material;
    private String materialString;
    @TypeConverters(StringListConverter.class)
    private StringList size;
    private String description;
    private float rating;
    private int timesRated;
    private int likes;
    @TypeConverters(StringListConverter.class)
    private StringList comments;
    private String id;
    private String colorString;
    private String shopUid;
    private String shopName;
    private String price;
    private String itemType;

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    public String getUrlStandard() {
        return urlStandard;
    }

    public void setUrlStandard(String urlStandard) {
        this.urlStandard = urlStandard;
    }

    public String getShopUid() {
        return shopUid;
    }

    public void setShopUid(String shopUid) {
        this.shopUid = shopUid;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Item(String urlStandard, String name, String timestamp, StringList material, StringList size,
                String description, float rating, int timesRated, int likes, StringList comments, String id, String materialString, String colorString,
                String shopUid, String shopName, String price, String itemType) {
        this.urlStandard = urlStandard;
        this.name = name;
        this.timestamp = timestamp;
        this.material = material;
        this.size = size;
        this.description = description;
        this.rating = rating;
        this.timesRated = timesRated;
        this.likes = likes;
        this.comments = comments;
        this.id = id;
        this.materialString = materialString;
        this.colorString = colorString;
        this.shopUid = shopUid;
        this.shopName = shopName;
        this.price = price;
        this.itemType = itemType;
    }

    public Map toMap(){
        Map<String, Object> item = new HashMap<>();
        item.put("urlStandard", urlStandard);
        item.put("name", name);
        item.put("timestamp", timestamp);
        item.put("material", material.toString());
        item.put("size", size.toString());
        item.put("description", description);
        item.put("rating", rating);
        item.put("timesRated", timesRated);
        item.put("likes", likes);
        item.put("comments", comments);
        item.put("id", id);
        item.put("materialString", materialString);
        item.put("colorString", colorString);
        item.put("shopUid", shopUid);
        item.put("shopName", shopName);
        item.put("price", price);

        return item;
    }

    public String getMaterialString() {
        return materialString;
    }

    public void setMaterialString(String materialString) {
        this.materialString = materialString;
    }

    public StringList getSize() {
        return size;
    }

    public void setSize(StringList size) {
        this.size = size;
    }

    @Ignore
    public Item(){

    }

    @Ignore
    public Item(String urlStandard, String name, StringList material, StringList size) {
        this.urlStandard = urlStandard;
        this.name = name;
        this.material = material;
        this.size = size;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getTimesRated() {
        return timesRated;
    }

    public void setTimesRated(int timesRated) {
        this.timesRated = timesRated;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public StringList getComments() {
        return comments;
    }

    public void setComments(StringList comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStandard(){
        return urlStandard;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setMedium(String medium) {
        this.urlStandard = medium;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public StringList getMaterial() {
        return material;
    }

    public void setMaterial(StringList material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }


}
