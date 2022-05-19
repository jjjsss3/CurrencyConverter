package com.labs.currencyconverter;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class ServiceCaller {
    public final static String GET = "GET";
    public final static String POST = "POST";

    public String call(String url, String method, ContentValues params) {
        InputStream in = null;
        BufferedReader br= null;
        StringBuilder sbService = new StringBuilder();
        String linkService = url;
        try {
            if (params.size()>0) {
                sbService.append(url); sbService.append("?");
                Set<Map.Entry<String, Object>> valueSet = params.valueSet();
                for (Map.Entry<String, Object> entry : valueSet) {
                    String columnName = entry.getKey();
                    sbService.append(entry.getKey().toString());
                    sbService.append("=");
                    sbService.append(entry.getValue().toString());
                    sbService.append("&");
                }
                sbService.deleteCharAt(sbService.lastIndexOf("&"));
                linkService = sbService.toString();
            }
            URL urlConn = new URL(linkService);
            HttpURLConnection httpConn = (HttpURLConnection)urlConn.openConnection();
            httpConn.setRequestMethod(method);
            httpConn.connect();
            int resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                br= new BufferedReader(new InputStreamReader(in, "UTF-8"));

                StringBuilder sb= new StringBuilder();
                String s= null;
                while((s= br.readLine())!= null) {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                Country country = new Country(httpConn.getResponseMessage(), "HTTP", Integer.toString(resCode));
                return country.toJSON();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("My error", e.toString());
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(in);
        }
        return null;
    }
}
