package com.vocabtrainer.project.vocabtrainer.api;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vocabtrainer.project.vocabtrainer.api.model.OxfordEntry;


/**
 * Created by steffy on 17/12/2017.
 */

/**
 *
 *  AsyncTask to call the Oxford API, query in background and get the result over the callback
 *
 */
public class OxfordRequestTask extends AsyncTask<String, Void, OxfordEntry> {
    private static final String TAG = OxfordRequestTask.class.getSimpleName();
    private final RequestCallback callback;

    public interface RequestCallback {
        void receive(OxfordEntry entry);
    }

    public OxfordRequestTask(RequestCallback requestCallback) {
        this.callback = requestCallback;
    }

    @Override
    protected OxfordEntry doInBackground(String... strings) {
        String language = strings[0];
        String word = strings[1];

        String result = "";
        if (language.equals("en")) {
            result = OxfordApiRequest.searchTranslationForEnglishWord(word);
        } else if (language.equals("de")) {
            result = OxfordApiRequest.searchTranslationForGermanWord(word);
        }
        if (! TextUtils.isEmpty(result)) {
            try {
                Gson gson = new Gson();
                OxfordEntry entry = gson.fromJson(result, OxfordEntry.class);
                return entry;
            }catch(JsonSyntaxException jsonEx){
                Log.e(TAG, "received string does not conform to the expected json");
                jsonEx.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(OxfordEntry oxfordEntry) {
        if (callback != null) {
            callback.receive(oxfordEntry);
        }
    }
}
