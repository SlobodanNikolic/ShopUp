package cobe.com.bejbikjum.DAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;

@Database(entities = {Seller.class, Item.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
public abstract class LocalDB extends RoomDatabase {

    public abstract SellerDAO getSellerDao();
    public abstract ItemDAO getItemDao();

    public void saveItem(final Item item) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                getItemDao().insertItem(item);
            }
        }).start();
    }

}


