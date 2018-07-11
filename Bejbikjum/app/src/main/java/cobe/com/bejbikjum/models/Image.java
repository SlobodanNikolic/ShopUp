package cobe.com.bejbikjum.models;

public class Image {

    private String urlSmall;
    private String urlMedium;
    private String urlLarge;
    private String name;
    private String timestamp;

    public String getMedium(){
        return urlMedium;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSmall(String small) {
        this.urlSmall = small;
    }

    public void setMedium(String medium) {
        this.urlMedium = medium;
    }

    public void setLarge(String large) {
        this.urlLarge = large;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
