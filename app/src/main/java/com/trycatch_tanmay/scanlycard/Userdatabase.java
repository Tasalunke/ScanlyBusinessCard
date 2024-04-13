package com.trycatch_tanmay.scanlycard;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {ModelHome.class}, version = 1)
public abstract class Userdatabase extends RoomDatabase {
public abstract UserDao getmodel();
    public static Userdatabase INSTANCE;
    public static Userdatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, Userdatabase.class, "userDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
//singleton classes
// U can make multiple database class to save data in android