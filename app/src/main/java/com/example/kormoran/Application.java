package com.example.kormoran;

import android.content.Context;

import androidx.room.Room;

import com.example.kormoran.data.Achivement;
import com.example.kormoran.room.RoomDatabase;
import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {
    private static Context mContext;
    public static com.example.kormoran.room.RoomDatabase roomDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
//            roomDatabase = RoomDatabase.getInstance(mContext);
    }

    public static Context getContext(){
        return mContext;
    }
}
