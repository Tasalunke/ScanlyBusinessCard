package com.trycatch_tanmay.scanlycard;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {ModelHome.class},version = 1)
public  abstract class Categorydatabase extends RoomDatabase {

    public abstract  CatgeforyDao getCatDao();
    public static  Categorydatabase INSTANCE;

    public static  Categorydatabase  getINSTANCE(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context ,Categorydatabase.class,"categoryDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
