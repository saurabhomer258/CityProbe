package com.example.saurabhomer.cityprobe;

/**
 * Created by saurabh omer on 06-Oct-18.
 */

public class DataModel {

    String Current_Date_time;
    String Lat;
    String Lang;
    String Date;
    String time;
    String SNo;
    String Pm1;
    String Pm25;
    String Pm10;

    public DataModel(String current_Date_time, String lat, String lang, String date, String time, String SNo, String pm1, String pm25, String pm10, String no2, String co, String humidity) {
        Current_Date_time = current_Date_time;
        Lat = lat;
        Lang = lang;
        Date = date;
        this.time = time;
        this.SNo = SNo;
        Pm1 = pm1;
        Pm25 = pm25;
        Pm10 = pm10;
        No2 = no2;
        Co = co;
        this.humidity = humidity;
    }

    String No2;
    String Co;
    String humidity;

    public String getCurrent_Date_time() {
        return Current_Date_time;
    }

    public void setCurrent_Date_time(String current_Date_time) {
        Current_Date_time = current_Date_time;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSNo() {
        return SNo;
    }

    public void setSNo(String SNo) {
        this.SNo = SNo;
    }

    public String getPm1() {
        return Pm1;
    }

    public void setPm1(String pm1) {
        Pm1 = pm1;
    }

    public String getPm25() {
        return Pm25;
    }

    public void setPm25(String pm25) {
        Pm25 = pm25;
    }

    public String getPm10() {
        return Pm10;
    }

    public void setPm10(String pm10) {
        Pm10 = pm10;
    }

    public String getNo2() {
        return No2;
    }

    public void setNo2(String no2) {
        No2 = no2;
    }

    public String getCo() {
        return Co;
    }

    public void setCo(String co) {
        Co = co;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
