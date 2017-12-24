package com.vocabtrainer.project.vocabtrainer;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stefanie on 24.12.17.
 */

class WordDetector implements Detector.Processor<TextBlock> {
    private final Callback callback;

    public interface Callback {
        void reportDetectedWords(List<String> words);
    }
    public WordDetector(Callback callback){
        this.callback = callback;
    }
    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        List<String> words = new ArrayList<>();

        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);

            String values = item.getValue();
            if(values.contains("\n")){
                values = values.replaceAll("\n", " ");
            }
            Collections.addAll(words, values.split(" "));

        }
        if(callback != null){
            callback.reportDetectedWords(words);
        }
    }
}
