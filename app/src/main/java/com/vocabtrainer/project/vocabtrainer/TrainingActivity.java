package com.vocabtrainer.project.vocabtrainer;

import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.vocabtrainer.project.vocabtrainer.database.VocabContract;
import com.vocabtrainer.project.vocabtrainer.vocab.VocabPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vocabtrainer.project.vocabtrainer.ListWordActivity.EXTRA_CATEGORY_ID;

/**
 * Created by stefanie on 23.12.17.
 */

public class TrainingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORDS_LOADER = 445566;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_view)
    View emptyView;

    private PagerAdapter pagerAdapter;
    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, 0);
        getSupportLoaderManager().initLoader(WORDS_LOADER, null, this);


        pager.setPadding(100, 0, 100, 0);
        pager.setClipToPadding(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(this, VocabContract.Category.buildItemUri(categoryId), null, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);
        if(data != null && data.getCount() > 0) {
            pagerAdapter = new VocabPagerAdapter(getSupportFragmentManager(), data);
            pager.setAdapter(pagerAdapter);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
