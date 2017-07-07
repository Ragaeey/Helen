package com.example.tarek_ragaeey.helen11;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
<<<<<<< HEAD
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
=======
>>>>>>> origin/master
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.support.v7.app.AlertDialog;
=======
import android.util.Log;
>>>>>>> origin/master
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
<<<<<<< HEAD
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
=======

>>>>>>> origin/master
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
<<<<<<< HEAD
public class ServerFragment extends Fragment  {
    private FragmentListener flisttener;
    private AlertDialog alertDialog;
    private BookSearch searcher;
=======
public class ServerFragment extends Fragment {


>>>>>>> origin/master
    public ServerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_server, container, false);
        ImageView mMic = (ImageView) root.findViewById(R.id.mic_view);
        mMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  ExceptSpeechInput();
            search();
            }
        });

        return root;
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void showDialogMsg(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
                //getActivity().finish();
            }
        });

<<<<<<< HEAD
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void search()
    {

        EditText search_Query=(EditText) getActivity().findViewById(R.id.input_text);
        String query= "";
        if(search_Query != null)
        {
            Context context = getActivity();
            query = search_Query.getText().toString();
            if(isOnline(context)) {
                searcher = new BookSearch(getActivity());
                JSONObject bookInfo = null;
                try {
                    bookInfo = searcher.getBookByTitle(query);
                    bookInfo.put("query_param",query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), BookListActivity.class).putExtra("JSONObject", bookInfo.toString());
                startActivity(intent);
            }else
                showDialogMsg("Please check your internet connection!");
        }
        else
            showDialogMsg("Please enter a book title or an author name!");
    }

    ////////////////////////////////////////////////////////////////////
   /* public void TestJson()
    {
        String result = "[ { \"query\":\"I want to a book called bad case of stripes\"," +
                "\"query_class\":\"ReadBook\"," +
                "\"score\":0.8199217319488525," +
                "\"entities\":" +
                "[ { \"entity\":\"a bad case of stripes\"," +
                "\"type\":\"BOOK\"" +
                " } ]," +
                "\"intent\":\"ReadBook\"," +
                "\"query_lemma\":\"-PRON- want to a book call harry potter \"" +
                " } ]"; //result of the query
        String queryClass=""; // returned class of the query ex: Readbook , searchbook
        String Entity=""; // returned entity of the query ex: lord of the rings
        String Type =""; // returned entity of the query ex: book or author
      try {
          JSONArray root = new JSONArray(result);
          JSONObject root2=root.getJSONObject(0);
          queryClass = root2.getString("query_class");
          JSONArray Entity_Type = root2.getJSONArray("entities");
          JSONObject EntityAndType = Entity_Type.getJSONObject(0);

          Entity = EntityAndType.getString("entity");
          Type = EntityAndType.getString("type");
      } catch (Exception e) {
          e.printStackTrace();
        }
        EditText et=(EditText) getActivity().findViewById(R.id.input_text);
        et.setText("Query Class= "+queryClass +" Entity= "+Entity+" Type= "+Type);


    }*/

///////////////////////////////////////////////////////////////////////

=======
>>>>>>> origin/master
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 100) && (data != null)) {

            // Store the data sent back in an ArrayList
            ArrayList<String> spokenText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.e("Spoken Text", spokenText.get(0));
            ServerTask Task = new ServerTask();
            Task.execute(spokenText.get(0));
//            EditText wordsEntered = (EditText) getActivity().findViewById(R.id.input_text);
//
//            // Put the spoken text in the EditText
//            wordsEntered.setText(spokenText.get(0));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ExceptSpeechInput() {

        // Starts an Activity that will convert speech to text
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Use a language model based on free-form speech recognition
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Recognize speech based on the default speech of device
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // Prompt the user to speak
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_input_phrase));
<<<<<<< HEAD
        try{
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e){
            Toast.makeText(getActivity(),R.string.stt_not_supported_message, Toast.LENGTH_LONG).show();
=======

        try {

            startActivityForResult(intent, 100);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(getActivity(), R.string.stt_not_supported_message, Toast.LENGTH_LONG).show();

>>>>>>> origin/master
        }
    }

    public class ServerTask extends AsyncTask<String, Void, String> {
<<<<<<< HEAD
        String result = ""; //result of the query
        String queryClass=""; // returned class of the query ex: Readbook , searchbook
        String Entity=""; // returned entity of the query ex: lord of the rings
        String Type=""; // returned entity of the query ex: book or author
        URL url;
=======

        String result = "";
>>>>>>> origin/master

        @Override
        protected String doInBackground(String... urls) {

<<<<<<< HEAD
=======

            URL url;
>>>>>>> origin/master

            HttpURLConnection urlConnection = null;

            try {
<<<<<<< HEAD
                url = new URL(urls[0]);
=======
                Log.e("task", urls[0]);
                String SpeechToText=urls[0].replace(" ","%20");
                url = new URL("http://localhost:8000/api/" + SpeechToText + "/?format=json");
                Log.e("Link Name", url.toString());
>>>>>>> origin/master
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


                /*JSONObject root = new JSONObject(result);
                JSONArray sportsArray = root.getJSONArray("sport");
// now get the first element:
                JSONObject firstSport = sportsArray.getJSONObject(0);
// and so on
                String name = firstSport.getString("name"); // basketball
                int id = firstSport.getInt("id"); // 40
                JSONArray leaguesArray = firstSport.getJSONArray("leagues");
                */
<<<<<<< HEAD
            }catch (Exception e) {
=======


            } catch (Exception e) {
>>>>>>> origin/master

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
<<<<<<< HEAD
=======

            EditText wordsEntered = (EditText) getActivity().findViewById(R.id.input_text);

            // Put the spoken text in the EditText
            wordsEntered.setText(result);
>>>>>>> origin/master
        }
    }
}
