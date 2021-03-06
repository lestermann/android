package com.eldarian.translator.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.eldarian.translator.database.AppDataBase;

public class App extends Application {

    public static App instance;
    private AppDataBase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDataBase.class, "appdata.db")
                .build();
    }

    @NonNull
    public static App getInstance() {
        return instance;
    }

    @NonNull
    public AppDataBase getDatabase() {
        return database;
    }

}