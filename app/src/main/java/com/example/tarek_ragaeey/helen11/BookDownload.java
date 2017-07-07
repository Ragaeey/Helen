package com.example.tarek_ragaeey.helen11;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookDownload {
    private ProgressDialog pDialog;
    private Activity activityContext;
    private String BOOK_TITLE,REFERER;
    BookDownload(){super();}
    public void getDownload (String downloadLink,String bookTitle,String referer ,Activity context)
    {
        activityContext = context;
        BOOK_TITLE = bookTitle;
        REFERER = referer;
        new DownloadFileFromURL().execute(downloadLink);
    }
    private class DownloadFileFromURL extends AsyncTask<String, String, String>
    {
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activityContext);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(f_url[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty ("referer", REFERER);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                // getting file length
                int lenghtOfFile = urlConnection.getContentLength();

                // input stream to read file - with 10M buffer
                InputStream input = urlConnection.getInputStream();
                byte[] data = input.toString().getBytes();
                long total = 0;
                String filename = BOOK_TITLE+".pdf";
                String path = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(path))
                {
                    //Creating a new directory for the file if it doesn't exist
                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS)+"/Helen/");
                    if(!folder.exists())
                    {
                        if(folder.mkdirs())
                        {
                            //Creating a new file with the book title
                            File file = new File(folder,filename);
                            file.createNewFile();
                            // Output stream to write file
                            FileOutputStream output = new FileOutputStream(file,true);
                            while ((count = input.read(data)) != -1) {
                                total += count;
                                // publishing the progress....
                                // After this onProgressUpdate will be called
                                publishProgress(""+(int)((total*100)/lenghtOfFile));

                                // writing data to file
                                output.write(data, 0, count);
                            }
                            // flushing output
                            output.flush();

                            // closing streams
                            output.close();
                            input.close();
                        }
                    }
                   else
                    {
                        //Creating a new file with the book title
                        File file = new File(folder,filename);
                        file.createNewFile();
                        // Output stream to write file
                        FileOutputStream output = new FileOutputStream(file,true);
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress(""+(int)((total*100)/lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }
                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();
                    }
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();
            HttpURLConnection urlConnection = null;
            try{
                Uri builtUri = Uri.parse("http://127.0.0.1:8000/book/book_download/?book_title="+BOOK_TITLE);
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
