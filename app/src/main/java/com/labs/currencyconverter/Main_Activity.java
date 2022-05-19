package com.labs.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Main_Activity extends AppCompatActivity {
    TextInputLayout textInputLayout1, textInputLayout2;
    TextInputEditText text_amount1,text_amount2;
    TextView text_currency_code1, text_currency_code2, text_rate_info1, text_rate_info2, text_result1, text_result2,text_time;
    Button btn_retype, btn_reset;
    ImageView image_flag1,image_flag2;
    String str_currency_code1 = "";
    String str_currency_code2 = "";
    ArrayList<Double> listRate;
    ArrayList<Country> listCountries;
    ServiceHandler serviceHandler;
    static final int CURRENCY_CODE_CONVERT=1;
    static final int CURRENCY_CODE_CONVERT_TO=2;
    int CURRENCY_EXCHANGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);
        while(!isNetworkAvailable()){
            if (!Utility.hasSendToast(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Please connect Wifi or LTE", Toast.LENGTH_LONG)
                        .show();
                Utility.setSendToast(getApplicationContext(), true);
            }
        }
            initview();
            getAllCountries();
            setAllListener();
            setAll();
    }

    private void setAllListener() {
        ((LinearLayout)findViewById(R.id.layout_currency1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, SelectCurrencyActivity.class);
                intent.putExtra("listCountries", listCountries);
                startActivityForResult(intent,CURRENCY_CODE_CONVERT);
            }
        });
        ((LinearLayout)findViewById(R.id.layout_currency2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, SelectCurrencyActivity.class);
                intent.putExtra("listCountries", listCountries);
                startActivityForResult(intent,CURRENCY_CODE_CONVERT_TO);
            }
        });
        text_amount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_amount1.setSelection(text_amount1.getText().length());
                text_amount1.moveCursorToVisibleOffset();
                CURRENCY_EXCHANGE=1;
            }
        });
        text_amount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_amount2.setSelection(text_amount2.getText().length());
                text_amount2.moveCursorToVisibleOffset();
                CURRENCY_EXCHANGE=2;
            }
        });
        btn_retype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CURRENCY_EXCHANGE==1){
                    text_amount1.setText("1");
                    text_amount1.setSelection(text_amount1.getText().length());
                    text_amount1.moveCursorToVisibleOffset();
                }
                else if(CURRENCY_EXCHANGE==2) {
                    text_amount2.setText("1");
                    text_amount2.setSelection(text_amount2.getText().length());
                    text_amount2.moveCursorToVisibleOffset();
                }
                setAll();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAll();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String countryName = data.getStringExtra("countryName");
        String countryCode = data.getStringExtra("countryCode");
        String currencyCode = data.getStringExtra("currencyCode").toUpperCase();
        Country country=new Country(countryName,countryCode,currencyCode);
        switch(requestCode) {
            case (CURRENCY_CODE_CONVERT) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    str_currency_code1=currencyCode;
                    if(str_currency_code1.toLowerCase().equals(str_currency_code2.toLowerCase())){
                        Toast.makeText(getApplicationContext(), "Please choose another currency", Toast.LENGTH_LONG).show();
                    }else {
                        text_currency_code1.setText(currencyCode.toUpperCase());
                        Glide.with(getApplicationContext()).load(country.get_URL_FLAG_GIF()).placeholder(R.drawable.progress_animation).into(image_flag1);
                        CURRENCY_EXCHANGE=1;
                        setAll();
                    }
                }
                break;
            }
            case (CURRENCY_CODE_CONVERT_TO) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    str_currency_code2=currencyCode;
                    if(str_currency_code1.toLowerCase().equals(str_currency_code2.toLowerCase())){
                        Toast.makeText(getApplicationContext(), "Please choose another currency", Toast.LENGTH_LONG).show();
                    }else {
                        text_currency_code2.setText(currencyCode.toUpperCase());
                        Glide.with(getApplicationContext()).load(country.get_URL_FLAG_GIF()).placeholder(R.drawable.progress_animation).into(image_flag2);
                        CURRENCY_EXCHANGE=2;
                        setAll();
                    }
                }
                break;
            }
        }

    }

    private void initview() {
        text_amount1 =findViewById(R.id.amount1);
        text_amount2 =findViewById(R.id.amount2);
        textInputLayout1=findViewById(R.id.textField1);
        textInputLayout2=findViewById(R.id.textField2);
        text_result1 =findViewById(R.id.text_result1);
        text_result2 =findViewById(R.id.text_result2);
        text_time=findViewById(R.id.text_time);
        text_amount1.append("1");
        btn_reset=findViewById(R.id.btn_reset);
        btn_retype=findViewById(R.id.btn_retype);
        text_currency_code1=findViewById(R.id.text_currency_code1);
        text_currency_code2=findViewById(R.id.text_currency_code2);
        text_rate_info1=findViewById(R.id.text_rate_info1);
        text_rate_info2=findViewById(R.id.text_rate_info2);
        str_currency_code1=text_currency_code1.getText().toString().toLowerCase();
        str_currency_code2=text_currency_code2.getText().toString().toLowerCase();
        image_flag1=findViewById(R.id.image_flag1);
        image_flag2=findViewById(R.id.image_flag2);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                setTextAmount_KeyUp("1");
                return true;
            case KeyEvent.KEYCODE_2:
                setTextAmount_KeyUp("2");
                return true;
            case KeyEvent.KEYCODE_3:
                setTextAmount_KeyUp("3");
                return true;
            case KeyEvent.KEYCODE_4:
                setTextAmount_KeyUp("4");
                return true;
            case KeyEvent.KEYCODE_5:
                setTextAmount_KeyUp("5");
                return true;
            case KeyEvent.KEYCODE_6:
                setTextAmount_KeyUp("6");
                return true;
            case KeyEvent.KEYCODE_7:
                setTextAmount_KeyUp("7");
                return true;
            case KeyEvent.KEYCODE_8:
                setTextAmount_KeyUp("8");
                return true;
            case KeyEvent.KEYCODE_9:
                setTextAmount_KeyUp("9");
                return true;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_DEL:
                if(isNetworkAvailable()) {
                    if ((CURRENCY_EXCHANGE == 1 && (text_amount1.getText().toString().equals("") || text_amount1.getText().toString().equals("0")))
                            || (CURRENCY_EXCHANGE == 2 && (text_amount2.getText().toString().equals("") || text_amount2.getText().toString().equals("0")))
                    ) {
                        text_amount1.setText("0");
                        text_amount2.setText("0");
                    } else {
                        setAll();
                    }
                }
                else {
//                    if (!Utility.hasSendToast(getApplicationContext())) {
//                        Toast.makeText(getApplicationContext(), "Please connect Wifi or LTE", Toast.LENGTH_LONG)
//                                .show();
//                        Utility.setSendToast(getApplicationContext(), true);
//                    }
                }
                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }
    }
    private void setTextAmount_KeyUp(String key){
        String text1=text_amount1.getText().toString();
        if(text1.equals(key+"0")||text1.equals("0"+key)) {
            if (CURRENCY_EXCHANGE == 1) {
                text_amount1.setText(key);
                text_amount1.setSelection(text_amount1.getText().length());
                text_amount1.moveCursorToVisibleOffset();
            }
        }
        else {
            String text2=text_amount2.getText().toString();
            if (text2.equals(key + "0") || text2.equals("0" + key)) {
                if (CURRENCY_EXCHANGE == 2) {
                    text_amount2.setText(key);
                    text_amount2.setSelection(text_amount2.getText().length());
                    text_amount2.moveCursorToVisibleOffset();
                }
            }
        }
        setAll();
    }
    private void setTextRateInfo(String code1, String code2){
        try {
            listRate=new RssFetchFeedTask(("https://" + code1 +
                    ".fxexchangerate.com/" + code2 + ".xml").toLowerCase()).execute().get();
        } catch (ExecutionException | InterruptedException e ) {
            e.printStackTrace();
        }
        if (listRate.get(0) > listRate.get(1)) {
            text_rate_info1.setText( ("1.00 "+code1+" = "+String.format("%,.4f", listRate.get(0))+" "+code2).toUpperCase());
            text_rate_info2.setText( ("1.00 "+code2+" = "+String.format("%,.10f", 1 / listRate.get(0))+" "+code1).toUpperCase());

        } else {
            text_rate_info1.setText(("1.00 " + code1 + " = " + String.format("%,.10f", 1 / listRate.get(1)) + " " + code2).toUpperCase());
            text_rate_info2.setText(("1.00 " + code2 + " = " + String.format("%,.4f", listRate.get(1)) + " " + code1).toUpperCase());
        }

    }

    private void setAmount(){
        Double amount1=0.0, amount2=0.0;
        if(CURRENCY_EXCHANGE==1) amount1=Double.parseDouble(text_amount1.getText().toString());
        if(CURRENCY_EXCHANGE==2) amount2=Double.parseDouble(text_amount2.getText().toString());
        if(listRate.get(0)>listRate.get(1)) {
            if(CURRENCY_EXCHANGE==1) {
                amount2=amount1*listRate.get(0);
                text_amount2.setText(String.valueOf(amount2));
            }
            if(CURRENCY_EXCHANGE==2) {
                amount1=amount2/listRate.get(0);
                text_amount1.setText(String.valueOf(amount1));
            }
        } else {
            if(CURRENCY_EXCHANGE==1) {
                amount2=amount1/listRate.get(1);
                text_amount2.setText(String.valueOf(amount2));
            }
            if(CURRENCY_EXCHANGE==2) {
                amount1=amount2*listRate.get(1);
                text_amount1.setText(String.valueOf(amount1));
            }
        }


    }

    private void setTextResult(){
        String pattern = "HH:mm:ss MM-dd-yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String currentTime = df.format(today);
        text_time.setText("Update at "+ currentTime);
        text_result1.setText(
                (String.format("%,.3f", Double.parseDouble(String.valueOf(text_amount1.getText()))))+" "
                        + str_currency_code1.toUpperCase()
        );
        text_result2.setText(
                (String.format("%,.3f", Double.parseDouble(String.valueOf(text_amount2.getText())))) +" "
                        + str_currency_code2.toUpperCase()
        );
    }

    private boolean setAll(){
        if(isNetworkAvailable()) {
            setTextRateInfo(str_currency_code1, str_currency_code2);
            setAmount();
            setTextResult();
            return true;
        }else {
//            if (!Utility.hasSendToast(getApplicationContext())) {
//                Toast.makeText(getApplicationContext(), "Please connect Wifi or LTE", Toast.LENGTH_LONG)
//                        .show();
//                Utility.setSendToast(getApplicationContext(), true);
//            }
            return false;
        }
    }

    private void getAllCountries() {
        ContentValues callParams = new ContentValues();
        callParams.put("username", "tdnghia");
        serviceHandler = new ServiceHandler(this, listCountries,callParams);
        try {
            listCountries=serviceHandler.execute(ServiceHandler.CREATE).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class RssFetchFeedTask extends AsyncTask<Void, Void, ArrayList<Double>> {
        Exception exception;
        private final String urlLink;
        ArrayList<Double> list =new ArrayList<>();


        public RssFetchFeedTask(String urlLink) {
            this.urlLink = urlLink;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Double> doInBackground(Void... voids) {
            try {
                URL url = new URL(urlLink);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                String s = (xpp.nextText());
                                list=get_rate(s);
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                        insideItem = false;
                    eventType = xpp.next();
                }
            } catch (IOException | XmlPullParserException e) {
                exception = e;
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Double> e) {
            super.onPostExecute(e);

        }

        private ArrayList<Double> get_rate(String s) {
            ArrayList<Double> list = new ArrayList<>();
            int l = s.length();
            int saved_index = 9;
            int count = 0;
            for (int i = 9; i < l; i++) {
                if (s.charAt(i) == '\n') {
                    if (count == 0) list.add(_get_rate(s.substring(saved_index, i - 5)));
                    if (count == 1) list.add(_get_rate(s.substring(saved_index, i - 5)));
                    saved_index = i + 9;
                    i = i + 9;
                    count++;
                }
                if (count == 2) break;
            }
            return list;
        }

        private Double _get_rate(String s) {
            int l = s.length() - 4;
            for (int i = l - 1; i > 0; i--) {
                if (s.charAt(i) == ' ') {
                    String text = s.substring(i, l);
                    double rate = Double.parseDouble(text);
                    return rate;
                }
            }
            return 0.0;
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}