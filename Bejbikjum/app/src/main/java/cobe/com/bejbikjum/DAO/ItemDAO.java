package cobe.com.bejbikjum.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;

@Dao
public interface ItemDAO {

    // Adds a person to the database
    @Insert
    void insertAll(Item... items);

    // Removes a person from the database
    @Delete
    void delete(Item item);

    // Gets all people in the database
    @Query("SELECT * FROM item")
    List<Item> getAllItems();

    // Gets all people in the database with a favorite color
    @Query("SELECT * FROM item WHERE name LIKE :itemName")
    List<Item> getAllItemsByName(String itemName);

    @Insert
    void insertItem (Item item);

}
