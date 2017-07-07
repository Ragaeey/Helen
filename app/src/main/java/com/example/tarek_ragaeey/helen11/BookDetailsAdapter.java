package com.example.tarek_ragaeey.helen11;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class BookDetailsAdapter extends ArrayAdapter<JSONObject> {
    private String review =  "";
    private String reviewAuthor = "";
    private String reviewDate = "";
    BookDetailsAdapter (Context context, List<JSONObject> Array) {
        super(context,0,Array);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject reviewobj = getItem(position);
        TextView Review,ReviewAuthor,ReviewDate;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        try {
            review = reviewobj.getString("review");
            reviewAuthor = reviewobj.getString("user");
            reviewDate = reviewobj.getString("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Review = (TextView) convertView.findViewById(R.id.review_list_item_data);
        Review.setText(review);

        ReviewAuthor = (TextView) convertView.findViewById(R.id.review_list_item_author);
        ReviewAuthor.setText(reviewAuthor);

        ReviewDate = (TextView) convertView.findViewById(R.id.review_release_date);
        ReviewDate.setText(reviewDate);

        return convertView;
    }
}
