package cobe.com.bejbikjum.controlers;

import android.app.Application;
import android.arch.persistence.room.Room;

import cobe.com.bejbikjum.DAO.LocalDB;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;
import cobe.com.bejbikjum.models.User;

public class LocalDBControler {

    private static LocalDBControler instance;
    private static final String DATABASE_NAME = "local_db";
    private LocalDB localDB;

    private LocalDBControler(){
        localDB = Room.databaseBuilder(FirebaseControler.getInstance().getCurrentContext(),
                LocalDB.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    };

    public static LocalDBControler getInstance(){
        if(instance == null){
            instance = new LocalDBControler();
        }
        return instance;
    }

    public void saveItem(final Item item){
        new Thread(new Runnable() {
            @Override
            public void run() {

                localDB.getItemDao().insertItem(item);
            }
        }) .start();
    }

}