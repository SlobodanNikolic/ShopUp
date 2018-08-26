package cobe.com.bejbikjum.controlers;

import cobe.com.bejbikjum.models.User;

public class AppControler {

    private static AppControler instance;
    private User currentUser;
    public Boolean isSeller;

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