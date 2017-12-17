package com.vocabtrainer.project.vocabtrainer.api;


import android.util.Log;

import com.google.gson.Gson;
import com.vocabtrainer.project.vocabtrainer.BuildConfig;
import com.vocabtrainer.project.vocabtrainer.api.model.OxfordEntry;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by steffy on 26/11/2017.
 */

/**
 *  Helper class to actually use the Oxford API
 *
 */
public class OxfordApiRequest {
    private static final String TAG = OxfordApiRequest.class.getSimpleName();


    private static final String ENDPOINT = "https://od-api.oxforddictionaries.com/api/v1";
    private static final String API_KEY = BuildConfig.OXFORD_API_KEY;
    private static final String APP_ID = BuildConfig.OXFORD_APP_ID;
    private static final String TRANSLATION_API = "/entries/";

    // the api call we need:
    // /entries/{source_translation_language}/{word_id}/translations={target_translation_language}

    public static String searchTranslationForGermanWord(String germanSource) {
        String url = ENDPOINT + TRANSLATION_API + "de/" + germanSource.toLowerCase() + "/translations=en";

        return searchApi(url);

    }

    public static String searchTranslationForEnglishWord(String englishSource) {
        String url = ENDPOINT + TRANSLATION_API + "en/" + englishSource.toLowerCase() + "/translations=de";

        return searchApi(url);

    }

    private static String searchApi(String url) {
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
           /* required headers:
                    "Accept": "application/json",
                    "app_id": "APP_ID",
                    "app_key": "APP_KEY"*/
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("app_id", APP_ID);
            connection.addRequestProperty("app_key", API_KEY);

            connection.connect();

            int responseCode = connection.getResponseCode();

            // according docu this may indicates error messages:

            // 400  any of target languages is unknown
            // 404  no entry is found matching supplied source_lang and id and/or that entry has no senses with translations in the target language(s).
            // 500  Internal Error. An error occurred while processing the data.


            // we treat everything as an error, that does not give a 200 OK
            if (responseCode != 200) {
                Log.e(TAG, "received unexpected response: " + responseCode);
                return null;
            }
            InputStream inputStream = connection.getInputStream();
            String result = readInputStreamToString(inputStream);

            inputStream.close();
            connection.disconnect();

            return result;
        } catch (MalformedURLException e) {
            Log.e(TAG, "error malformed url");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IO exception");
            e.printStackTrace();
            return null;
        }
    }

    private static String readInputStreamToString(InputStream is) throws IOException {
        String result = null;
        StringBuffer sb = new StringBuffer();


        is = new BufferedInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        result = sb.toString();


        return result;
    }

}
