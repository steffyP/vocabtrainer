package com.vocabtrainer.project.vocabtrainer.api.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by steffy on 17/12/2017.
 */

public class OxfordEntry {


    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }
}
