package cobe.com.bejbikjum.models;

enum Material
{
    SILK, COTTON, SUEDE, ACRYLIC;
}

enum Size{
    XS, S, M, L, XL, XXL
}

public class Item {

    private String urlSmall;
    private String urlStandard;
    private String name;
    private String timestamp;
    private Material material;
    private Size size;
    private String description;
    private float rating;
    private int timesRated;
    private int likes;
    private String[] comments;
    private long id;

    public Item(String urlStandard, String name, String timestamp, Material material, Size size,
                 String description, float rating, int timesRated, int likes, String[] comments, long id) {
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
    }

    public Item(){

    }

    public Item(String urlStandard, String name, Material material, Size size) {
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

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStandard(){
        return urlStandard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSmall(String small) {
        this.urlSmall = small;
    }

    public void setMedium(String medium) {
        this.urlStandard = medium;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }


}
