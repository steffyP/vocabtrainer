package com.vocabtrainer.project.vocabtrainer.api;

import com.google.gson.Gson;
import com.vocabtrainer.project.vocabtrainer.api.model.LexicalEntry;
import com.vocabtrainer.project.vocabtrainer.api.model.OxfordEntry;
import com.vocabtrainer.project.vocabtrainer.api.model.Sense;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by steffy on 17/12/2017.
 */
public class GsonModelDeserializerTest {


    @Test
    public void testDeserializeGson() throws IOException {
        Gson gson = new Gson();
        String input = readFromAssets();

        OxfordEntry oxfordEntry = gson.fromJson(input, OxfordEntry.class);

        assertEquals(1, oxfordEntry.getResults().size());
        assertEquals(2, oxfordEntry.getResults().get(0).getLexicalEntries().size());
        List<LexicalEntry> entries = oxfordEntry.getResults().get(0).getLexicalEntries();
        assertEquals(1, entries.get(0).getEntries().size());
        List<Sense> senses = entries.get(0).getEntries().get(0).getSenses();

        assertEquals("en", entries.get(0).getLanguage());
        assertEquals("change", entries.get(0).getText());

        assertEquals(7, senses.size());

        assertEquals("this last year has seen many changes", senses.get(0).getExamples().get(0).getText());
        assertEquals("das vergangene Jahr hat viele Veränderungen [mit sich] gebracht", senses.get(0).getExamples().get(0).getTranslations().get(0).getText());
        assertEquals("de", senses.get(0).getExamples().get(0).getTranslations().get(0).getLanguage());


        assertEquals(2, senses.get(0).getExamples().get(2).getTranslations().size());


        assertEquals("in bellringing", senses.get(senses.size()-1).getNotes().get(0).getText());
        assertEquals("indicator", senses.get(senses.size()-1).getNotes().get(0).getType());

        assertEquals("Schlagtonfolge",senses.get(senses.size()-1).getTranslations().get(0).getText() );
    }

    private String readFromAssets() throws IOException {

        StringBuilder buf = new StringBuilder();
        InputStream json = this.getClass().getClassLoader().getResourceAsStream("example_real.json");

        BufferedReader in =
                new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str = in.readLine()) != null) {
            buf.append(str);
        }

        in.close();

        return buf.toString();

    }
}
