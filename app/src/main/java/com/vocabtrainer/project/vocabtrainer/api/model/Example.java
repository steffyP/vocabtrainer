package com.vocabtrainer.project.vocabtrainer.api.model;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class Example {
    private String text;
    private List<Translation> translations;

    public String getText() {
        return text;
    }

    public List<Translation> getTranslations() {
        return translations;
    }
}
