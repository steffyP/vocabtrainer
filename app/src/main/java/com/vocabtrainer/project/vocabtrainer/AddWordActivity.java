package com.vocabtrainer.project.vocabtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddWordActivity extends AppCompatActivity {

    public static final String INPUT_ENGLISH_WORD = "input_english_word";
    private static final String INPUT_WORD = "input_word";


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(inputOne.getText().length() > 0){
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
               if(s.length() > 0){
                   lookup.setVisibility(View.VISIBLE);
               } else {
                   lookup.setVisibility(View.GONE);
               }
            }
        });
      //  spinner.setAdapter();
    }

    public void saveEntry(View view) {
        // TODO
    }

    @Override
    public void onBackPressed() {
        // TODO handle back if user has entered word but not saved
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

    public void swapLanguage(View view){
        if(isEnglish(languageOne)){
            languageTwo.setText(getString(R.string.language_en));
            languageOne.setText(getString(R.string.language_de));
        } else {
            languageOne.setText(getString(R.string.language_en));
            languageTwo.setText(getString(R.string.language_de));
        }
    }

    private boolean isEnglish(TextView textView) {
        return textView.getText().equals(getString(R.string.language_en));
    }

    public void lookupOxfordDictionary(View view){
        if(!TextUtils.isEmpty(inputOne.getText())) {
            Intent intent = new Intent(this, WordExplanationActivity.class);
            intent.putExtra(INPUT_ENGLISH_WORD, isEnglish(inputOne));
            intent.putExtra(INPUT_WORD, inputOne.getText());
        }
    }
}
