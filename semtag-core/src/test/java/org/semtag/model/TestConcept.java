package org.semtag.model;

import org.junit.Test;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;

import static org.junit.Assert.assertEquals;

/**
 * @author Ari Weiland
 */
public class TestConcept {

    @Test
    public void test() throws ConfigurationException {
        LocalId localId = new LocalId(Language.getByLangCode("sw"), 1324234);
        Concept concept = new Concept(localId);
        Concept concept2 = new Concept(concept.getConceptId());
        assertEquals(concept.getConceptId(), concept2.getConceptId());
        assertEquals(concept.getLocalId(), concept2.getLocalId());
        assertEquals(concept.getLocalId(), localId);
    }
}
