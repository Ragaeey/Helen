package com.example.tarek_ragaeey.helen11;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment  {


    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_server, container, false);
        ImageView mMic=(ImageView) root.findViewById(R.id.mic_view);
        mMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExceptSpeechInput();
            }
        });

        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

         if((requestCode == 100) && (data != null) ){

            // Store the data sent back in an ArrayList
            ArrayList<String> spokenText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            EditText wordsEntered = (EditText) getActivity().findViewById(R.id.input_text);

            // Put the spoken text in the EditText
            wordsEntered.setText(spokenText.get(0));

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

        try{

            startActivityForResult(intent, 100);

        } catch (ActivityNotFoundException e){

            Toast.makeText(getActivity(),R.string.stt_not_supported_message, Toast.LENGTH_LONG).show();

        }

    }

    public class ServerTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                /*JSONObject root = new JSONObject(result);
                JSONArray sportsArray = root.getJSONArray("sport");
// now get the first element:
                JSONObject firstSport = sportsArray.getJSONObject(0);
// and so on
                String name = firstSport.getString("name"); // basketball
                int id = firstSport.getInt("id"); // 40
                JSONArray leaguesArray = firstSport.getJSONArray("leagues");
                */




            }catch (Exception e) {

                e.printStackTrace();

            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
