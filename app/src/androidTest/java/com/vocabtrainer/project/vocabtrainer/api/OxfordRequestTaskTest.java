package com.vocabtrainer.project.vocabtrainer.api;

import com.vocabtrainer.project.vocabtrainer.api.model.OxfordEntry;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by steffy on 17/12/2017.
 */

public class OxfordRequestTaskTest {

    @Test
    public void testSimpleRequest() throws ExecutionException, InterruptedException {
        OxfordRequestTask task = new OxfordRequestTask(null);

        task.execute(new String[]{"en", "change"});
        OxfordEntry entry = task.get();

        assertTrue(entry != null);
        assertEquals("change", entry.getResults().get(0).getId());
        assertEquals("en", entry.getResults().get(0).getLanguage());

        assertEquals(2, entry.getResults().get(0).getLexicalEntries().size());
    }
}
