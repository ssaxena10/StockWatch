package com.example.sharul.stockwatch11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sharul on 06-03-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";
    private static final String LTP = "LastTradePrice";
    private static final String PCA = "PriceChangeAmount";
    private static final String PCP = "PriceChangePercent";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";//, " +
                    //LTP + " TEXT not null, " +
                    //PCA + " TEXT not null, " +
                    //PCP + " TEXT not null)";

    private SQLiteDatabase database;

   private static DatabaseHandler instance;

   /* public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);
        return instance;
    } */

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void setupDb() {
        database = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addStock(Stock stock)

    {
        Log.d(TAG, "addStock: Adding "+ stock.getSymbol());

        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getSymbol());
        values.put(COMPANY, stock.getName());
        //values.put(LTP, stock.getPrice());
        //values.put(PCA, stock.getPchange());
        //values.put(PCP, stock.getChangePer());

        database.insert(TABLE_NAME, null, values);

        Log.d(TAG, "addStock: Add Complete" );
    }

    public void deleteStock(String symbol)
    {
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol);
        int cnt = database.delete( TABLE_NAME, SYMBOL + " = ?", new String[] { symbol });
        Log.d(TAG, "deleteStock: " + cnt);
    }

    public ArrayList<Stock> loadStocks()
    {
        Log.d(TAG, " loadStocks: Load all symbol-company entries from DB");

        ArrayList<Stock> stocks = new ArrayList<>();

        Cursor cursor = database.query( TABLE_NAME, // The table to query
                 new String[]{ SYMBOL, COMPANY }, //, LTP, PCA, PCP}, // The columns to return
                 null, // The columns for the WHERE clause, null means “*”
                 null, // The values for the WHERE clause, null means “*”
                 null, // don't group the rows
                 null, // don't filter by row groups
                 null); // The sort order
         if (cursor != null)
         { // Only proceed if cursor is not null
              cursor.moveToFirst();
             for (int i = 0; i < cursor.getCount(); i++)
             {
                 String symbol = cursor.getString(0); // 1st returned column
                  String company = cursor.getString(1); // 2nd returned column
                // String ltp = cursor.getString(2);
                // String pca = cursor.getString(3);
                // String pcp = cursor.getString(4);
                  stocks.add(new Stock (symbol, company));
                 //stocks.add(new Stock(ltp, pca, pcp));
                 cursor.moveToNext();
             }
             cursor.close();
         }
         return stocks;
    }

    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
               // String ltp = cursor.getString(2);
               // String pca = cursor.getString(3);
                //String pcp = cursor.getString(4);

                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", SYMBOL + ":", symbol) +
                        String.format("%s %-18s", COMPANY + ":", company) //+
                  //      String.format("%s %-18s", LTP + ":", ltp) +
                    //    String.format("%s %-18s", PCA + ":", pca) +
                      //  String.format("%s %-18s", PCP + ":", pcp)
                        );
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
    public void shutDown() {
        database.close();
    }


}

