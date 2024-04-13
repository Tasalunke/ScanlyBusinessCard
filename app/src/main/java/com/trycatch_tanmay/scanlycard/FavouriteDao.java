package com.trycatch_tanmay.scanlycard;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface FavouriteDao {
    @Insert
    void Insert(ModelHome modelHome);
    @Query("DELETE FROM ModelHome WHERE id = :id")
    void delete(double id);
    @Query("Select * from ModelHome")
    List<ModelHome> getAllUsers();
}
