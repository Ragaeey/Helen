package com.example.tarek_ragaeey.helen11;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {


    public BooksFragment() {
        // Required empty public constructor
    }

    Set<String> BookList;
    Boolean ThereisBooks=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Books");
    }

    private ArrayList<String> Books=new ArrayList<>();
    private ArrayList<String> BooksNames=new ArrayList<>();
    private ArrayAdapter<String> mBooksAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_books, container, false);


        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);

        BookList=sharedPreferences.getStringSet("books",null);
        if(BookList!=null)
        {

                //Books.addAll(BookList);
                for (Iterator<String> it = BookList.iterator(); it.hasNext(); ) {
                    String uri = it.next();
                    Log.e("Book on create",uri);
                    Books.add(uri);
                    Uri myUri = Uri.parse(uri);
                    File myFile = new File(myUri.getPath());
                   String FilePath = myFile.getAbsolutePath();
                    String FileName =FilePath.substring(FilePath.lastIndexOf("/")+1);
                    FileName.replaceAll(".pdf","");
                    BooksNames.add(FileName);

                     }


        }
        else
        {
            BookList=new HashSet<String>();
          //  BooksNames.add("You Haven't opened any book yet");
        }


        mBooksAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_books_2, // The name of the layout ID.
                        R.id.list_item_books_textview_2, // The ID of the textview to populate.
                        BooksNames);

    /*    mBooksAdapter.add("Alice In WonderLand");
        mBooksAdapter.add("A Tale Of Two Cities");
        mBooksAdapter.add("App Statistics Report");
        mBooksAdapter.add("Women From Venus Men From Mars");
        mBooksAdapter.add("The Shining");*/
        ListView listView = (ListView) rootView.findViewById(R.id.listview_books);
        listView.setAdapter(mBooksAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String uri=Books.get(i);
                Intent intent = new Intent(getActivity(), PDFViewer.class);
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.get_pdf:
                showFileChooser();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "Tarek";

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FILE_SELECT_CODE) {

            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();

                File myFile = new File(uri.getPath());
                /*String FilePath= myFile.getAbsolutePath();
                String Filename=FilePath.substring(FilePath.lastIndexOf("/")+1);*/
                // Get the path
             /*   if(BookList==null)
                {
                    BookList=new HashSet<String>();
                }*/
                if(!Books.contains(uri.toString())){
                    BookList.add(uri.toString());
                    ///////////////////////////////////////////////
                    Books.add(uri.toString());


                    String FilePath = myFile.getAbsolutePath();
                    String FileName =FilePath.substring(FilePath.lastIndexOf("/")+1);
                    FileName.replaceAll(".pdf","");
                    BooksNames.add(FileName);
                    mBooksAdapter.notifyDataSetChanged();
                    /////////////////////////////////////////////

                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    edit.clear();
                    edit.putStringSet("books", BookList);
                    edit.commit();
                    //sharedPreferences.edit().putStringSet("books",BookList).apply();

                }


                Intent i = new Intent(getActivity(), PDFViewer.class);
                i.putExtra("uri", uri.toString());
               /* i.putExtra("path",FilePath );
                i.putExtra("name",Filename );
                i.putExtra("page","");*/
                startActivity(i);
            }
        }
        else if((requestCode == 100) && (data != null) ){

            // Store the data sent back in an ArrayList
            ArrayList<String> spokenText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            EditText wordsEntered = (EditText) getActivity().findViewById(R.id.input_text);

            // Put the spoken text in the EditText
            wordsEntered.setText(spokenText.get(0));

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public void ExceptSpeechInput(View view) {

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


}
