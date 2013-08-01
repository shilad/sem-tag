package org.semtag.model;

import org.junit.Test;
import org.semtag.model.concept.Concept;
import org.semtag.model.concept.WikapidiaConcept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;

/**
 * @author Ari Weiland
 */
public class TestConcept {

    @Test
    public void test() throws ConfigurationException {
        Configurator  configurator = new Configurator(new Configuration());
        LocalId localId = new LocalId(Language.getByLangCode("en"), 1);
        Concept concept = new WikapidiaConcept(localId, configurator.get(LocalSRMetric.class));
        Concept<LocalId> newConcept = new WikapidiaConcept(
                concept.getConceptId(),
                configurator.get(LocalSRMetric.class, concept.getMetric()),
                concept.conceptObjToBytes()
        );
        System.out.println(newConcept.getConceptObj().asLocalPage());
    }
}
