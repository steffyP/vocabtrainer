package com.vocabtrainer.project.vocabtrainer.database;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.HashMap;

/**
 * Created by steffy on 08/12/2017.
 */

public class VocabContract {


    public static final String CONTENT_AUTHORITY = "com.vocabtrainer.project.provider";
    public static final Uri BASE_URI = Uri.parse("content://com.vocabtrainer.project.provider");

  /*  private static final HashMap<String, String> categoriesProjectionMap;

    private static final HashMap<String, String> wordProjectionMap;

    static {
        //Setup projection maps
        categoriesProjectionMap = new HashMap<String, String>();
        categoriesProjectionMap.put(Category._ID, Category.TABLE_NAME + "." + Category._ID);
        categoriesProjectionMap.put(Category.COLUMN_NAME, Category.TABLE_NAME + "." + Category.COLUMN_NAME);

        wordProjectionMap = new HashMap<String, String>();
        wordProjectionMap.put(Word._ID, Word.TABLE_NAME + "." + Word._ID);
        wordProjectionMap.put(Word.COLUMN_GERMAN, Word.TABLE_NAME + "." + Word.COLUMN_GERMAN);
        wordProjectionMap.put(Word.COLUMN_ENGLISH, Word.TABLE_NAME + "." + Word.COLUMN_ENGLISH);

        wordProjectionMap.put(Word.COLUMN_CATEGORY, Word.TABLE_NAME + "." + Word.COLUMN_CATEGORY);
        wordProjectionMap.put("category_name", Category.COLUMN_NAME + " AS " + "category_name");
    }*/

    public static class Word implements BaseColumns {
        public static final String PATH = "words";
        public static final String TABLE_NAME = "word";
        public static final String COLUMN_GERMAN = "german";
        public static final String COLUMN_ENGLISH = "english";
        public static final String COLUMN_CATEGORY = "category";


        /**
         * Matches: /words/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH).build();
        }

        /**
         * Matches: /words/[_id]/
         */
        public static Uri buildItemUri(long id) {
            return BASE_URI.buildUpon().appendPath(PATH).appendPath(Long.toString(id)).build();
        }


    }

    public static class Category implements BaseColumns {
        public static final String PATH = "categories";
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME = "name";


        /**
         * Matches: /categories/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH).build();
        }

        /**
         * Matches: /categories/[_id]/
         */
        public static Uri buildItemUri(long id) {
            return BASE_URI.buildUpon().appendPath(PATH).appendPath(Long.toString(id)).build();
        }


    }

    private VocabContract() {
    }


}
