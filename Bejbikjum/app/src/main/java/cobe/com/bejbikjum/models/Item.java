package cobe.com.bejbikjum.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cobe.com.bejbikjum.helpers.StringListConverter;

@Entity
public class Item implements Parcelable {

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

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeStringArray(new String[] {this.urlStandard,
                this.name,
                this.timestamp,
                this.materialString,
                this.description, ""+this.rating, ""+this.timesRated, ""+this.likes, this.id,
                this.colorString, this.shopUid, this.shopName, this.price, this.itemType
        });
    }

    public Item parseMap(Map<String, Object> map){

        urlStandard = map.get("urlStandard").toString();
        name= map.get("name").toString();
        timestamp= map.get("timestamp").toString();
        materialString = map.get("materialString").toString();
        description = map.get("description").toString();
        timesRated = ((Long)map.get("timesRated")).intValue();
        likes = ((Long)map.get("likes")).intValue();
        id = map.get("id").toString();
        colorString = map.get("colorString").toString();
        shopUid = map.get("shopUid").toString();
        shopName = map.get("shopName").toString();
        price = map.get("price").toString();
        itemType = map.get("itemType").toString();

        return this;
    }

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

        if(material != null)
            item.put("material", material.toString());
        else
            item.put("material", "");

        if(size!=null)
            item.put("size", size.toString());
        else
            item.put("size", "");

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

    @Override
    public String toString(){
        return "Item: " + urlStandard+", " +
         name+", " +
         timestamp+", " +
         material+", " +
         materialString+", " +
         size+", " +
         description+", " +
         rating+", " +
         timesRated+", " +
         likes+", " +
         comments+", " +
         id+", " +
         colorString+", " +
         shopUid+", " +
         shopName+", " +
         price+", " +
         itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Item(Parcel in){
        String[] data = new String[14];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        urlStandard = data[0];
        name = data[1];
        timestamp = data[2];
        materialString = data[3];
        description = data[4];
        rating = Float.parseFloat(data[5]);
        timesRated = Integer.parseInt(data[6]);
        likes = Integer.parseInt(data[7]);
        id = data[8];
        colorString = data[9];
        shopUid = data[10];
        shopName = data[11];
        price = data[12];
        itemType = data[13];
        material = new StringList();
        size = new StringList();
        comments = new StringList();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
