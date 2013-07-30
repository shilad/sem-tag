package org.semtag.core.model;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.junit.Test;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.LocalPageDao;
import org.wikapidia.core.model.LocalPage;

/**
 * @author Ari Weiland
 */
public class Benchmark {

    @Test
    public void benchmarkConceptRetrieval() throws ConfigurationException, DaoException {
        ConceptDao dao = new Configurator(new Configuration()).get(ConceptDao.class);
        long start = System.currentTimeMillis();
        for (int i=0; i<100; i++) {
            System.out.println("Starting #" + (i+1));
            Iterable<Concept> concepts = dao.get(new DaoFilter());
            TIntSet set = new TIntHashSet();
            for (Concept concept : concepts) {
                set.add(concept.getConceptId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/100.0);
    }

    @Test
    public void benchmarkLocalPageRetrieval() throws ConfigurationException, org.wikapidia.core.dao.DaoException {
        LocalPageDao dao = new Configurator(new Configuration()).get(LocalPageDao.class);
        long start = System.currentTimeMillis();
        for (int i=0; i<10; i++) {
            System.out.println("Starting #" + (i+1));
            Iterable<LocalPage> concepts = dao.get(new org.wikapidia.core.dao.DaoFilter().setRedirect(false));
            TIntSet set = new TIntHashSet();
            for (LocalPage concept : concepts) {
                set.add(concept.getLocalId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/10.0);

    }
}
