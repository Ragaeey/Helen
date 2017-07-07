package com.example.tarek_ragaeey.helen11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TransitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);
        Intent i = this.getIntent();
        String queryClass=i.getStringExtra("query_class");
        String Entity=i.getStringExtra("entity");
        String Type=i.getStringExtra("type");
        switch (queryClass) {
            case "Search":
                break;
            case "Recommend":
                break;
            case "ReadBook":
                break;

            case "Summary":
                break;

            case "WriteReview ":
                break;

            case "GetReview ":
                break;

            case "WriteRating ":
                break;
            case "GetRating":
                break;
            case"AuthorName":
                break;




            default:
                break;
        }
    }
}
