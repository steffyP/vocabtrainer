package com.vocabtrainer.project.vocabtrainer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.vocabtrainer.project.vocabtrainer.adapter.WordAdapter;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListWordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private static final int WORDS_LOADER = 998877;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;


    @BindView(R.id.empty_view)
    View emptyTextView;

    private long categoryId;
    private String categoryName;
    private WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_word);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, 0);

        getSupportActionBar().setTitle(categoryName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordAdapter = new WordAdapter(this);
        recyclerView.setAdapter(wordAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.training){
            Intent intent = new Intent(this, TrainingActivity.class);
            intent.putExtra(ListWordActivity.EXTRA_CATEGORY_ID, categoryId);
            intent.putExtra(ListWordActivity.EXTRA_CATEGORY_NAME, categoryName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_training, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(WORDS_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(this, VocabContract.Category.buildItemUri(categoryId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);

        if (data != null && data.getCount() > 0) {
            wordAdapter.swapData(data);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
