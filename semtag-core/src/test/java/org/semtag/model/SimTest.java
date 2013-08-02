package org.semtag.model;

import org.junit.Test;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.model.concept.WikapidiaConcept;
import org.semtag.sim.ConceptSimilarity;
import org.semtag.sim.SimilarResult;
import org.semtag.sim.SimilarResultList;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.lang.LocalId;
import org.wikapidia.sr.LocalSRMetric;

/**
 * @author Ari Weiland
 */
public class SimTest {

    public static final Language LANGUAGE = Language.getByLangCode("simple");

    @Test
    public void testConceptSim() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptSimilarity sim = conf.get(ConceptSimilarity.class);
        LocalSRMetric metric = conf.get(LocalSRMetric.class);
        WikapidiaConcept c1 = new WikapidiaConcept(new LocalId(LANGUAGE, 223430), metric); // Barack Obama
        WikapidiaConcept c2 = new WikapidiaConcept(new LocalId(LANGUAGE, 219587), metric); // United States
        System.out.println("Similarity: " + sim.similarity(c1, c2));
        SimilarResultList list = sim.mostSimilar(c1, 10);
        for (SimilarResult result : list) {
            System.out.println(dao.getByConceptId(result.getIntId()) + " : " + result.getValue());
        }
    }
}
