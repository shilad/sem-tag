package org.semtag.model;

import org.junit.Test;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.model.concept.Concept;
import org.semtag.sim.ConceptSimilarity;
import org.semtag.sim.SimilarResult;
import org.semtag.sim.SimilarResultList;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.lang.Language;
import org.wikapidia.sr.LocalSRMetric;

import java.util.ArrayList;
import java.util.List;

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
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i>=10) break;
        }
        Concept x = concepts.get(0);
        for (Concept y : concepts) {
            System.out.println(x.getConceptObj() + " to " + y.getConceptObj() + " : " + sim.similarity(x, y));
            SimilarResultList list = sim.mostSimilar(y, 10);
            for (SimilarResult result : list) {
                System.out.println("Most sim to " + y.getConceptObj() + ": " + dao.getByConceptId(result.getIntId()).getConceptObj() + " - " + result.getValue());
            }
        }
    }
}
