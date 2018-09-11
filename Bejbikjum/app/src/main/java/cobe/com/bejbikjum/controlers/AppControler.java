package cobe.com.bejbikjum.controlers;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Seller;
import cobe.com.bejbikjum.models.User;

public class AppControler {

    private static AppControler instance;
    private User currentUser;
    private Seller currentSeller;
    public Boolean isSeller;


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

    };

    public static AppControler getInstance(){
        if(instance == null){
            instance = new AppControler();
        }
        return instance;
    }

}