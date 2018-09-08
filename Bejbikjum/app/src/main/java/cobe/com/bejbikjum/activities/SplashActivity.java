package cobe.com.bejbikjum.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.controlers.FirebaseControler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseControler.getInstance().setContext(this, this);

        final Timer timer = new Timer();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {

              @Override
              public void run() {
                if(checkConnection()){
                    timer.cancel();
                    //Nastavi dalje
                    checkLoginStatus();
                }
              }
          },0, 10*1000);

    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkConnection(){
        if(isOnline()){
            return true;
        }else{
            Toast.makeText(SplashActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void checkLoginStatus(){
        FirebaseControler.getInstance().checkCurrentUser();
    }


}
