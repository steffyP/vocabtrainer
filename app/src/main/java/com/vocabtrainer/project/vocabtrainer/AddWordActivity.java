package com.vocabtrainer.project.vocabtrainer;

import android.content.ContentValues;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
    EditText inputGerman;

    @BindView(R.id.input_two)
    EditText inputEnglish;

    @BindView(R.id.language_one)
    TextView languageOne;

    @BindView(R.id.language_two)
    TextView languageTwo;

    @BindView(R.id.lookup_word_de)
    View lookupDe;

    @BindView(R.id.lookup_word_en)
    View lookupEn;

    @BindView(R.id.main_content)
    View mainContent;

    private boolean isNew;
    private long itemId;
    private String englishWord;
    private String germanWord;
    private long categorySaved;
    private Menu menu;

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
        } else {
            isNew = true;
            // this maybe set if the scan word view has been used
            germanWord = getIntent().getStringExtra(EXTRA_ITEM_GERMAN);
            categorySaved = getIntent().getLongExtra(EXTRA_ITEM_CATEGORY, -1);

        }
        if (!isNew) {
            getSupportActionBar().setTitle(getString(R.string.update_word));
        }


        //  spinner.setAdapter();
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        if (!TextUtils.isEmpty(englishWord)) {
            inputEnglish.setText(englishWord);
        }
        if (!TextUtils.isEmpty(germanWord)) {
            inputGerman.setText(germanWord);
        }
        if (categorySaved != -1) {
            spinner.setSelection((int) categorySaved - 1);
        }

        addLookupLogic();
    }

    private void addLookupLogic() {
        if (inputGerman.getText().length() > 0) {
            lookupDe.setVisibility(View.VISIBLE);
        } else {
            lookupDe.setVisibility(View.GONE);
        }

        if(inputEnglish.getText().length() > 0){
            lookupEn.setVisibility(View.VISIBLE);
        } else {
            lookupEn.setVisibility(View.GONE);
        }

        inputGerman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lookupDe.setVisibility(View.VISIBLE);
                } else {
                    lookupDe.setVisibility(View.GONE);
                }
            }
        });

        inputEnglish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lookupEn.setVisibility(View.VISIBLE);
                } else {
                    lookupEn.setVisibility(View.GONE);
                }
            }
        });

        inputEnglish.setOnEditorActionListener(new DoneOnEditorActionListener());
        inputGerman.setOnEditorActionListener(new DoneOnEditorActionListener());


    }

    public void saveEntry(View view) {
        if (TextUtils.isEmpty(inputGerman.getText()) || TextUtils.isEmpty(inputEnglish.getText())) {
            Snackbar.make(mainContent, getString(R.string.fill_fields), Snackbar.LENGTH_SHORT).show();
        } else {
            String german;
            String english;

            german = inputGerman.getText().toString().trim();
            english = inputEnglish.getText().toString().trim();

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
        inputGerman.setText(null);
        inputEnglish.setText(null);
        spinner.setSelection(0);

        isNew = true;
        itemId = -1;
        categorySaved = -1;
        englishWord = "";
        germanWord = "";

        if(menu != null){
           MenuItem item =  menu.findItem(R.id.delete_word);
           if(item != null) item.setVisible(false);
        }
        getSupportActionBar().setTitle(getString(R.string.add_word));

    }

    private void showSuccessful() {
        Snackbar.make(mainContent, R.string.inserted_word, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if ((isNew && (!TextUtils.isEmpty(inputGerman.getText()) || !TextUtils.isEmpty(inputEnglish.getText())))
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

        if (!inputGerman.getText().toString().equals(germanWord)) return true;
        if (!inputEnglish.getText().toString().equals(englishWord)) return true;

        if (spinner.getSelectedItemPosition() != (int) categorySaved - 1) return true;

        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNew) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.add_word, menu);
            this.menu = menu;
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
        String german = inputGerman.getText().toString();
        String english = inputEnglish.getText().toString();
        inputGerman.setText(english);
        inputEnglish.setText(german);
    }


    public void lookupOxfordDictionaryDe(View view) {
        if (!TextUtils.isEmpty(inputGerman.getText())) {
           startOxfordApi(false, inputGerman.getText().toString());
        }
    }

    private void startOxfordApi(boolean isEnglish, String query){
        Intent intent = new Intent(this, OxfordDefinitionActivity.class);
        intent.putExtra(OxfordDefinitionActivity.INPUT_ENGLISH_WORD, isEnglish);
        intent.putExtra(OxfordDefinitionActivity.INPUT_WORD, query);
        startActivity(intent);
    }

    public void lookupOxfordDictionaryEn(View view){
        if (!TextUtils.isEmpty(inputEnglish.getText())) {
            startOxfordApi(true, inputEnglish.getText().toString());
        }
    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }
}
