package cobe.com.bejbikjum.controlers;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

public class FacebookControler {
    private static FacebookControler instance;
    private AccessToken token;

    private FacebookControler(){};

    public static FacebookControler getInstance(){
        if(instance == null){
            instance = new FacebookControler();
        }
        return instance;
    }

    public void setToken(AccessToken token){
        this.token = token;
    }

    public AccessToken getToken() {
        return token;
    }
}
