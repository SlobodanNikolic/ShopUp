package cobe.com.bejbikjum.controlers;

import cobe.com.bejbikjum.models.User;

public class AppControler {

    private static AppControler instance;
    private User currentUser;
    public Boolean isSeller;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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