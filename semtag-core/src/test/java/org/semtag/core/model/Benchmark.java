package org.semtag.core.model;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.junit.Ignore;
import org.junit.Test;
import org.semtag.core.dao.ConceptDao;
import org.semtag.core.dao.DaoException;
import org.semtag.core.dao.DaoFilter;
import org.semtag.core.dao.TagAppDao;
import org.semtag.core.model.concept.Concept;
import org.semtag.core.sim.*;
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

    public static final double SIZE = 1;

    @Ignore
    @Test
    public void benchmarkTagAppRetrieval() throws ConfigurationException, DaoException {
        TagAppDao dao = new Configurator(new Configuration()).get(TagAppDao.class);
        long start = System.currentTimeMillis();
        for (int i=0; i<SIZE; i++) {
            System.out.println("Starting #" + (i+1));
            Iterable<TagApp> tagApps = dao.get(new DaoFilter());
            TIntSet set = new TIntHashSet();
            for (TagApp t : tagApps) {
                set.add(t.getConceptId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkLocalPageRetrieval() throws ConfigurationException, org.wikapidia.core.dao.DaoException {
        LocalPageDao dao = new Configurator(new Configuration()).get(LocalPageDao.class);
        long start = System.currentTimeMillis();
        for (int i=0; i<SIZE; i++) {
            System.out.println("Starting #" + (i+1));
            Iterable<LocalPage> concepts = dao.get(new org.wikapidia.core.dao.DaoFilter().setRedirect(false));
            TIntSet set = new TIntHashSet();
            for (LocalPage concept : concepts) {
                set.add(concept.getLocalId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);

    }

    @Ignore
    @Test
    public void benchmarkConceptMostSimilar() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptSimilarity sim = conf.get(ConceptSimilarity.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i==SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        for (Concept c : concepts) {
            TIntSet set = new TIntHashSet();
            SimilarResultList list = sim.mostSimilar(c, 10);
            for (SimilarResult result : list) {
                set.add(result.getIntId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkTagAppMostSimilar() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppSimilarity sim = conf.get(TagAppSimilarity.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp tagApp : iterable) {
            if (tagApp.getConceptId() > -1) {
                tagApps.add(tagApp);
                i++;
            }
            if (i==SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            TLongSet set = new TLongHashSet();
            SimilarResultList list = sim.mostSimilar(t, 10);
            for (SimilarResult result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

//    @Ignore
    @Test
    public void benchmarkItemMostSimilar() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemSimilarity sim = conf.get(ItemSimilarity.class);
        List<Item> items = new ArrayList<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
                i++;
            }
            if (i==SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        for (Item item : items) {
            TLongSet set = new TLongHashSet();
            SimilarResultList list = sim.mostSimilar(item, 10);
            for (SimilarResult result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkConceptSimilarity() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptSimilarity sim = conf.get(ConceptSimilarity.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i==SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        TDoubleList list = new TDoubleArrayList();
        Concept x = concepts.get(0);
        for (Concept y : concepts) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkTagAppSimilarity() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppSimilarity sim = conf.get(TagAppSimilarity.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp tagApp : iterable) {
            if (tagApp.getConceptId() > -1) {
                tagApps.add(tagApp);
                i++;
            }
            if (i >= SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        TDoubleList list = new TDoubleArrayList();
        TagApp x = tagApps.get(0);
        for (TagApp y : tagApps) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkItemSimilarity() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemSimilarity sim = conf.get(ItemSimilarity.class);
        List<Item> items = new ArrayList<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
                i++;
            }
            if (i==SIZE) break;
        }
        System.out.println("Start");
        long start = System.currentTimeMillis();
        TDoubleList list = new TDoubleArrayList();
        Item x = items.get(0);
        for (Item y : items) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }
}
