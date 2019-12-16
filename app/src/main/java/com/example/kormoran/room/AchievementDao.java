package com.example.kormoran.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kormoran.data.Achivement;

import java.util.List;

@Dao
public interface AchievementDao {

    @Insert
    void insertAll(Achivement... achivements);

    @Query("SELECT * from tb_achievement where target <=:like ORDER BY target DESC")
    List<Achivement> getAchievment(int like);

    @Query("SELECT nama from tb_achievement where target >:like LIMIT 1")
    String getNextAchievement(int like);

}
