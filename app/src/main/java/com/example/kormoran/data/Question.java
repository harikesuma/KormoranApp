package com.example.kormoran.data;

public class Question {

    int id;


    String user_id;

    String user_name;
    String kategori;


    int kategori_id;
    String pertanyaan;


    String pict;
    String created_at;
    String edited;

    int clicked;


    String total_jawaban;

    String user_pict;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getKategori_id() {
        return kategori_id;
    }

    public void setKategori_id(int kategori_id) {
        this.kategori_id = kategori_id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public String getTotal_jawaban() {
        return total_jawaban;
    }

    public void setTotal_jawaban(String total_jawaban) {
        this.total_jawaban = total_jawaban;
    }

    public String getUser_pict() {
        return user_pict;
    }

    public void setUser_pict(String user_pict) {
        this.user_pict = user_pict;
    }

    @Override
    public String toString(){
        return
                "pertanyaanItem{" +
                        ",id = '" + id + '\'' +
                        ",user_id = '" + user_id + '\'' +
                        ",user_pict = '" + user_pict+ '\'' +
                        ",user_name = '" + user_name + '\'' +
                        ",kategori = '" + kategori + '\'' +
                        ",kategori_id = '" + kategori_id + '\'' +
                        ",pertanyaan = '" + pertanyaan + '\'' +
                        ",pict = '" + pict + '\'' +
                        ",edited = '" + edited + '\'' +
                        ",clicked = '" + clicked + '\'' +
                        ",total_jawaban = '" + total_jawaban + '\'' +
                        ",created_at = '" + created_at + '\'' +
                        "}";
    }
}
