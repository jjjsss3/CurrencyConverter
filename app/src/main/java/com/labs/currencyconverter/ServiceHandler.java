package com.labs.currencyconverter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceHandler extends AsyncTask<String,Void,ArrayList<Country>> {
    // URL
    String URL_GETCOUNTRIES = "http://api.geonames.org/countryInfoJSON";
    ContentValues callParams;

    ProgressDialog pDialog;

    static final String DISPLAY = "display";
    static final String CREATE = "create";
    static final String UPDATE = "update";
    static final String DELETE = "delete";

    ServiceCaller serviceCaller = new ServiceCaller();

    Context context;
    ArrayList<Country> countries;

    public ServiceHandler(Context context, ArrayList<Country> list, ContentValues params){
        this.context = context;
        countries = list;
        callParams = params;
    }

    @Override
    protected ArrayList<Country> doInBackground(String... params) {
        switch (params[0]) {
            case CREATE:
                countries = new ArrayList<>();
                String json = serviceCaller.call(URL_GETCOUNTRIES, ServiceCaller.GET, callParams);
                if (json != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        if (jsonObj != null) {
                            JSONArray accounts = jsonObj.getJSONArray("geonames");
                            for (int i = 0; i < accounts.length(); i++) {
                                JSONObject obj = (JSONObject) accounts.get(i);
                                if(!obj.getString("currencyCode").isEmpty()) {
                                    Country country = new Country(obj.getString("countryName"),
                                            obj.getString("countryCode"),
                                            obj.getString("currencyCode"));
                                    countries.add(country);
                                }
                            }
                        }
                        else {
                            Log.d("JSON Data", "JSON data's format is incorrect!");
                            Country country = new Country("JSON data's format is incorrect!",
                                    "JSON Data", "0");
                            countries.add(country);
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                } else {
                    Log.d("JSON Data", "Didn't receive any data from server!");
                    Country country = new Country("Didn't receive any data from server!",
                            "JSON Data", "0");
                    countries.add(country);
                }
                break;
            case DISPLAY: break;
            case UPDATE: break;
            case DELETE: break;
        }
        return countries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Proccesing..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Country> ret) {
        super.onPostExecute(ret);
        if (pDialog.isShowing())
            pDialog.dismiss();
        if (ret == null)
            Toast.makeText(context, "Lỗi - Refresh lại", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Country> getData() {
        return countries;
    }
}

