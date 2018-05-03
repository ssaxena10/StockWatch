package com.example.sharul.stockwatch11;

import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.HashMap;

/**
 * Created by Sharul on 07-03-2017.
 */

public class StockFinancialAsync extends AsyncTask<String, String, String>
{
    private static final String TAG = "StockFinancialAsync";
    private MainActivity mainActivity;
    private HashMap<String, String> fData1 = new HashMap<>();
    private HashMap<String, Double> fData2 = new HashMap<>();
    private String lc = "";


    private final String financeURL = "https://finance.google.com/finance/info?client=ig";

    public StockFinancialAsync(MainActivity ma) {mainActivity = ma;}

    @Override
    protected void onPostExecute(String s)
    {
        Toast.makeText(mainActivity, s, Toast.LENGTH_LONG).show();
        mainActivity.updateData(fData1,fData2);
    }

    
    @Override
    protected String doInBackground(String... params)
    {

        lc = params[1];
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

        return params[1];

    }

    private void parseJSON(String s)
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
    }
}
