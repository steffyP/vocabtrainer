package com.vocabtrainer.project.vocabtrainer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.api.OxfordRequestTask;
import com.vocabtrainer.project.vocabtrainer.api.model.OxfordEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OxfordDefinitionActivity extends AppCompatActivity implements OxfordRequestTask.RequestCallback {

    public static final String INPUT_ENGLISH_WORD = "input_english_word";
    public static final String INPUT_WORD = "input_word";
    private static final String TAG = OxfordDefinitionActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.no_entry_found)
    TextView noEntryFound;

    private String input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxford_definition);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input = getIntent().getStringExtra(INPUT_WORD);
        boolean isEnglish = getIntent().getBooleanExtra(INPUT_ENGLISH_WORD, false);

        getSupportActionBar().setTitle(input);
        String language;
        if(isEnglish) {
            language = "en";
        } else language = "de";

        if(hasInternet()) {
            new OxfordRequestTask(this).execute(language, input);
        } else {
            Snackbar.make(findViewById(R.id.nested_scroll_view), R.string.check_internet, Snackbar.LENGTH_INDEFINITE).show();
            receive(null);
        }
    }

    private boolean hasInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void receive(OxfordEntry entry) {
        progressBar.setVisibility(View.GONE);
        if(entry == null){
            noEntryFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        Log.d(TAG, entry.getResults().get(0).getWord());

    }
}
