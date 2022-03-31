package com.example.weather.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static boolean handleProvinceRequest(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject object=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(object.getString("name"));
                    province.setProvinceCode(object.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityRequest(String response,int provinceCode){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject object=allProvince.getJSONObject(i);
                    City city=new City();
                    city.setCityName(object.getString("name"));
                    city.setCityCode(object.getInt("id"));
                    city.setProvinceCode(provinceCode);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyRequest(String response,int cityCode){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject object=allProvince.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(object.getString("name"));
                    county.setWeatherId(object.getString("weather_id"));
                    county.setCityCode(cityCode);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
