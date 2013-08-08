package org.semtag.model;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.junit.Ignore;
import org.junit.Test;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.concept.Concept;
import org.semtag.relate.*;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class Benchmark {

    public static final double SIZE = 100;
    public static final int MAX_RESULTS = 500;

    @Ignore
    @Test
    public void benchmarkConceptSimilarity()    throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptRelator sim = conf.get(ConceptRelator.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i>=SIZE) break;
        }
        TDoubleList list = new TDoubleArrayList();
        Concept x = concepts.get(0);
        System.out.println("Start ConceptSimilarity");
        long start = System.currentTimeMillis();
        for (Concept y : concepts) {
            list.add(sim.relatedness(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkTagAppSimilarity()     throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppRelator sim = conf.get(TagAppRelator.class);
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
            list.add(sim.relatedness(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkItemSimilarity()       throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemRelator sim = conf.get(ItemRelator.class);
        List<Item> items = new ArrayList<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start ItemSimilarity");
        TDoubleList list = new TDoubleArrayList();
        Item x = items.get(0);
        long start = System.currentTimeMillis();
        for (Item y : items) {
            list.add(sim.relatedness(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkConceptMostSimilar()   throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptRelator sim = conf.get(ConceptRelator.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start ConceptMostSimilar");
        long start = System.currentTimeMillis();
        for (Concept c : concepts) {
            TIntSet set = new TIntHashSet();
            RelatedResultList list = sim.mostRelated(c, MAX_RESULTS);
            for (RelatedResult result : list) {
                set.add(result.getIntId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkTagAppMostSimilar()    throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppRelator sim = conf.get(TagAppRelator.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp tagApp : iterable) {
            if (tagApp.getConceptId() > -1) {
                tagApps.add(tagApp);
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start TagAppMostSimilar");
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            TLongSet set = new TLongHashSet();
            RelatedResultList list = sim.mostRelated(t, MAX_RESULTS);
            for (RelatedResult result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
        System.out.println("Start TagMostSimilar");
        start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            Set<String> set = new HashSet<String>();
            RelatedResultList list = sim.mostRelated(t.getTag(), MAX_RESULTS);
            for (RelatedResult result : list) {
                set.add(result.getStringId());
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

//    @Ignore
    @Test
    public void benchmarkMacademia()    throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppRelator sim = conf.get(TagAppRelator.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp tagApp : iterable) {
            if (tagApp.getConceptId() > -1) {
                tagApps.add(tagApp);
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start Macademia");
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            RelatedResultList tagAppIds = sim.mostRelated(t, MAX_RESULTS);
            List<RelatedResult> result = new ArrayList<RelatedResult>();
            for (RelatedResult sr : tagAppIds) {
                TagApp app = dao.getByTagAppId(sr.getLongId());
                String tag = app.getTag().getRawTag();
                if (tag == null) {
                } else {
                    result.add(new RelatedResult(tag, sr.getValue()));
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);

        System.out.println("Start SimpleMacademia");
        start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            RelatedResultList tagAppIds = sim.mostRelated(t, MAX_RESULTS);
            List<RelatedResult> result = new ArrayList<RelatedResult>();
            for (RelatedResult sr : tagAppIds) {
                TagApp app = (TagApp) sr.getObj();
                String tag = app.getTag().getRawTag();
                if (tag == null) {
                } else {
                    result.add(new RelatedResult(tag, sr.getValue()));
                }
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkItemMostSimilar()      throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemRelator sim = conf.get(ItemRelator.class);
        List<Item> items = new ArrayList<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start ItemMostSimilar");
        long start = System.currentTimeMillis();
        for (Item item : items) {
            TLongSet set = new TLongHashSet();
            RelatedResultList list = sim.mostRelated(item, MAX_RESULTS);
            for (RelatedResult result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    @Ignore
    @Test
    public void benchmarkConceptCosimilarity()  throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptRelator sim = conf.get(ConceptRelator.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        int i=0;
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start ConceptCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = sim.corelatedness(concepts.toArray(new Concept[concepts.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }

    @Ignore
    @Test
    public void benchmarkTagAppCosimilarity()   throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppRelator sim = conf.get(TagAppRelator.class);
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
        System.out.println("Start TagAppCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = sim.corelatedness(tagApps.toArray(new TagApp[tagApps.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }

    @Ignore
    @Test
    public void benchmarkItemCosimilarity()     throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemRelator sim = conf.get(ItemRelator.class);
        List<Item> items = new ArrayList<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        int i=0;
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
                i++;
            }
            if (i>=SIZE) break;
        }
        System.out.println("Start ItemCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = sim.corelatedness(items.toArray(new Item[items.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }
}
