package com.example.kormoran.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kormoran.data.Achivement;

import java.util.concurrent.Executors;

@Database(entities = {Achivement.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
        public abstract AchievementDao achievementDao();


        private static RoomDatabase INSTANCE;

        public synchronized static RoomDatabase getInstance(Context context) {
                if (INSTANCE == null) {
                        INSTANCE = buildDatabase(context);
                }
                return INSTANCE;
        }

        private static RoomDatabase  buildDatabase(final Context context) {
                return Room.databaseBuilder(context,
                        RoomDatabase .class,
                        "db_kormoran")
                        .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                        super.onCreate(db);
                                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                getInstance(context).achievementDao().insertAll(Achivement.populateData());
                                                Log.e("DEBUG","SUCCESS");
                                                }
                                        });
                                }
                        })
                        .allowMainThreadQueries()
                        .build();
        }

}
