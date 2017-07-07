package com.example.tarek_ragaeey.helen11;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BookListActivityFragment extends Fragment {
    private BookListAdapter BooksAdapter;
    private FragmentListener flistener;
    private List<JSONObject> BookList ;
    private JSONObject BookFullData;
    private AlertDialog alertDialog;
    private String BOOK_TITLE;
    public BookListActivityFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_item_list, container, false);
        BooksAdapter = new BookListAdapter(getActivity(),new ArrayList<JSONObject>());
        ListView listView = (ListView) rootView.findViewById(R.id.Book_List_view);
        listView.setAdapter(BooksAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BookSearch searcher = new BookSearch(getActivity());
                BookFullData = searcher.getFullBookInfo(BOOK_TITLE);
                flistener.setDetailsData(BookFullData);
            }
        });
        return rootView;
    }
    public void setFragmentListner(FragmentListener fragmentListener) {
        flistener = fragmentListener;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        updateListView();
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

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void updateListView(){
        Context context = getActivity();
        if(isOnline(context)) {
            String BooksDataString = getArguments().getString("JSONObject");
            try {
                BookList = getBooksDataFromJson(BooksDataString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (BookList != null) {
                BooksAdapter.clear();
                BooksAdapter.addAll(BookList);
            }
        }else
            showDialogMsg("Please check your internet connection!");
    }

    private List<JSONObject> getBooksDataFromJson(String BooksDataString)throws JSONException {
        final String _INFO = "booksinfo";
        JSONObject booksJson = new JSONObject(BooksDataString);
        JSONArray booksArray = booksJson.getJSONArray(_INFO);
        BOOK_TITLE = booksJson.getString("query_param");
        List<JSONObject> resultStrs = new ArrayList<JSONObject>() ;
        for(int i = 0; i < booksArray.length(); i++)
        {
            JSONObject bookObj = booksArray.getJSONObject(i);
            resultStrs.add(bookObj);
        }
        return resultStrs;
    }
}
