package cobe.com.bejbikjum.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cobe.com.bejbikjum.models.Seller;

@Dao
public interface SellerDAO {

    // Adds a person to the database
    @Insert
    void insertAll(Seller... sellers);

    // Removes a person from the database
    @Delete
    void delete(Seller seller);

    // Gets all people in the database
    @Query("SELECT * FROM seller")
    List<Seller> getAllPeople();

    // Gets all people in the database with a favorite color
    @Query("SELECT * FROM seller WHERE shopName LIKE :shopName")
    List<Seller> getAllPeopleWithFavoriteColor(String shopName);

}
