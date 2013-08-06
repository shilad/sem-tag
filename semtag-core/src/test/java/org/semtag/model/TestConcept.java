package org.semtag.model;

import org.junit.Test;
import org.semtag.model.concept.Concept;
import org.semtag.model.concept.WikapidiaConcept;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;

/**
 * @author Ari Weiland
 */
public class TestConcept {

    @Test
    public void test() throws ConfigurationException {
        LocalId localId = new LocalId(Language.getByLangCode("en"), 1);
        Concept concept = new WikapidiaConcept(localId, "wikapidia");
        Concept<LocalId> newConcept = new WikapidiaConcept(
                concept.getConceptId(),
                concept.getType(),
                concept.conceptObjToBytes());
        System.out.println(newConcept.getConceptObj().asLocalPage());
    }
}
