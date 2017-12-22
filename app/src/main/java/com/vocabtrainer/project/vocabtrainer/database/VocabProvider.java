package com.vocabtrainer.project.vocabtrainer.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by steffy on 08/12/2017.
 */

public class VocabProvider extends ContentProvider {
    private static final String TAG = VocabProvider.class.getSimpleName();
    private SQLiteOpenHelper database;

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int WORDS = 1000;
    private static final int WORD_FOR_ID = 1001;
    private static final int CATEGORIES = 2000;
    private static final int CATEGORY_FOR_ID = 2001;
    private static final String[] projectionIn = {VocabContract.Word.TABLE_NAME + "." + VocabContract.Word._ID + " as " + VocabContract.Word._ID,
            VocabContract.Category.TABLE_NAME + "." + VocabContract.Category._ID + " as categoryId",
            VocabContract.Word.COLUMN_GERMAN,
            VocabContract.Word.COLUMN_ENGLISH,
            VocabContract.Category.COLUMN_NAME};

    static {

        sUriMatcher.addURI(VocabContract.CONTENT_AUTHORITY, VocabContract.Word.PATH, WORDS);

        sUriMatcher.addURI(VocabContract.CONTENT_AUTHORITY, VocabContract.Word.PATH + "/#", WORD_FOR_ID);

        sUriMatcher.addURI(VocabContract.CONTENT_AUTHORITY, VocabContract.Category.PATH, CATEGORIES);

        sUriMatcher.addURI(VocabContract.CONTENT_AUTHORITY, VocabContract.Category.PATH + "/#", CATEGORY_FOR_ID);
    }


    @Override
    public boolean onCreate() {
        database = new VocabDatabaseHelper(getContext());
        return true;
    }

    // Implements ContentProvider.query()
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {


        Cursor returnCursor;
        SQLiteDatabase db = database.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for table 3 are shown.
         */
        switch (sUriMatcher.match(uri)) {

            // If the incoming URI was for all of table3
            case WORDS:
                // will include the category, so we need to merge this one:
                builder.setTables(VocabContract.Word.TABLE_NAME + " LEFT OUTER JOIN " +
                        VocabContract.Category.TABLE_NAME + " ON ( " +
                        VocabContract.Word.COLUMN_CATEGORY + "=" + VocabContract.Category.TABLE_NAME + "." + VocabContract.Category._ID
                        + ")");

                returnCursor = builder.query(
                        db,
                        projectionIn,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case CATEGORIES:
                returnCursor = db.query(
                        VocabContract.Category.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CATEGORY_FOR_ID:
                // will include all words for this category, so we need to merge this:
                // will include the category, so we need to merge this one:
                builder.setTables(VocabContract.Word.TABLE_NAME + " JOIN " +
                        VocabContract.Category.TABLE_NAME + " ON ( " +
                        VocabContract.Word.COLUMN_CATEGORY + "=" + VocabContract.Category.TABLE_NAME + "." + VocabContract.Category._ID
                        + ")");

                if (selection == null) {
                    selection = "";
                } else {
                    selection += " AND ";
                }
                selection += VocabContract.Category.TABLE_NAME + "." + VocabContract.Category._ID + " = ?";

                if (selectionArgs == null) {
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                } else {
                    String[] selectionArgsTmp = new String[selectionArgs.length + 1];
                    for (int i = 0; i < selectionArgs.length; i++) {
                        selectionArgsTmp[i] = selectionArgs[i];
                    }
                    selectionArgsTmp[selectionArgs.length] = uri.getLastPathSegment();
                    selectionArgs = selectionArgsTmp;
                }

                returnCursor = builder.query(
                        db,
                        projectionIn,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);

        }

        Context context = getContext();
        if (context != null) {
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        Uri returnUri;
        long insertedId;
        switch (sUriMatcher.match(uri)) {
            case WORDS:
                insertedId = db.insert(
                        VocabContract.Word.TABLE_NAME,
                        null,
                        values
                );
                returnUri = VocabContract.Word.buildItemUri(insertedId);
                break;

            case CATEGORIES:
                insertedId = db.insert(
                        VocabContract.Category.TABLE_NAME,
                        null,
                        values
                );
                returnUri = VocabContract.Category.buildItemUri(insertedId);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted;


        switch (sUriMatcher.match(uri)) {
            case WORD_FOR_ID:
                rowsDeleted = db.delete(
                        VocabContract.Word.TABLE_NAME,
                        "_id = ?",
                        new String[]{uri.getLastPathSegment()}
                );

                break;

            case CATEGORY_FOR_ID:
                rowsDeleted = db.delete(
                        VocabContract.Category.TABLE_NAME,
                        "_id = ?",
                        new String[]{uri.getLastPathSegment()}
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        if (rowsDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int rowsUpdated;


        switch (sUriMatcher.match(uri)) {
            case WORD_FOR_ID:
                rowsUpdated = db.update(
                        VocabContract.Word.TABLE_NAME,
                        values,
                        "_ID = ?",
                        new String[]{uri.getLastPathSegment()}
                );

                break;

            case CATEGORY_FOR_ID:
                rowsUpdated = db.update(
                        VocabContract.Category.TABLE_NAME,
                        values,
                        "_ID = ?",
                        new String[]{uri.getLastPathSegment()}
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        if (rowsUpdated != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return rowsUpdated;
    }
}
