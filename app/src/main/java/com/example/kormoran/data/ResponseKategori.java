package com.example.kormoran.data;

import java.util.List;

public class ResponseKategori {

    String msg;
    List<Kategori> kategoriList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Kategori> getKategoriList() {
        return kategoriList;
    }

    public void setKategoriList(List<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
    }


    @Override
    public String toString(){
        return
                "ResponseKategori{" +
                        "kategoriList = '" + kategoriList + '\'' +
                        ",msg = '" + msg + '\'' +
                        "}";
    }
}
