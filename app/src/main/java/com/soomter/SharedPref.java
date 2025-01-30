package com.soomter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.soomter.utils.ListInterface;

public class SharedPref {

    private Context context;
    public static final String USER = "user";
    public static final String LANG = "lang";
    public static final String CITIES = "cities";
    public static final String PORTAL_CATG = "portal_catg";
    public static final String PRODUCTS_CATG = "products_catg";
    public static final String BUSINESS_CATG = "business_catg";
    public static final String ALL_CITIES = "all_cities";
    public static final String ALL_TRADS = "all_trads";
    public static final String ALL_BUSINESS = "all_business_catgs";
    public static final String COUNTRY_ID = "country_id";

    public static final String PROD_SUBCATGS = "products_subcatgs";
    public static final String PROD_TRADEMARKS = "products_tradmark";
    public static final String PROD_CATGS_TREE = "catgs_tree";
    private SharedPreferences sharedPref;
    Gson gson;

    public SharedPref(Context context){
        this.context = context;
         gson = new Gson();
        this.sharedPref = context.getSharedPreferences("PREF",Context.MODE_PRIVATE);
    }

    public void saveUSer(UserData.Datum user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.commit();
    }

    public void saveLanguage(String lang) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LANG, lang);
        editor.commit();
    }

    public void saveAllCitiesStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ALL_CITIES, status);
        editor.commit();
    }

    public void saveAllTradsStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ALL_TRADS, status);
        editor.commit();
    }

    public boolean getAllCitiesStatus() {
        return sharedPref.getBoolean(ALL_CITIES, false);
    }

    public boolean getAllTradsStatus() {
        return sharedPref.getBoolean(ALL_TRADS, false);
    }

    public void saveAllBussCatgsStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ALL_BUSINESS, status);
        editor.commit();
    }

    public boolean getAllBussCatgsStatus() {
        return sharedPref.getBoolean(ALL_BUSINESS, false);
    }

    public void saveCities(List<CitiesData.Datum> cities) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(cities);
        editor.putString(CITIES, json);
        editor.commit();
    }

    public void saveTradeMarks(List<ProductTradeMarksData.Datum> cities) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(cities);
        editor.putString(PROD_TRADEMARKS, json);
        editor.commit();
    }

    public void saveProductSubCatgs(List<ProductSubCatgsData.Datum> cities) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(cities);
        editor.putString(PROD_SUBCATGS, json);
        editor.commit();
    }

    public void saveProductCatgsTree(List<ProductCatgsTreeData.Datum> data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(data);
        editor.putString(PROD_CATGS_TREE, json);
        editor.commit();
    }

    public void saveBusinessCatg(List<BussinesCatgData.Datum> business) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(business);
        editor.putString(BUSINESS_CATG, json);
        editor.commit();
    }

    public void saveSubCatgId(int coutntrID){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(COUNTRY_ID, coutntrID);
        editor.commit();
    }

    public int getSubCatgID(){
        return sharedPref.getInt(COUNTRY_ID, 0);
    }

    public String getLang(){
        return sharedPref.getString(LANG, "");
    }

    public void savePortalCatg(List<PortalCatgData.Datum> cities) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(cities);
        editor.putString(PORTAL_CATG, json);
        editor.commit();
    }

    public void saveProductsCatg(List<ProductsCatgData.Datum> cities) {
        SharedPreferences.Editor editor = sharedPref.edit();
        String json = gson.toJson(cities);
        editor.putString(PRODUCTS_CATG, json);
        editor.commit();
    }

    public UserData.Datum getUser(){
        String json = sharedPref.getString(USER, "");
        return gson.fromJson(json, UserData.Datum.class);
    }

    public List<CitiesData.Datum> getCities(){
        String json = sharedPref.getString(CITIES, "");
        Type listType = new TypeToken<ArrayList<CitiesData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<ProductCatgsTreeData.Datum> getTree(){
        String json = sharedPref.getString(PROD_CATGS_TREE, "");
        Type listType = new TypeToken<ArrayList<ProductCatgsTreeData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<BussinesCatgData.Datum> getBusinessCatg(){
        String json = sharedPref.getString(BUSINESS_CATG, "");
        Type listType = new TypeToken<ArrayList<BussinesCatgData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<PortalCatgData.Datum> getPortalCatg(){
        String json = sharedPref.getString(PORTAL_CATG, "");
        Type listType = new TypeToken<ArrayList<PortalCatgData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<ProductsCatgData.Datum> getProductsCatg(){
        String json = sharedPref.getString(PRODUCTS_CATG, "");
        Type listType = new TypeToken<ArrayList<ProductsCatgData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<ProductSubCatgsData.Datum> getProducSubCatgs(){
        String json = sharedPref.getString(PROD_SUBCATGS, "");
        Type listType = new TypeToken<ArrayList<ProductSubCatgsData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public List<ProductTradeMarksData.Datum> getProductTradeMarks(){
        String json = sharedPref.getString(PROD_TRADEMARKS, "");
        Type listType = new TypeToken<ArrayList<ProductTradeMarksData.Datum>>(){}.getType();
        return gson.fromJson(json, listType);
    }

}
