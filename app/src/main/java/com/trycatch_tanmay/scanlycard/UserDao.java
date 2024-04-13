package com.trycatch_tanmay.scanlycard;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface UserDao {
    @Insert
    void Insert(ModelHome modelHome);

    @Query("Select * from ModelHome")
    List<ModelHome> getAllUsers();
}
