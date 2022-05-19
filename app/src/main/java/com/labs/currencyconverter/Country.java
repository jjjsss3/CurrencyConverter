package com.labs.currencyconverter;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Country implements Serializable {
    public String countryName, countryCode, currencyCode;
    public Country(String name, String code, String currency) {
        countryName = name;
        countryCode = code;
        currencyCode = currency;
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"geonames\":[{\"countryCode\":\"");
        sb.append(countryCode);
        sb.append("\",\"countryName\":\"");
        sb.append(countryName);
        sb.append("\",\"currencyCode\":\"");
        sb.append(currencyCode);
        sb.append("\"}]}");
        return sb.toString();
    }

    public String get_URL_FLAG_GIF() {
//        https://www.worldometers.info/img/flags/af-flag.gif
//        return "https://img.geonames.org/flags/x/"+countryCode.toLowerCase()+".gif";
        return "https://www.worldatlas.com/img/flag/"+countryCode.toLowerCase()+"-flag.jpg";

    }
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }

}
