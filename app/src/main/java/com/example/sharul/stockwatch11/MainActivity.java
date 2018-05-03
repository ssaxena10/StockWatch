package com.example.sharul.stockwatch11;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private RecyclerView recycler;
    private TextView tv1;
    private StockAdapter myAdapter;
    private List<Stock> stockList = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private DatabaseHandler databaseHandler;
    private String SOL = "";
    private HashMap<String, String> wdata = new HashMap<>();
    private HashMap<String, String> fdata1 = new HashMap<>();
    private String sym,comp = "";

    private static String url = "http://www.marketwatch.com/investing/stock/";

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView)findViewById(R.id.recycler);
        myAdapter = new StockAdapter(stockList, this);
        recycler.setAdapter(myAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        swiper = (SwipeRefreshLayout)findViewById(R.id.swiper);
        databaseHandler = new DatabaseHandler(this);

        databaseHandler.dumpLog();
        stockList = databaseHandler.loadStocks();

       stockList.addAll(stockList);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();

            }
        });
        //Stock s = new Stock("SOS","sos.com",1.0,1.0,1.0);
        //databaseHandler.addStock(s);
        // Make some data - not always needed - used to fill list
       /* for (int i = 0; i < 20; i++) {
            s.setSymbol("AMZN");
            s.setName("Amazon.com, inc.");
            s.setPrice(845.07);
            s.setPchange(0.38);
            s.setChangePer(0.28);
            stockList.add(s);
        }
        */

      //  DatabaseHandler.getInstance(this).setupDb();


    }

  /* @Override
    protected void onResume() {
        DatabaseHandler.getInstance(this).dumpLog();
        ArrayList<Stock> list = DatabaseHandler.getInstance(this).loadStocks();
        stockList.clear();
        stockList.addAll(list);
        Log.d(TAG, "onResume: " + list);
        myAdapter.notifyDataSetChanged();

        super.onResume();
    }*/

   /* @Override
    protected void onDestroy() {
        DatabaseHandler.getInstance(this).shutDown();
        super.onDestroy();
    }*/

    private void doRefresh() {
        //Add access in app manifest

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        swiper.setRefreshing(false);


        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            //Collections.shuffle(stockList);
            //Collections.shuffle();
            //myAdapter.notifyDataSetChanged();
            Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You are NOT Connected to the Internet!", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "List content shuffled", Toast.LENGTH_LONG).show();

    }

    public void updateData(HashMap<String, String > fData1, HashMap<String, Double> fData2)
    {
        fdata1 = fData1;
       // if(fData1 == wdata) {
            Stock s2 = new Stock(sym,comp, fData2.get("LASTTP"), fData2.get("PRICECHANGE"), fData2.get("PERCHANGE"));
            //Stock(s1);
            stockList.add(s2);
            myAdapter = new StockAdapter(stockList, this);
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(this));
            // databaseHandler.addStock(s2);
       // }
    }

    public void updateStock(HashMap<String, String> wData) {
        if (wData.isEmpty()) {
            Toast.makeText(this, "Please Enter valid Stock Symbol", Toast.LENGTH_SHORT).show();
            return;
        }
        Stock s1 = new Stock(wData.get("SYMBOL"), wData.get("CNAME"));
        //Stock(s1);
        //stockList.add(s1);
        //myAdapter = new StockAdapter(stockList, this);
        //recycler.setAdapter(myAdapter);
        //recycler.setLayoutManager(new LinearLayoutManager(this));
        wdata = wData;
        databaseHandler.addStock(s1);
        new StockFinancialAsync(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SOL, "FINANCIAL");


       /* TextView symbol = (TextView) findViewById(R.id.stock_sym);
        symbol.setText(wData.get("SYMBOL"));

        TextView name = (TextView) findViewById(R.id.name_comp);
        name.setText(wData.get("CNAME"));
        */
    }
    public void newSymbol(String symbol, String company)
    {
        Stock s = new Stock(symbol,company);
        // StockList.add(s);
        //mAdapter = new StockAdapter(StockList, this);
        //recyclerView.setAdapter(mAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //final CharSequence[] sArray = new CharSequence[20];
        databaseHandler.addStock(s);
        sym = symbol;
        comp = company;
        //fdata = wData;
        new StockSymbolAsync(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sym, "Fin");

        //ArrayList<Stock> list = databaseHandler.loadStocks();
        //StockList.addAll(list);
        Toast.makeText(this, "Stock Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(et);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SOL = et.getText().toString();
                        //StockSymbolAsync sa = new StockSymbolAsync(MainActivity.this);
                        //sa.execute(SOL);
                        //StockFinancialAsync fa = new StockFinancialAsync(MainActivity.this);
                        //fa.execute(SOL);
                        //doToast();'

                        new StockSymbolAsync(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,SOL,"STOCK");

                        //new StockSymbolAsync(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,SOL,"FINANCIAL");

                        //new StockFinancialAsync()
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                builder.setMessage("Please enter a Stock Symbol:");
                builder.setTitle("Stock Selection");

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void doToast()
    {
        Toast.makeText(this, "Will search for that info later!!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {

        Toast.makeText(this, SOL, Toast.LENGTH_SHORT).show();
        String Url = url;
        //MyViewHolder.symbol.getText();
       // Uri.Builder buildURL = Uri.parse(url).buildUpon();

        //buildURL.appendQueryParameter("api_key", myAPI);
        //buildURL.appendQueryParameter("", SOL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Url));
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recycler.getChildLayoutPosition(v);


        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                    databaseHandler.deleteStock(stockList.get(pos).getSymbol());
                    stockList.remove(pos);
                    myAdapter.notifyDataSetChanged();


            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // dialog cancelled
            }
        });
        build.setMessage("Delete Stock?");
        AlertDialog dialog = build.create();
        dialog.show();
        return false;
        //Toast.makeText(this, "Long Click", Toast.LENGTH_SHORT).show();
        //return false;
    }

   /* public void doAdd(View v) {
        Intent intent = new Intent(this, Stock.class);
        startActivityForResult(intent, ADD_CODE);
    }*/
}
