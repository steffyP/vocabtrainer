package com.vocabtrainer.project.vocabtrainer.vocab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.OxfordDefinitionActivity;
import com.vocabtrainer.project.vocabtrainer.R;

import java.util.Random;

/**
 * Created by stefanie on 23.12.17.
 */

public class VocabFragment extends Fragment {


    public static final String VOCAB_ENGLISH = "vocab_english";
    public static final String VOCAB_GERMAN = "vocab_german";
    private static final String CARD_COLOR = "card_color";
    private static final String CARD_COLOR_DARK = "card_color_dark";


    private TextView vocab;

    private String englishWord;
    private String germanWord;

    private View lookup;

    private int color, darkColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_vocab, container, false);

        englishWord = getArguments().getString(VOCAB_ENGLISH, "");
        germanWord = getArguments().getString(VOCAB_GERMAN, "");

        vocab = rootView.findViewById(R.id.tv_vocab);
        lookup = rootView.findViewById(R.id.lookup);

        if (savedInstanceState != null) {
            color = savedInstanceState.getInt(CARD_COLOR);
            darkColor = savedInstanceState.getInt(CARD_COLOR_DARK);
        } else {
            Pair<Integer, Integer> pair = getRandomColorPair();
            color = pair.first;
            darkColor = pair.second;
        }

        vocab.setBackgroundColor(getResources().getColor(getColor()));
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
                    vocab.setBackgroundColor(getResources().getColor(getColorDark()));
                } else {
                    vocab.setText(englishWord);
                    vocab.setBackgroundColor(getResources().getColor(getColor()));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CARD_COLOR, getColor());
        outState.putInt(CARD_COLOR_DARK, getColorDark());
    }

    private int getColor() {
        return color;
    }

    private int getColorDark() {
        return darkColor;
    }

    private Pair<Integer, Integer> getRandomColorPair() {
        Random random = new Random();
        int selected = random.nextInt(5);
        switch (selected) {
            case 0:
                return new Pair<Integer, Integer>(R.color.cardColor1, R.color.cardColor1Dark);
            case 1:
                return new Pair<Integer, Integer>(R.color.cardColor2, R.color.cardColor2Dark);
            case 2:
                return new Pair<Integer, Integer>(R.color.cardColor3, R.color.cardColor3Dark);
            case 3:
                return new Pair<Integer, Integer>(R.color.cardColor4, R.color.cardColor4Dark);
            case 4:
                return new Pair<Integer, Integer>(R.color.cardColor5, R.color.cardColor5Dark);
            default:
                return new Pair<Integer, Integer>(R.color.colorPrimary, R.color.colorPrimaryDark);
        }
    }


}
