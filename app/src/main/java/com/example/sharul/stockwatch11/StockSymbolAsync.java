package com.example.sharul.stockwatch11;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Sharul on 07-03-2017.
 */

public class StockSymbolAsync extends AsyncTask<String, Void, String>
{
    private static final String TAG = "StockSymbolAsync";
    private MainActivity mainActivity;
    private HashMap<String, String> wData = new HashMap<>();
    //private HashMap<String, String> fData1 = new HashMap<>();
    //private HashMap<String, Double> fData2 = new HashMap<>();
    private int sym;

    private String lc = "";
    private final String stockURL = "http://stocksearchapi.com/api/";
    private final String myAPI = "e6d6445e5c4370e66c38d86596dc4e943443e4d1";
   // private final String financeURL = "https://finance.google.com/finance/info?client=ig";
   final String[] sArray = new String[20];


    public StockSymbolAsync(MainActivity ma) {mainActivity = ma;}

    @Override
    protected void onPostExecute(String s)
    {
       // if(lc.equals("STOCK"))
         //   {


            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Make a selection");
            // builder.setIcon(R.drawable.icon2);

            builder.setItems(sArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sym=which;
                    String[] sp = sArray[sym].split(" - ");
                    String symbol = sp[0];
                    String comp = sp[1];
                    mainActivity.newSymbol(symbol,comp);
                    // tv2.setText(sArray[which]);
                }
            });
            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        //mainActivity.updateStock(wData);
        //}
     /*  else if(lc.equals("FINANCIAL"))
        {
            Toast.makeText(mainActivity, s, Toast.LENGTH_LONG).show();
            mainActivity.updateData(fData1,fData2);
        }

*/
    }



    @Override
    protected String doInBackground(String... params)
    {
        lc = params[1];
      //  if (params[1].equals("STOCK"))
       // {
            Uri.Builder buildURL = Uri.parse(stockURL).buildUpon();

            buildURL.appendQueryParameter("api_key", myAPI);
            buildURL.appendQueryParameter("search_text", params[0]);
            String urlToUse = buildURL.build().toString();
            Log.d(TAG, "doInBackground: " + urlToUse);

            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlToUse);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                Log.d(TAG, "doInBackground: " + sb.toString());

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }

            parseJSON(sb.toString());
     //   }

    /*   else if(params[1].equals("FINANCIAL"))
        {
            Uri.Builder buildURL = Uri.parse(financeURL).buildUpon();

            buildURL.appendQueryParameter("q", params[0]);

            String urlToUse = buildURL.build().toString();
            Log.d(TAG, "doInBackground: " + urlToUse);

            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlToUse);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Log.d(TAG, "doInBackground: " + sb.toString());

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
            sb.replace(0,2,"");
           parseJSON(sb.toString());
        }
        */
        return params[1];
    }

    private void parseJSON(String s)
    {
      //  if(lc.equals("STOCK")) {

            try {
                JSONArray stock = new JSONArray(s);
                //JSONObject jStock = (JSONObject) stock.get(0);
               // wData.put("SYMBOL", jStock.getString("company_symbol"));
               // wData.put("CNAME", jStock.getString("company_name"));
                int l = stock.length();
                for(int i = 0;i<l;i++)
                {
                    JSONObject jStock = (JSONObject) stock.get(i);
                    wData.put("SYMBOL", jStock.getString("company_symbol"));
                    wData.put("CNAME", jStock.getString("company_name"));
                    sArray[i] = wData.get("SYMBOL") + " - " + wData.get("CNAME");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
       // }
      /*  else if(lc.equals("FINANCIAL"))
        {
            try
            {
                JSONArray data = new JSONArray(s);
                JSONObject jstock = (JSONObject) data.get(0);
                fData1.put("TICKER", jstock.getString("t"));
                fData2.put("LASTTP", jstock.getDouble("l"));
                 fData2.put("PRICECHANGE", jstock.getDouble("c"));
                 fData2.put("PERCHANGE", jstock.getDouble("cp"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }
}
