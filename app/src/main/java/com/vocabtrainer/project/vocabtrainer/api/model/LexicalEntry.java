package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class LexicalEntry {

    private String language;
    private String lexicalCategory;
    private String text;
    private List<Entry> entries;

    public String getLanguage() {
        return language;
    }

    public String getLexicalCategory() {
        return lexicalCategory;
    }

    public String getText() {
        return text;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
