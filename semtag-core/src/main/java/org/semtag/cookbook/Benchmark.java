package org.semtag.cookbook;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.semtag.dao.*;
import org.semtag.model.Item;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.semtag.model.concept.Concept;
import org.semtag.sim.*;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.*;

/**
 * Benchmarks the speed of the core methods of Similar classes at
 * the specified iteration count (SIZE) and finding the specified
 * max results (MAX_RESULTS) for most similar methods. For
 * cosimilarity, it builds one symmetric SIZE x SIZE matrix.
 *
 * @author Ari Weiland
 */
public class Benchmark {

    public static final int SIZE = 1000;
    public static final int MAX_RESULTS = 500;

    private final ConceptSimilar cSim;
    private final TagAppSimilar taSim;
    private final TagSimilar tSim;
    private final ItemSimilar iSim;

    private final Set<Concept> concepts;
    private final Set<TagApp> tagApps;
    private final Set<Item> items;

    public Benchmark() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        cSim = conf.get(ConceptSimilar.class);
        taSim = conf.get(TagAppSimilar.class);
        iSim = conf.get(ItemSimilar.class);
        tSim = conf.get(TagSimilar.class);

        // build concept set
        ConceptDao cDao = conf.get(ConceptDao.class);
        concepts = sample(cDao.get(new DaoFilter()));

        // build TagApp set
        TagAppDao tDao = conf.get(TagAppDao.class);
        tagApps = sample(tDao.get(new DaoFilter()));

        // build item set
        ItemDao iDao = conf.get(ItemDao.class);
        items = sample(iDao.get(new DaoFilter()));
    }


    public static void main(String[] args)     throws ConfigurationException, DaoException {
        Benchmark benchmark = new Benchmark();
//
//        benchmark.benchmarkConceptSimilarity();
//        benchmark.benchmarkTagAppSimilarity();
//        benchmark.benchmarkItemSimilarity();
//
//        benchmark.benchmarkConceptMostSimilar();
//        benchmark.benchmarkTagAppMostSimilar();
//        benchmark.benchmarkItemMostSimilar();
//
//        benchmark.benchmarkConceptCosimilarity();
//        benchmark.benchmarkTagAppCosimilarity();
//        benchmark.benchmarkItemCosimilarity();

        benchmark.benchmarkMacademia();
    }

    public void benchmarkConceptSimilarity()   throws ConfigurationException, DaoException {
        TDoubleList list = new TDoubleArrayList();
        Concept x = concepts.iterator().next();
        System.out.println("Start ConceptSimilarity");
        long start = System.currentTimeMillis();
        for (Concept y : concepts) {
            list.add(cSim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkTagAppSimilarity()    throws ConfigurationException, DaoException {
        TDoubleList list = new TDoubleArrayList();
        TagApp x = tagApps.iterator().next();
        System.out.println("Start TagAppSimilarity");
        long start = System.currentTimeMillis();
        for (TagApp y : tagApps) {
            list.add(taSim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkItemSimilarity()      throws ConfigurationException, DaoException {
        System.out.println("Start ItemSimilarity");
        TDoubleList list = new TDoubleArrayList();
        Item x = items.iterator().next();
        long start = System.currentTimeMillis();
        for (Item y : items) {
            list.add(iSim.similarity(x, y));
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkConceptMostSimilar()  throws ConfigurationException, DaoException {
        System.out.println("Start ConceptMostSimilar");
        long start = System.currentTimeMillis();
        for (Concept c : concepts) {
            TIntSet set = new TIntHashSet();
            SimilarResultList<Concept> list = cSim.mostSimilar(c, MAX_RESULTS);
            for (SimilarResult<Concept> result : list) {
                set.add(result.getIntId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkTagAppMostSimilar()   throws ConfigurationException, DaoException {
        System.out.println("Start TagAppMostSimilar");
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            TLongSet set = new TLongHashSet();
            SimilarResultList<TagApp> list = taSim.mostSimilar(t, MAX_RESULTS);
            for (SimilarResult<TagApp> result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
        System.out.println("Start TagMostSimilar");
        start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            TLongSet set = new TLongHashSet();
            SimilarResultList<TagApp> list = taSim.mostSimilar(t, MAX_RESULTS);
            for (SimilarResult<TagApp> result : list) {
                set.add(result.getLongId());
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkMacademia()           throws ConfigurationException, DaoException {
        System.out.println("Start Macademia");
        long start = System.currentTimeMillis();
        for (TagApp t : tagApps) {
            SimilarResultList<Tag> tagAppIds = tSim.mostSimilar(t.getTag(), MAX_RESULTS);
            List<SimilarResult<Tag>> result = new ArrayList<SimilarResult<Tag>>();
            System.out.println("similar tags for " + t.getTag().getRawTag() + " are " + tagAppIds.size());
            for (SimilarResult<Tag> sr : tagAppIds) {
                Tag app = (Tag) sr.getObj();
                String tag = app.getRawTag();
                if (tag != null) {
//                    if (result.size() < 50) {
//                        System.out.println("\t" + tag);
//                    }
                    result.add(new SimilarResult<Tag>(tag, sr.getValue()));
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/tagApps.size());
    }

    public void benchmarkItemMostSimilar()     throws ConfigurationException, DaoException {
        System.out.println("Start ItemMostSimilar");
        long start = System.currentTimeMillis();
        for (Item item : items) {
            TLongSet set = new TLongHashSet();
            SimilarResultList<Item> list = iSim.mostSimilar(item, MAX_RESULTS);
            for (SimilarResult<Item> result : list) {
                set.add(result.getLongId());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkConceptCosimilarity() throws ConfigurationException, DaoException {
        System.out.println("Start ConceptCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = cSim.cosimilarity(concepts.toArray(new Concept[concepts.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }

    public void benchmarkTagAppCosimilarity()  throws ConfigurationException, DaoException {
        System.out.println("Start TagAppCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = taSim.cosimilarity(tagApps.toArray(new TagApp[tagApps.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }

    public void benchmarkItemCosimilarity()    throws ConfigurationException, DaoException {
        System.out.println("Start ItemCosimilarity");
        long start = System.currentTimeMillis();
        double[][] matrix = iSim.cosimilarity(items.toArray(new Item[items.size()]));
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("for a " + SIZE + "x" + SIZE + " matrix");
    }

    private static  <T> Set<T> sample(Iterable<T> iter) {
        Set<T> sample = new HashSet<T>();

        for (T t : iter) {
            if (t instanceof Concept) {
                if (t != null && ((Concept)t).getConceptId() > -1) {
                    sample.add(t);
                }
            } else if (t instanceof TagApp) {
                if (((TagApp)t).getConcept() != null) {
                    sample.add(t);
                }
            } else {
                sample.add(t);
            }
        }
        if (sample.size() > SIZE) {
            List<T> sl = new ArrayList<T>(sample);
            Collections.shuffle(sl);
            sample.clear();
            sample.addAll(sl.subList(0, SIZE));
        }
        return sample;
    }
}
