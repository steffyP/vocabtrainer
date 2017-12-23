package com.vocabtrainer.project.vocabtrainer.vocab;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.OxfordDefinitionActivity;
import com.vocabtrainer.project.vocabtrainer.R;

/**
 * Created by stefanie on 23.12.17.
 */

public class VocabFragment extends Fragment {


    public static final String VOCAB_ENGLISH = "vocab_english";
    public static final String VOCAB_GERMAN = "vocab_german";


    private TextView vocab;

    private String englishWord;
    private String germanWord;

    private View lookup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_vocab, container, false);

        englishWord = getArguments().getString(VOCAB_ENGLISH, "");
        germanWord = getArguments().getString(VOCAB_GERMAN, "");

        vocab = rootView.findViewById(R.id.tv_vocab);
        lookup = rootView.findViewById(R.id.lookup);

        vocab.setText(englishWord);

        lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocabFragment.this.getContext(), OxfordDefinitionActivity.class);
                intent.putExtra(OxfordDefinitionActivity.INPUT_ENGLISH_WORD, vocab.getText().equals(englishWord));
                intent.putExtra(OxfordDefinitionActivity.INPUT_WORD, vocab.getText());
                startActivity(intent);
            }
        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (vocab.getText().equals(englishWord)) {
                    vocab.setText(germanWord);
                    vocab.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    vocab.setText(englishWord);
                    vocab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        return rootView;
    }


}
