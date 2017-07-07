package com.example.tarek_ragaeey.helen11;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookSearch {
    private  Activity activityContext;
    private AlertDialog alertDialog;
    private JSONObject BookInfo = null;
    BookSearch (Activity context)
    {
        super();
        activityContext = context;
    }
    public JSONObject getBookByTitle(String book_title)
    {
        String title = book_title.replace(" ","%20");
        String _BASE_URL = "http://127.0.0.1:8000/book/search/book/?book_title="+title;
        new searchAsyhTask().execute(_BASE_URL);
        return BookInfo;
    }
    public JSONObject getBookByAuthor(String book_author)
    {
        String author = book_author.replace(" ","%20");
        String _BASE_URL = "http://127.0.0.1:8000/book/search/author/?author="+author;
        new searchAsyhTask().execute(_BASE_URL);
        return BookInfo;
    }
    public JSONObject getSimilarByTitle(String book_title)
    {
        String title = book_title.replace(" ","%20");
        String _BASE_URL = "http://127.0.0.1:8000/book/recommend/book/?book_title="+title;
        new searchAsyhTask().execute(_BASE_URL);
        return BookInfo;
    }
    public JSONObject getSimilarByAuthor(String book_author)
    {
        String author = book_author.replace(" ","%20");
        String _BASE_URL = "http://127.0.0.1:8000/book/recommend/author/?author="+author;
        new searchAsyhTask().execute(_BASE_URL);
        return BookInfo;
    }
    public  JSONObject getFullBookInfo(String book_title)
    {
        String title = book_title.replace(" ","%20");
        String _BASE_URL = "http://127.0.0.1:8000/book/search/detail/?book_title="+title;
        new searchAsyhTask().execute(_BASE_URL);
        return BookInfo;
    }
    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void showDialogMsg(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityContext);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
                //getActivity().finish();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class searchAsyhTask extends AsyncTask<String, Void, JSONObject> {
        //private final String LOG_TAG = MainActivityFragment.FetchBookTask.class.getSimpleName();
        private JSONObject getBooksDataFromJson(String bookJsonStr) throws JSONException {
            JSONArray booksJsonArray = new JSONArray(bookJsonStr);
            JSONObject booksJsonObj = new JSONObject();
            booksJsonObj.put("booksinfo",booksJsonArray);
            return  booksJsonObj;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String BooksJsonStr = null;
            try {
                Uri builtUri = Uri.parse(params[0]);
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                BooksJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("Error", "Could not connect ", e);
                showDialogMsg("Please check your internet connection!");
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Error", "Error closing stream", e);
                    }
                }
            }
            try{
                return getBooksDataFromJson(BooksJsonStr);
            } catch (JSONException e)
            {
                Log.e("Error",e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                BookInfo = result;
            }
        }
    }
}
