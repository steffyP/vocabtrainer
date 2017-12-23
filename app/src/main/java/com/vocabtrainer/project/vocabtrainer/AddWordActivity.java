package com.vocabtrainer.project.vocabtrainer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
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


    public static final String EXTRA_ITEM_ID = "extra_item_id";
    public static final String EXTRA_ITEM_ENGLSH = "extra_item_english";
    public static final String EXTRA_ITEM_GERMAN = "extra_item_german";
    public static final String EXTRA_ITEM_CATEGORY = "extra_item_category";


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
    private long categorySaved;

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
            categorySaved = getIntent().getLongExtra(EXTRA_ITEM_CATEGORY, -1);
        } else isNew = true;

        if (!isNew) {
            getSupportActionBar().setTitle(getString(R.string.update_word));
        }
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
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        if (!TextUtils.isEmpty(englishWord)) {
            inputTwo.setText(englishWord);
        }
        if (!TextUtils.isEmpty(germanWord)) {
            inputOne.setText(germanWord);
        }
        if (categorySaved != -1) {
            spinner.setSelection((int) categorySaved - 1);
        }
    }

    public void saveEntry(View view) {
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
                Uri uri = getContentResolver().insert(VocabContract.Word.buildDirUri(), cv);
                if (uri != null) {
                    showSuccessful();
                    resetInputFields();
                }
            } else {
                int update = getContentResolver().update(VocabContract.Word.buildItemUri(itemId), cv, null, null);
                if (update > 0) showSuccessful();
                finish();
            }
        }
    }

    private void resetInputFields() {
        inputOne.setText(null);
        inputTwo.setText(null);
        spinner.setSelection(0);

        isNew = true;
        itemId = -1;
        categorySaved = -1;
        englishWord = "";
        germanWord = "";

        getSupportActionBar().setTitle(getString(R.string.add_word));

    }

    private void showSuccessful() {
        Snackbar.make(mainContent, R.string.inserted_word, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if ((isNew && (!TextUtils.isEmpty(inputOne.getText()) || !TextUtils.isEmpty(inputTwo.getText())))
                || (!isNew && textHasChanged())) {

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

    private boolean textHasChanged() {
        if (isEnglish()) {
            if (!inputOne.getText().toString().equals(englishWord)) return true;
            if (!inputTwo.getText().toString().equals(germanWord)) return true;
        } else {
            if (!inputOne.getText().toString().equals(germanWord)) return true;
            if (!inputTwo.getText().toString().equals(englishWord)) return true;
        }
        if(spinner.getSelectedItemPosition() != (int) categorySaved -1 ) return true;

        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNew) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.add_word, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.delete_word:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.delete_word));
                builder.setMessage(getString(R.string.delete_word_permantely));

                builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEntry();
                        resetInputFields();
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void deleteEntry() {
        int i = getContentResolver().delete(VocabContract.Word.buildItemUri(itemId), null, null);
        if (i > 0) {
            Snackbar.make(mainContent, R.string.deleted_entry, Snackbar.LENGTH_SHORT).show();
        }
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
        return languageOne.getText().equals(getString(R.string.language_en));
    }

    public void lookupOxfordDictionary(View view) {
        if (!TextUtils.isEmpty(inputOne.getText())) {
            Intent intent = new Intent(this, OxfordDefinitionActivity.class);
            intent.putExtra(OxfordDefinitionActivity.INPUT_ENGLISH_WORD, isEnglish());
            intent.putExtra(OxfordDefinitionActivity.INPUT_WORD, inputOne.getText().toString());
            startActivity(intent);
        }
    }
}
