package com.example.tarek_ragaeey.helen11;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.tarek_ragaeey.helen11.PDFViewer;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
//        implements Preference.OnPreferenceChangeListener
{



    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        /*SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);*/
    }

    /*@Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        final ListPreference listPreference = (ListPreference) findPreference((CharSequence) o);
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                SharedPreferences sharedPreferences=getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("Settings","value of speechRate=" +(float)newValue );
                editor.putFloat("speechRate",(float)newValue );
                editor.commit();
                return false;
            }
        });


        return false;

    }*/


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        final ListPreference listPreference = (ListPreference) findPreference(s);

         sharedPreferences=getSharedPreferences("com.example.tarek_ragaeey.helen11", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       // Log.e("Settings","value of speechRate= " +listPreference.getValue()) ;

        float rate=Float.parseFloat(listPreference.getValue());
        Log.e("Settings","value of speechRate= " +rate );
        editor.putFloat("speechRate",rate );
        editor.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
