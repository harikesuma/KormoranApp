package com.example.kormoran.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_achievement")
public class Achivement {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "nama")
    String nama;

    @ColumnInfo(name = "deskripsi")
    String deskripsi;

    @ColumnInfo(name = "target")
    int target;

    public Achivement(String nama, String deskripsi, int target) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }


    public static Achivement[] populateData() {
        return new Achivement[]{
                new Achivement("Starter", "Pertama kali bergabung dengan Kormoran",0),
                new Achivement("Beginner", "Telah mendapatkan 10 like",10),
                new Achivement("Bronze", "Telah mendapatkan 25 like",25),
                new Achivement("Silver", "Telah mendapatkan 50 like",50),
                new Achivement("Gold", "Telah mendapatkan 100 like",100),
                new Achivement("Platinum", "Telah mendapatkan 150 like",150),
                new Achivement("Elite", "Telah mendapatkan 200 like",200),
                new Achivement("Expert", "Telah mendapatkan 300 like",300),
                new Achivement("Mystical", "Telah mendapatkan 500 like",500),
                new Achivement("Unquestionable", "Telah mendapatkan 1000 like",1000)

        };
    }


    }

