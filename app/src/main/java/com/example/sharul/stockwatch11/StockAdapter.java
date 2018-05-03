package com.example.sharul.stockwatch11;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sharul on 05-03-2017.
 */

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    private List<Stock> stockList;
    private MainActivity mainActivity;

    public StockAdapter(List<Stock> list, MainActivity main)
    {
        this.stockList = list;
        this.mainActivity = main;
    }

    public void updateList(List<Stock> sList)
    {
        this.stockList.clear();
        this.stockList.addAll(sList);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup view, int i)
    {
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.cardview, view, false);
        v.setOnClickListener(mainActivity);
        v.setOnLongClickListener(mainActivity);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos)
    {
        holder.symbol.setText(this.stockList.get(pos).getSymbol());
        holder.name.setText(this.stockList.get(pos).getName());
        holder.price.setText(String.valueOf(this.stockList.get(pos).getPrice()));
        holder.pchange.setText(String.valueOf(this.stockList.get(pos).getPchange()));
        holder.changePer.setText(String.valueOf(this.stockList.get(pos).getChangePer()));
        holder.direction.setText("+");

    }

    @Override
    public int getItemCount(){return this.stockList.size();}
}
