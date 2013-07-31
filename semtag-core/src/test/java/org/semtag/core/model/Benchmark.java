package org.semtag.core.model;

import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.junit.Ignore;
import org.junit.Test;
import org.semtag.concept.Concept;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;
import org.wikapidia.core.dao.LocalPageDao;
import org.wikapidia.core.model.LocalPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Benchmark {

    @Ignore
    @Test
    public void benchmarkTagAppRetrieval() throws ConfigurationException, DaoException {
        TagAppDao dao = new Configurator(new Configuration()).get(TagAppDao.class);
        long start = System.currentTimeMillis();
        for (int i=0; i<100; i++) {
            System.out.println("Starting #" + (i+1));
            Iterable<TagApp> tagApps = dao.get(new DaoFilter());
            TIntSet set = new TIntHashSet();
            for (TagApp t : tagApps) {
                set.add(t.getConceptId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/100.0);
    }

    @Ignore
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

    @Ignore
    @Test
    public void benchmarkConceptMostSimilar() throws ConfigurationException, DaoException {
        TagAppDao helperDao = new Configurator(new Configuration()).get(TagAppDao.class);
        ConceptDao dao = new Configurator(new Configuration()).get(ConceptDao.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            concepts.add(concept);
            i++;
            if (i==100) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        for (Concept c : concepts) {
            TIntSet set = new TIntHashSet();
            SimilarResultList list = c.getMostSimilar(10, helperDao);
            for (SimilarResult result : list) {
                set.add(result.getIntId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/100.0);
    }

    @Ignore
    @Test
    public void benchmarkTagAppMostSimilar() throws ConfigurationException, DaoException {
        TagAppDao dao = new Configurator(new Configuration()).get(TagAppDao.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        int i=1;
        while (tagApps.size() < 100) {
            TagApp tagApp = dao.getByTagAppId(i);
            if (tagApp.getConceptId() > -1) {
                tagApps.add(tagApp);
            }
            i++;
        }
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            TLongSet set = new TLongHashSet();
            SimilarResultList list = t.getMostSimilar(10, dao);
            for (SimilarResult result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/100.0);
    }
}
