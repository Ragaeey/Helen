package com.example.tarek_ragaeey.helen11;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tarek-Ragaeey on 7/2/2017.
 */

public class UnderstandUserTask extends AsyncTask <String, Void, ArrayList<String>>{
    ArrayList<String> Result=new ArrayList<>();
    String result = ""; //result of the query
    String queryClass=""; // returned class of the query ex: Readbook , searchbook
    String Entity=""; // returned entity of the query ex: lord of the rings
    String Type=""; // returned entity of the query ex: book or author
    @Override
    protected ArrayList<String> doInBackground(String... strings) {

        URL url;

        HttpURLConnection urlConnection = null;

        try {

            url = new URL("http://127.0.0.1:8000/questions/" + strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {

                char current = (char) data;

                result += current;

                data = reader.read();

            }
            JSONArray arr=new JSONArray(result);
            JSONObject root = arr.getJSONObject(0);
            queryClass=root.getString("query_class");
            JSONArray Entity_Type=root.getJSONArray("entities");
            JSONObject EntityAndType=Entity_Type.getJSONObject(0);

            Entity=EntityAndType.getString("entity");
            Type=EntityAndType.getString("type");
            Result.add(queryClass);
            Result.add(Entity);
            Result.add(Type);

        }catch (Exception e) {

            e.printStackTrace();

        }


        return Result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
    }
    /* @Override
    protected Void onPostExecute(Void aVoid ) {
            super.onPostExecute(aVoid);
        switch(queryClass) {
            case "ReadBook":
               break; // optional
            case "Search" :
                // Statements
                break; // optional
            case "Recommend" :
                // Statements
                break; // optional

            case "WriteReview" :
                // Statements
                break; // optional
            case "GetReview" :
                // Statements
                break; // optional
            case "WriteRating" :
                // Statements
                break; // optional
            case "GetRating" :
                // Statements
                break; // optional
            case "AuthorName" :
                // Statements
                break; // optional


            // You can have any number of case statements.
            default : // Optional
                break;// Statements
        }


    }*/
}
