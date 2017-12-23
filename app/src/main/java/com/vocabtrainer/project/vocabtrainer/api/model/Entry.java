package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class Entry {

    private List<GrammaticalFeature> grammaticalFeatures;

    private List<Sense> senses;

    public List<GrammaticalFeature> getGrammaticalFeatures() {
        return grammaticalFeatures;
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public String toString() {
        String res = "senses:\n";
        if (senses != null) {
            for (Sense s : senses) {
                res += s.toString();
            }
        }

        res += "\ngrammaticalFeatures:\n";
        if (grammaticalFeatures != null) {
            for (GrammaticalFeature g : grammaticalFeatures) {
                res += g.toString()+ "\n";
            }
        }
        return res;
    }
}
