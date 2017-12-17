package com.vocabtrainer.project.vocabtrainer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import org.junit.Test;

/**
 * Created by steffy on 16/12/2017.
 */

public class VocabProviderTest extends ProviderTestCase2<VocabProvider> {

    private static final String TAG = VocabProviderTest.class.getSimpleName();

    MockContentResolver mMockResolver;

    public VocabProviderTest() {
        super(VocabProvider.class, VocabContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testPreFilledContentWordNotEmpty(){
        Cursor cursor = mMockResolver.query(VocabContract.Word.buildDirUri(), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(72, cursor.getCount());

        cursor.moveToFirst();
        assertEquals(5, cursor.getColumnNames().length);

        cursor = mMockResolver.query(VocabContract.Word.buildDirUri(), null, "category = ?", new String[]{"4"}, null);

        assertNotNull(cursor);
        assertEquals(12, cursor.getCount());
        assertEquals(5, cursor.getColumnNames().length);

    }

    @Test
    public void testPreFilledContentCategories(){
        Cursor cursor = mMockResolver.query(VocabContract.Category.buildDirUri(), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(4, cursor.getCount());

        cursor = mMockResolver.query(VocabContract.Category.buildItemUri(1), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(18, cursor.getCount());
        assertEquals(5, cursor.getColumnNames().length);

    }

    @Test
    public void testInsertCategory(){
        ContentValues cv = new ContentValues();
        cv.put(VocabContract.Category.COLUMN_NAME, "test");

        mMockResolver.insert(VocabContract.Category.buildDirUri(), cv);
        Cursor cursor = mMockResolver.query(VocabContract.Category.buildDirUri(), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(5, cursor.getCount());


    }

    @Test
    public void testInsertWord(){
        ContentValues cv = new ContentValues();
        cv.put(VocabContract.Word.COLUMN_GERMAN, "test");
        cv.put(VocabContract.Word.COLUMN_ENGLISH, "testing");
        cv.put(VocabContract.Word.COLUMN_CATEGORY, 1);

        Uri id = mMockResolver.insert(VocabContract.Word.buildDirUri(), cv);
        assertNotNull(id);

        Cursor cursor = mMockResolver.query(VocabContract.Category.buildItemUri(1), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(19, cursor.getCount());
        assertEquals(5, cursor.getColumnNames().length);

        cursor.moveToLast();
        assertEquals("test", cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_GERMAN)));
        assertEquals("testing", cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_ENGLISH)));

    }

    @Test
    public void testUpdateWord(){
        ContentValues cv = new ContentValues();
        cv.put(VocabContract.Word.COLUMN_GERMAN, "testOne");
        cv.put(VocabContract.Word.COLUMN_ENGLISH, "testTwo");

        int rows =  mMockResolver.update(VocabContract.Word.buildItemUri(1), cv, null, null);
        assertEquals(1, rows);
        Cursor cursor = mMockResolver.query(VocabContract.Category.buildItemUri(1), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(18, cursor.getCount());
        assertEquals(5, cursor.getColumnNames().length);


        cursor.moveToFirst();
        assertEquals("testOne", cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_GERMAN)));
        assertEquals("testTwo", cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_ENGLISH)));

    }

    @Test
    public void testDeleteWord(){

        int rows =  mMockResolver.delete(VocabContract.Word.buildItemUri(1), null, null);
        assertEquals(1, rows);
        Cursor cursor = mMockResolver.query(VocabContract.Category.buildItemUri(1), null, null, null, null);

        assertNotNull(cursor);
        assertEquals(17, cursor.getCount());
        assertEquals(5, cursor.getColumnNames().length);


    }
}
