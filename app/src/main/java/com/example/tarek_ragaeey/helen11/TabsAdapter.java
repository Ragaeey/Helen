package com.example.tarek_ragaeey.helen11;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Tarek-Ragaeey on 4/28/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Books", "Server"};
    private Context mContext;
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BooksFragment();
        }
        else{
            return new ServerFragment();
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
