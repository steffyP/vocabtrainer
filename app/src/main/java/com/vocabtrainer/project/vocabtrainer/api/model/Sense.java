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


    public String toHtmlString() {
        String res = "";
        if (translations != null) {
            for (Translation t : translations) {
                res += "<b>" + t.getText() + " (" + t.getLanguage() + ")</b><br/>";
                if (notes != null) {
                    for (Note n : notes) {
                        res += n.toString() + "<p/>";
                    }
                }
            }
        }
        res += "<p/>";

        if (examples != null) {
            for (Example e : examples) {
                res += e.getText() + ":<br/>";
                if (e.getTranslations() != null) {
                    for (Translation translation : e.getTranslations()) {
                        res += translation.getText() + "<p/>";
                    }
                }
                res += "<br/>";
            }
        }
        return res;
    }
}
