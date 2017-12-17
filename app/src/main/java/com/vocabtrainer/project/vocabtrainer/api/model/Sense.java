package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class Sense {
    private String id;
    private List<Example> examples;
    private List<Translation> translations;
    private List<Note> notes;

    public String getId() {
        return id;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
