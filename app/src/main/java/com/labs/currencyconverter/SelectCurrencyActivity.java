package com.labs.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SelectCurrencyActivity extends AppCompatActivity {
    private ArrayList<Country> list;
    private RecyclerView rcv;
    private CountryAdapter adapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);
        list=(ArrayList<Country>) getIntent().getSerializableExtra("listCountries");
        rcv = (RecyclerView) findViewById(R.id.rcvCountries);
        adapter = new CountryAdapter(list, this);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.addItemDecoration(new
                DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rcv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rcv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent resultIntent = new Intent(SelectCurrencyActivity.this, Main_Activity.class);
                        // TODO Add extras or a data URI to this intent as appropriate.
                        resultIntent.putExtra("countryName", adapter.getItem(position).getCountryName());
                        resultIntent.putExtra("countryCode", adapter.getItem(position).getCountryCode());
                        resultIntent.putExtra("currencyCode", adapter.getItem(position).getCurrencyCode());
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_currency, menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}