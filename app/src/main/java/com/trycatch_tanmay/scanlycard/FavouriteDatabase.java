package com.trycatch_tanmay.scanlycard;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {ModelHome.class}, version = 1)
public abstract class  FavouriteDatabase extends RoomDatabase {
    public  abstract  FavouriteDao getfavouriteDao();
    public static FavouriteDatabase INSTANCE;
    public static synchronized FavouriteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavouriteDatabase.class, "favourite_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
