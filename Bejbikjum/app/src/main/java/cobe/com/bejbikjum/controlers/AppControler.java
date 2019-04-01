package cobe.com.bejbikjum.controlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;
import cobe.com.bejbikjum.models.User;

public class AppControler {

    private static AppControler instance;
    private User currentUser;
    private Seller currentSeller;
    public Boolean isSeller;
    private List<Item> shopItems;
    private File galleryFolder;

    public File getGalleryFolder() {
        return galleryFolder;
    }

    public void setGalleryFolder(File galleryFolder) {
        this.galleryFolder = galleryFolder;
    }

    private ArrayList<Item> topRatedItems;

    public List<Item> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<Item> shopItems) {
        this.shopItems = shopItems;
    }

    private String[] data = {"Women's shoes", "Men's shoes", "Sneakers", "Handbags", "Dresses", "Sports", "Men's jeans", "Women's jeans",
            "Men's suits", "Men's underwear", "Lingerie", "Skirts", "Men's hats", "Sunglasses", "Accessories"};
    private int[] iconIds = {R.drawable.high_heels, R.drawable.shoes, R.drawable.running, R.drawable.handbag, R.drawable.dress,
            R.drawable.sport, R.drawable.jeansm, R.drawable.jeansw, R.drawable.suit, R.drawable.boxer, R.drawable.underwear,
            R.drawable.skirt, R.drawable.hatm, R.drawable.sunglasses, R.drawable.jewel};

    public User getCurrentUser() {
        return currentUser;
    }

    public Seller getCurrentSeller() {
        if(currentSeller == null){
            currentSeller = new Seller();
        }
        return currentSeller;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public int[] getIconIds() {
        return iconIds;
    }

    public void setIconIds(int[] iconIds) {
        this.iconIds = iconIds;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void setCurrentSeller(Seller currentSeller) {
        this.currentSeller = currentSeller;
    }


    public Boolean getSeller() {
        return isSeller;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }

    private AppControler(){
        isSeller = false;
        currentUser = new User();
        topRatedItems = new ArrayList<Item>();
    };

    public void clearTopRated(){
        topRatedItems.clear();
    }

    public void addToTopRated(Item item){
        topRatedItems.add(item);
    }

    public static AppControler getInstance(){
        if(instance == null){
            instance = new AppControler();
        }
        return instance;
    }


    public ArrayList<Item> getTopRatedItems() {
        return topRatedItems;
    }

    public void setTopRatedItems(ArrayList<Item> topRatedItems) {
        this.topRatedItems = topRatedItems;
    }

}