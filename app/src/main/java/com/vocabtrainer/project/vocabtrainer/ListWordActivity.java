package com.vocabtrainer.project.vocabtrainer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionMenu;
import com.vocabtrainer.project.vocabtrainer.adapter.WordAdapter;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vocabtrainer.project.vocabtrainer.AddWordActivity.EXTRA_ITEM_CATEGORY;
import static com.vocabtrainer.project.vocabtrainer.MainActivity.ANIMATION_DURATION;

public class ListWordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FloatingActionMenu.OnMenuToggleListener {
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

    @BindView(R.id.main_content)
    NestedScrollView mainContent;

    @BindView(R.id.floating_menu)
    FloatingActionMenu floatingMenu;


    private long categoryId;
    private String categoryName;
    private WordAdapter wordAdapter;
    private Menu menu;

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

        floatingMenu.setOnMenuToggleListener(this);

        recyclerView.addOnItemTouchListener( new RecyclerView.OnItemTouchListener(){

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (floatingMenu.isOpened()) {
                    floatingMenu.close(false);
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // this is required for the margin on the bottom of the view
        mainContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (floatingMenu.isOpened()) {
                    floatingMenu.close(false);
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.training){
            Intent intent = new Intent(this, TrainingActivity.class);
            intent.putExtra(ListWordActivity.EXTRA_CATEGORY_ID, categoryId);
            intent.putExtra(ListWordActivity.EXTRA_CATEGORY_NAME, categoryName);
            startActivity(intent);
            return true;
        } if(item.getItemId() == android.R.id.home){
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_training, menu);
        this.menu = menu;
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(WORDS_LOADER, null, this);
        if(floatingMenu.isOpened()){
            floatingMenu.close(false);
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

        if (data != null && data.getCount() > 0) {
            wordAdapter.swapData(data);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if(menu != null){
                MenuItem item = menu.findItem(R.id.training);
                if(item != null) item.setVisible(false);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMenuToggle(boolean isOpen) {
        setBackgroundInactive(isOpen);
    }


    private void setBackgroundInactive(boolean inactive) {
        final float targetAlpha = !inactive ? 1f : 0.3f;
        final float startAlpha = !inactive ? 0.3f : 1f;

        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, targetAlpha);
        alphaAnimation.setDuration(ANIMATION_DURATION);
        alphaAnimation.setFillAfter(true);
        mainContent.startAnimation(alphaAnimation);

    }

    public void openAddWordActivity(View view) {
        Intent intent = new Intent(this, AddWordActivity.class);
        intent.putExtra(EXTRA_ITEM_CATEGORY, categoryId);
        startActivity(intent);
    }
    public void openScanActivity(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(EXTRA_ITEM_CATEGORY, categoryId);

        startActivity(intent);
    }


}
