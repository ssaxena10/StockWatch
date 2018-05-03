package com.example.sharul.stockwatch11;

import java.io.Serializable;

/**
 * Created by Sharul on 05-03-2017.
 */

public class Stock implements Serializable
{
    private String symbol;
    private String name;
    private Double price;
    private Double pchange;
    private Double changePer;

    public Stock(String s, String n)
    {
        symbol = s;
        name = n;

    }

    public Stock(String s, String n, Double p, Double c, Double per)
    {
        symbol = s;
        name = n;
        price = p;
        pchange = c;
        changePer = per;

    }

    public Stock(Double p, Double c, Double per)
    {
        price = p;
        pchange = c;
        changePer = per;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPchange() {
        return pchange;
    }

    public void setPchange(Double pchange) {
        this.pchange = pchange;
    }

    public Double getChangePer() {
        return changePer;
    }

    public void setChangePer(Double changePer) {
        this.changePer = changePer;
    }
}
