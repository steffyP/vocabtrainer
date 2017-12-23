package com.vocabtrainer.project.vocabtrainer.vocab;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

/**
 * Created by stefanie on 23.12.17.
 */

public class VocabPagerAdapter extends FragmentStatePagerAdapter {
    private final Cursor cursor;

    public VocabPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        this.cursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {

        cursor.moveToPosition(position);
        String english = cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_ENGLISH));
        String german = cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_GERMAN));

        Fragment fragment = new VocabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VocabFragment.VOCAB_ENGLISH, english);
        bundle.putString(VocabFragment.VOCAB_GERMAN, german);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        if(cursor == null) return 0;
        return cursor.getCount();
    }
}
