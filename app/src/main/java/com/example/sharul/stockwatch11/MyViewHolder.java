package com.example.sharul.stockwatch11;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by Sharul on 05-03-2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder
{
   public static TextView symbol;
    TextView name;
    TextView price;
    TextView pchange;
    TextView changePer;
    TextView direction;

    MyViewHolder(View item)
    {
        super(item);

        symbol = (TextView)item.findViewById(R.id.stock_sym);
        name = (TextView)item.findViewById(R.id.name_comp);
        price = (TextView)item.findViewById(R.id.trade_price);
        pchange = (TextView)item.findViewById(R.id.price_change);
        changePer = (TextView)item.findViewById(R.id.price_perc);
        direction = (TextView)item.findViewById(R.id.price_direct);

    }
}
