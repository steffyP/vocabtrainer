package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class Translation {
    private String language;
    private String text;

    private List<Note> notes;

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
