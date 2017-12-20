package com.vocabtrainer.project.vocabtrainer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vocabtrainer.project.vocabtrainer.adapter.CategoryAdapter;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FloatingActionMenu.OnMenuToggleListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CATEGORY_LOADER = 112233;
    private static final int ANIMATION_DURATION = 50;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.floating_menu)
    FloatingActionMenu floatingMenu;

    @BindView(R.id.main_content)
    View mainContent;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.span_count)));

        categoryAdapter = new CategoryAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoryAdapter);

        getSupportLoaderManager().initLoader(CATEGORY_LOADER, null, this);

        floatingMenu.setOnMenuToggleListener(this);

        nestedScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingMenu.isOpened()) {
                    setBackgroundInactive(false);
                }
            }
        });

        // this is not working for the coordinatorlayout floatingMenu.setClosedOnTouchOutside(true);

        // TODO nice to have: close menu when clicking in background
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(this, VocabContract.Category.buildDirUri(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);
        categoryAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryAdapter.swapData(null);
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
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (floatingMenu.isOpened()) {
            floatingMenu.close(true);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (floatingMenu.isOpened()) {
            floatingMenu.close(false);
        }
    }
}
