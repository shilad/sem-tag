package org.semtag.model;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.junit.Test;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.concept.Concept;
import org.semtag.sim.*;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Benchmark {

    public static final double SIZE = 1000;

//    @Ignore
    @Test
    public void benchmarkConceptSimilarity()    throws ConfigurationException, DaoException {
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
        TDoubleList list = new TDoubleArrayList();
        Concept x = concepts.get(0);
        System.out.println("Start ConceptSimilarity");
        long start = System.currentTimeMillis();
        for (Concept y : concepts) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

//    @Ignore
    @Test
    public void benchmarkTagAppSimilarity()     throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppSimilarity sim = conf.get(TagAppSimilarity.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                tagApps.add(t);
                i++;
            }
            if (i >= SIZE) break;
        }
        TDoubleList list = new TDoubleArrayList();
        TagApp x = tagApps.get(0);
        System.out.println("Start TagAppSimilarity");
        long start = System.currentTimeMillis();
        for (TagApp y : tagApps) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

//    @Ignore
    @Test
    public void benchmarkItemSimilarity()       throws ConfigurationException, DaoException {
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
        System.out.println("Start ItemSimilarity");
        TDoubleList list = new TDoubleArrayList();
        Item x = items.get(0);
        long start = System.currentTimeMillis();
        for (Item y : items) {
            list.add(sim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

//    @Ignore
    @Test
    public void benchmarkConceptMostSimilar()   throws ConfigurationException, DaoException {
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
        System.out.println("Start ConceptMostSimilar");
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

//    @Ignore
    @Test
    public void benchmarkTagAppMostSimilar()    throws ConfigurationException, DaoException {
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
        System.out.println("Start TagAppMostSimilar");
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
    public void benchmarkItemMostSimilar()      throws ConfigurationException, DaoException {
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
        System.out.println("Start ItemMostSimilar");
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
}
