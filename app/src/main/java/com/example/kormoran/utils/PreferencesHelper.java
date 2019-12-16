package com.example.kormoran.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {


//    public static final String SP_NAMA = "spNama";
//    public static final String SP_EMAIL = "spEmail";
//    public static  final String SP_TOKEN = "spToken";

    public static  final String SP_ID = "spId";
    public static final String SP_USERNAME = "spUserName";
    public static final String SP_NAME = "spName";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_TOKEN= "spToken";
    public static final String SP_PICT = "spPict";
    public static final String SP_PICT_PATH_INTERNAL = "sp_pict_path_internal";
    public static final String SP_KORMORAN_APP = "spKormoranApp";


    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public PreferencesHelper(Context context){
        sp = context.getSharedPreferences(SP_KORMORAN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpName() {
        return sp.getString(SP_NAME,"");
    }

    public String getSpEmail() {
        return sp.getString(SP_EMAIL,"") ;
    }

    public String getSpUserName(){

        return sp.getString(SP_USERNAME, "");
    }

    public String getSpToken(){

        return sp.getString(SP_TOKEN, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public String getSpId() {
        return sp.getString(SP_ID,"");
    }

    public String getSpPict() {
        return sp.getString(SP_PICT,"");
    }

    public String getSpPictPathInternal() {
        return sp.getString(SP_PICT_PATH_INTERNAL,"");
    }

    public void deleteSharedPreferenced(){
        spEditor
                .clear()
                .commit();
    }
}
