package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class Result {

    private String id;
    private String language;
    private List<LexicalEntry> lexicalEntries;
    private String word;
    private String type;

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public List<LexicalEntry> getLexicalEntries() {
        return lexicalEntries;
    }

    public String getWord() {
        return word;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        String res = "";

        res += "id: " + id + "\n";
        res += "language: " + language + "\n";
        res += "word: " + word + "\n";
        res += "type: " + type + "\n";
        res += "lexicalEntries: " + "\n";

        if(lexicalEntries != null){
            for(LexicalEntry e : lexicalEntries){
                res += e.toString() + "\n";
            }
        }
        return res;
    }
}
