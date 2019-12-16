package com.example.kormoran.data;

public class Kategori {

    int id;
    String kategori;
    String pict;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    @Override
    public String toString(){
        return
                "kategoriItem{" +
                        ",id = '" + id + '\'' +
                        ",kategori = '" + kategori + '\'' +
                        ",pict = '" + pict + '\'' +
                        "}";
    }
}
