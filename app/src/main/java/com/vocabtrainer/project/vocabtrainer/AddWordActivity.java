package com.vocabtrainer.project.vocabtrainer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddWordActivity extends AppCompatActivity {

    public static final String INPUT_ENGLISH_WORD = "input_english_word";
    private static final String INPUT_WORD = "input_word";
    private static final String EXTRA_ITEM_ID = "extra_item_id";
    private static final String EXTRA_ITEM_ENGLSH = "extra_item_english";
    private static final String EXTRA_ITEM_GERMAN = "extra_item_german";
    private static final String EXTRA_ITEM_CATEGORY = "extra_item_category";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drop_down)
    Spinner spinner;

    @BindView(R.id.input_one)
    EditText inputOne;

    @BindView(R.id.input_two)
    EditText inputTwo;

    @BindView(R.id.language_one)
    TextView languageOne;

    @BindView(R.id.language_two)
    TextView languageTwo;

    @BindView(R.id.lookup_word)
    View lookup;

    @BindView(R.id.main_content)
    View mainContent;

    private boolean isNew;
    private long itemId;
    private String englishWord;
    private String germanWord;
    private int categorySaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra(EXTRA_ITEM_ID)) {
            isNew = false;
            itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
            englishWord = getIntent().getStringExtra(EXTRA_ITEM_ENGLSH);
            germanWord = getIntent().getStringExtra(EXTRA_ITEM_GERMAN);
            categorySaved = getIntent().getIntExtra(EXTRA_ITEM_CATEGORY, -1);
        } else isNew = true;
        if (inputOne.getText().length() > 0) {
            lookup.setVisibility(View.VISIBLE);
        } else {
            lookup.setVisibility(View.GONE);
        }

        inputOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lookup.setVisibility(View.VISIBLE);
                } else {
                    lookup.setVisibility(View.GONE);
                }
            }
        });
        //  spinner.setAdapter();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.categories));
        spinner.setAdapter(adapter);


        if (!TextUtils.isEmpty(germanWord)) {
            inputTwo.setText(germanWord);
        }
        if (!TextUtils.isEmpty(englishWord)) {
            inputOne.setText(englishWord);
        }
        if (categorySaved != -1) {
            spinner.setSelection(categorySaved - 1);
        }
    }

    public void saveEntry(View view) {
        // TODO
        if (TextUtils.isEmpty(inputOne.getText()) || TextUtils.isEmpty(inputTwo.getText())) {
            Snackbar.make(mainContent, getString(R.string.fill_fields), Snackbar.LENGTH_SHORT).show();
        } else {
            String german;
            String english;
            if (isEnglish()) {
                english = inputOne.getText().toString().trim();
                german = inputTwo.getText().toString().trim();
            } else {
                german = inputOne.getText().toString().trim();
                english = inputTwo.getText().toString().trim();
            }
            int category = spinner.getSelectedItemPosition() + 1;

            ContentValues cv = new ContentValues();
            cv.put(VocabContract.Word.COLUMN_ENGLISH, english);
            cv.put(VocabContract.Word.COLUMN_GERMAN, german);
            cv.put(VocabContract.Word.COLUMN_CATEGORY, category);
            if (isNew) {
                getContentResolver().insert(VocabContract.Word.buildDirUri(), cv);
            } else {
                getContentResolver().update(VocabContract.Word.buildItemUri(itemId), cv, null, null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(inputOne.getText()) || !TextUtils.isEmpty(inputTwo.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.unsaved_changes));
            builder.setMessage(getString(R.string.changes_will_be_lost));

            builder.setPositiveButton(getString(R.string.save_now), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveEntry(null);
                }
            });
            builder.setNegativeButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddWordActivity.super.onBackPressed();
                }
            });
            builder.show();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void swapLanguage(View view) {
        if (isEnglish()) {
            languageTwo.setText(getString(R.string.language_en));
            languageOne.setText(getString(R.string.language_de));
        } else {
            languageOne.setText(getString(R.string.language_en));
            languageTwo.setText(getString(R.string.language_de));
        }
    }

    private boolean isEnglish() {
        return inputOne.getText().equals(getString(R.string.language_en));
    }

    public void lookupOxfordDictionary(View view) {
        if (!TextUtils.isEmpty(inputOne.getText())) {
            Intent intent = new Intent(this, WordExplanationActivity.class);
            intent.putExtra(INPUT_ENGLISH_WORD, isEnglish());
            intent.putExtra(INPUT_WORD, inputOne.getText());
            startActivity(intent);
        }
    }
}
