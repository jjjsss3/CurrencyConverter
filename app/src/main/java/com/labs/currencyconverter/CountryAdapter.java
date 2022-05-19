package com.labs.currencyconverter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {
    private ArrayList<Country> listCountries;
    private ArrayList<Country> listCountriesOld;

    // Lưu Context để dễ dàng truy cập
    private Context context;

    public CountryAdapter(ArrayList<Country> listCountries, Context context) {
        this.listCountries = listCountries;
        this.context = context;
        this.listCountriesOld=listCountries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử sinh viên
        View view =
                inflater.inflate(R.layout.country_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country =  listCountries.get(position);
//        Picasso.with(context).load(country.get_URL_FLAG_GIF()).placeholder(R.drawable.progress_animation).into(holder.im_item);

        Glide.with(context).load(country.get_URL_FLAG_GIF()).placeholder(R.drawable.progress_animation).into(holder.im_item);
        holder.tv_name.setText(country.getCountryName());
        holder.tv_code.setText(country.getCurrencyCode());

    }

    @Override
    public int getItemCount() {
        return listCountries.size();
    }

    public Country getItem(int position) {
        return listCountries.get(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView im_item;
        TextView tv_name;
        TextView tv_code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            im_item = itemView.findViewById(R.id.countryFlag);
            tv_name = itemView.findViewById(R.id.countryName);
            tv_code = itemView.findViewById(R.id.currencyCode);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch=constraint.toString();
                if(strSearch.isEmpty()){
                    listCountries=listCountriesOld;
                }else {
                    ArrayList<Country> list=new ArrayList<>();
                    for (Country c: listCountriesOld
                    ) {
                        if(c.getCountryName().toLowerCase().contains(strSearch.toLowerCase())||c.getCurrencyCode().toLowerCase().contains(strSearch.toLowerCase()))
                            list.add(c);
                    }
                    listCountries=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listCountries;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCountries= (ArrayList<Country>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}