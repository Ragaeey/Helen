package com.example.tarek_ragaeey.helen11;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

 public class BookDetailsActivityfragment extends Fragment {
    private BookDetailsAdapter detailsAdapter;
    private String BOOK_TITLE;
    private String downloadLink,Referer;
    private BookDownload downloader;
    private View headerview;
    public  BookDetailsActivityfragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.book_details_body, container, false);
        headerview = inflater.inflate(R.layout.book_details_header,null);
        detailsAdapter = new BookDetailsAdapter(getActivity(),new ArrayList<JSONObject>());
        ListView listView = (ListView) rootview.findViewById(R.id.Book_Content_List_view);
        String bookDataString = getArguments().getString("JSONObject");
        try {
            JSONObject BookDataObj = new JSONObject(bookDataString);
            parseBookDataFromObj(BookDataObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.addHeaderView(headerview);
        listView.setAdapter(detailsAdapter);
        return rootview;
    }
    private void parseBookDataFromObj(JSONObject bookObj) throws JSONException{
        BOOK_TITLE = bookObj.getString("title");
        String AUTHOR = bookObj.getString("author");
        String IMAGEURL = bookObj.getString("image");
        String REL_DATE = bookObj.getString("published_date");
        String DESCRIPTION = bookObj.getString("description");
        String GOODREADS_RATING = bookObj.getString("goodreads_rating");
        String USER_RATING = bookObj.getString("user_ratings");
        Referer = bookObj.getString("referer");
        downloadLink = bookObj.getString("download_link");
        JSONArray REVIEWS = bookObj.getJSONArray("comments");

        List<JSONObject> Adapterinput = new ArrayList<JSONObject>();
        for(int i = 0; i < REVIEWS.length(); i++)
        {
            JSONObject bookReview = REVIEWS.getJSONObject(i);
            Adapterinput.add(bookReview);
        }
        if(Adapterinput != null)
        {
            detailsAdapter.clear();
            detailsAdapter.addAll(Adapterinput);
        }

        TextView Title = (TextView) headerview.findViewById(R.id.object_title);
        Title.setText(BOOK_TITLE);

        TextView authors = (TextView) headerview.findViewById(R.id.object_author);
        String authorsName = "by "+AUTHOR;
        authors.setText(authorsName);

        ImageView Poster = (ImageView) headerview.findViewById(R.id.object_poster);
        Picasso.with(getContext()).load(IMAGEURL).into(Poster);

        TextView OverView = (TextView) headerview.findViewById(R.id.object_desc);
        OverView.setText(DESCRIPTION);

        TextView ReleaseDate = (TextView) headerview.findViewById(R.id.object_release_date);
        ReleaseDate.setText("Released in: "+REL_DATE);

        RatingBar goodreads_rating = (RatingBar) headerview.findViewById(R.id.goodreads_rating);
        float rate = Float.parseFloat(GOODREADS_RATING);
        goodreads_rating.setRating(rate);

        RatingBar user_ratings = (RatingBar) headerview.findViewById(R.id.user_ratings);
        float u_rate = Float.parseFloat(USER_RATING);
        user_ratings.setRating(u_rate);

        Button downloadButton = (Button) headerview.findViewById(R.id.download_button);
        downloadButton.setVisibility(View.VISIBLE);
        downloadButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloader = new BookDownload();
                downloader.getDownload(downloadLink,BOOK_TITLE,Referer,getActivity());
            }
        });
    }
}
