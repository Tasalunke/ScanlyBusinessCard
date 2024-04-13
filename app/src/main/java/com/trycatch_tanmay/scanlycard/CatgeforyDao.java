package com.trycatch_tanmay.scanlycard;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface CatgeforyDao {
    @Insert
    void  Insert(ModelHome modelHome);

    @Query("Select * from ModelHome")
    List<ModelHome> getAllUser();
    @Query("SELECT COUNT(*) from ModelHome WHERE id = :itemId")
    int exists(String itemId);
}
