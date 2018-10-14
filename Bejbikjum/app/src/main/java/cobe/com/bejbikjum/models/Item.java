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
    private long timestamp;

    private String materialString;

    private String description;
    private int randomNumber;
    private int likes;
    private String comments;
    private String id;
    private String colorString;
    private String shopUid;
    private String shopName;
    private int price;
    private String itemType;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public long getDateRated() {
        return dateRated;
    }

    public void setDateRated(long dateRated) {
        this.dateRated = dateRated;
    }

    public int getTimesViewed() {
        return timesViewed;
    }

    public void setTimesViewed(int timesViewed) {
        this.timesViewed = timesViewed;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    private long dateRated;
    private int timesViewed;
    private String brandName;


    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeStringArray(new String[] {
                this.urlStandard,
                this.name,
                this.timestamp+"",
                this.materialString,
                this.description,
                this.randomNumber+"",
                this.likes + "",
                this.comments,
                this.id,
                this.colorString,
                this.shopUid,
                this.shopName,
                this.price +"",
                this.itemType,
                this.dateRated + "",
                this.timesViewed + "",
                this.brandName
        });
    }

    public Item parseMap(Map<String, Object> map){

        urlStandard = map.get("urlStandard").toString();
        name= map.get("name").toString();
        timestamp= (Long)map.get("timestamp");
        materialString = map.get("materialString").toString();
        description = map.get("description").toString();
        likes = ((Long)map.get("likes")).intValue();
        id = map.get("id").toString();
        colorString = map.get("colorString").toString();
        shopUid = map.get("shopUid").toString();
        shopName = map.get("shopName").toString();
        price = ((Long)map.get("price")).intValue();
        itemType = map.get("itemType").toString();
        randomNumber = ((Long)map.get("randomNumber")).intValue();
        comments = map.get("comments").toString();
        dateRated = ((Long)map.get("dateRated")).intValue();
        timesViewed = ((Long)map.get("timesViewed")).intValue();
        brandName = map.get("brandName").toString();

        return this;
    }


    public Map toMap(){
        Map<String, Object> item = new HashMap<>();
        item.put("urlStandard", urlStandard);
        item.put("name", name);
        item.put("timestamp", timestamp);
        item.put("description", description);
        item.put("randomNumber", randomNumber);
        item.put("likes", likes);
        item.put("comments", comments);
        item.put("id", id);
        item.put("materialString", materialString);
        item.put("colorString", colorString);
        item.put("shopUid", shopUid);
        item.put("shopName", shopName);
        item.put("price", price);
        item.put("itemType", itemType);
        item.put("brandName", brandName);
        item.put("timesViewed", timesViewed);
        item.put("dateRated", dateRated);

        return item;
    }

    public String getMaterialString() {
        return materialString;
    }

    public void setMaterialString(String materialString) {
        this.materialString = materialString;
    }

    @Ignore
    public Item(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
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

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "Item: " + urlStandard+", " +
         name+", " +
         timestamp+", " +
//         material+", " +
         materialString+", " +
//         size+", " +
         description+", " +
         randomNumber+", " +
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

    public Item(String urlStandard, @NonNull String name, long timestamp, String materialString,
                String description, int randomNumber, int likes, String comments, String id,
                String colorString, String shopUid, String shopName, int price, String itemType,
                long dateRated, int timesViewed, String brandName) {

        this.urlStandard = urlStandard;
        this.name = name;
        this.timestamp = timestamp;
        this.materialString = materialString;
        this.description = description;
        this.randomNumber = randomNumber;
        this.likes = likes;
        this.comments = comments;
        this.id = id;
        this.colorString = colorString;
        this.shopUid = shopUid;
        this.shopName = shopName;
        this.price = price;
        this.itemType = itemType;
        this.dateRated = dateRated;
        this.timesViewed = timesViewed;
        this.brandName = brandName;
    }

    public Item(Parcel in){
        String[] data = new String[17];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method

        urlStandard = data[0];
        name= data[1];
        timestamp = Long.parseLong(data[2]);
        materialString = data[3];
        description = data[4];
        randomNumber = Integer.parseInt(data[5]);
        likes   = Integer.parseInt(data[6]);
        comments = data[7];
        id = data[8];
        colorString = data[9];
        shopUid = data[10];
        shopName = data[11];
        price  = Integer.parseInt(data[12]);
        itemType = data[13];
        dateRated   = Long.parseLong(data[14]);
        timesViewed   = Integer.parseInt(data[15]);
        brandName = data[16];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };


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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
