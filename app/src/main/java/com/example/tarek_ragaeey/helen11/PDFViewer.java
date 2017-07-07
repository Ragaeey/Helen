package com.example.tarek_ragaeey.helen11;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PDFViewer extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    private int currentWord = 0;
    private int Page_number_of_words;
    private Locale currentSpokenLang = Locale.US;
    private TextToSpeech textToSpeech;
    private PDFView pdfView;
    Context context;
    private Uri myUri;
    private String FilePath;
    private String FileName;
    private Integer m_Integer = 0;
    private boolean Completed = false;
    public float speechRate = 1;
    private File MyFile;
    private MenuItem play_pause_item;
    private static final int SETTINGS_INFO = 1;
    HashMap<String, String> TTSmap = new HashMap<String, String>();
    com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener;
    private boolean pause = false;
    private String pageText;
    private ArrayList<Integer> Start=new ArrayList<>();
    private ArrayList<Integer> End=new ArrayList<>();
    private Integer StartEndIT=0;
    private Integer StartEndDiff=0;
/////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();

        ExtractDataFromuri(i.getStringExtra("uri"));


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(FileName);
            //  actionBar.setDisplayShowHomeEnabled(false);
        }
        play_pause_item = (MenuItem) findViewById(R.id.play_pause);

        setContentView(R.layout.activity_pdfviewer);
        ////////////////////////////////////////////////////////////

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);

        speechRate = sharedPreferences.getFloat("speechRate", 1);
        ////////////////////////////////////////////////////////////

        onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {


                Log.e("Tarek Change Page", "LOL " + page);

            }
        };


        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (!textToSpeech.isSpeaking())
                    alertChangePage();

            }
        });
        pdfView.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View view) {
                if (!textToSpeech.isSpeaking())
                    alertChangePage();

                try {
                    toastText("Long was pressed");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return false;
            }

        });


        textToSpeech = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);

        pdfView.fromUri(myUri).onPageChange(onPageChangeListener).load();

    }

    /*
    extract file path and file name sent by the intent
     */
    private void ExtractDataFromuri(String uri) {

        myUri = Uri.parse(uri);
        File myFile = new File(myUri.getPath());
        FilePath = myFile.getAbsolutePath();
        Log.e("Filepath", FilePath);
        FileName = FilePath.substring(FilePath.lastIndexOf("/") + 1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdfviewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(PDFViewer.this, SettingsActivity.class);
                startActivity(i);
            case R.id.play_pause:
                play_pause();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSpeechRate() {
        textToSpeech.setSpeechRate(speechRate);
    }

    @Override
    public void onInit(int status) {
        // Check if TextToSpeech is available
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(currentSpokenLang);
            setSpeechRate();

            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {


                @Override
                public void onDone(String utteranceId) {

                    Log.e("Tarek onDone", "LOL");
                    if (Completed==true)
                        try {
                            ChangePage(pdfView.getCurrentPage() + 1);
                            Completed=false;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                   else if (pause == false)
                        try {
                            readTheText(pageText);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {


                    Log.e("Tarek onStart", "LOL");
                    //Completed = true;
                    // Toast.makeText(context,"ana aho",Toast.LENGTH_SHORT);
                }
            });

        }
    }

    private void toastText(final String content) throws RemoteException {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Toast.makeText(context, content, Toast.LENGTH_SHORT);
            }
        });
    }


    private void ChangePage(final int page) throws RemoteException {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pdfView.jumpTo(page);
                String TextToRead = pdfToText(FilePath, page + 1);
                if (TextToRead.length() > 0)
                    try {
                        pageText = TextToRead;
                        formSentences();
                        readTheText(TextToRead);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        ChangePage(page + 1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                Log.e("Page Text", TextToRead);

            }
        });


    }
    private void formSentences()
    {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        //String source = "This is a test. This is a T.L.A. test. Now with a Dr. in it.";
        iterator.setText(pageText);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            //System.out.println(.substring(start,end));
            Start.add(start); End.add(end);
        }

    }

    /*
    extract the text from the pdf one page a time
     */
    private String pdfToText(String path, int pageIndex) {
        try {

            PdfReader reader = new PdfReader(path);

            String page = PdfTextExtractor.getTextFromPage(reader, pageIndex);
           page= page.replaceAll(","," ");
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    private void readTheText(String pageContent) throws RemoteException {


        TTSmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");


            //if(End.get(StartEndIT)!=pageContent.length()-1) {
                String s = "";

                s=pageContent.substring(Start.get(StartEndIT),End.get(StartEndIT));



               // Log.d("7amada " + currentWord, words[currentWord]);
                textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, TTSmap);
                if(StartEndIT!=Start.size()-1) {
                    StartEndIT++;
                }
                else
                {

                    Completed=true;
                    Start.clear();
                    End.clear();
                    StartEndIT=0;
                }
           /* }
            else
            {
               Completed=true;
                Start.clear();
                End.clear();
                StartEndIT=0;
            }
*/
    }

    private void playSound(String words) {

        textToSpeech.speak(words, TextToSpeech.QUEUE_FLUSH, TTSmap);
    }

    private void play_pause() {
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();

           StartEndIT--;
           pause = true;
           // Completed = false;
        } else {
            try {
                ChangePage(pdfView.getCurrentPage());
                pause = false;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /*
    an alert asks the user to enter a page number
     */
    private void alertChangePage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Page Index");

        // Set up the input
        final EditText input = new EditText(this);

        if (textToSpeech.isSpeaking()) {
            play_pause();
            // play_pause_item.setIcon(R.drawable.play_action);

           // Completed = false;

        }

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        // input.setHint(pdfView.getCurrentPage());
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Integer = Integer.parseInt(input.getText().toString());
                try {
                    ChangePage(m_Integer - 1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);

        speechRate = sharedPreferences.getFloat("speechRate", 1);
        // play_pause();
        textToSpeech = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
        // setSpeechRate();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
