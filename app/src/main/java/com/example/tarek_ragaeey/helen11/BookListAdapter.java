package com.example.tarek_ragaeey.helen11;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<JSONObject> {
    private String imageURL =  "";
    private String title = "";
    BookListAdapter (Context context, List<JSONObject> Array) {
        super(context,0,Array);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject bookobj = getItem(position);
        ImageView poster;
        TextView booktitle;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }
        try {
            imageURL = bookobj.getString("image");
            title = bookobj.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        poster = (ImageView) convertView.findViewById(R.id.poster_list_item_image);
        booktitle = (TextView) convertView.findViewById(R.id.download_list_item_link);
        Picasso.with(getContext()).load(imageURL).into(poster);
        booktitle.setText(title);
        return convertView;
    }
}
