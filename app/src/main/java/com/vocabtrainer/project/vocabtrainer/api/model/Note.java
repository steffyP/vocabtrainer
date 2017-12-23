package com.vocabtrainer.project.vocabtrainer.api.model;

/**
 * Created by steffy on 17/12/2017.
 */

public class Note {

    private String text;
    private String type;

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        String res = "";
        res += text != null ? text : "";
        res += type != null ? " ("+type+")" : "";
        return  res;
    }
}
