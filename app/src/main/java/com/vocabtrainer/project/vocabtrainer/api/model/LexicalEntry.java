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

    public String toString(){
        String res = "language: " + language + "\n";
        res += "category: " + lexicalCategory+ "\n";
        res += "text: " + text + "\n";
        res += "entries:\n";

        if(entries != null){
            for(Entry e : entries){
                res += e.toString()+ "\n";
            }
        }
        return res;
    }
}
