package org.semtag.cookbook;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TIntHashSet;
import gnu.trove.set.hash.TLongHashSet;
import org.semtag.dao.*;
import org.semtag.model.Item;
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

    public static final double SIZE = 100;
    public static final int MAX_RESULTS = 500;

    private final ConceptSimilar cSim;
    private final TagAppSimilar tSim;
    private final ItemSimilar iSim;

    private final Set<Concept> concepts;
    private final Set<TagApp> tagApps;
    private final Set<Item> items;

    public Benchmark() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        cSim = conf.get(ConceptSimilar.class);
        tSim = conf.get(TagAppSimilar.class);
        iSim = conf.get(ItemSimilar.class);
        concepts = new HashSet<Concept>();
        tagApps = new HashSet<TagApp>();
        items = new HashSet<Item>();
        // build concept set
        ConceptDao cDao = conf.get(ConceptDao.class);
        Iterator<Concept> cIterator = cDao.get(new DaoFilter()).iterator();
        while (concepts.size() < SIZE && cIterator.hasNext()) {
            Concept concept = cIterator.next();
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
            }
        }
        // build TagApp set
        TagAppDao tDao = conf.get(TagAppDao.class);
        Iterator<TagApp> tIterator = tDao.get(new DaoFilter()).iterator();
        while (tagApps.size() < SIZE && tIterator.hasNext()) {
            TagApp t = tIterator.next();
            if (t.getConceptId() > -1) {
                tagApps.add(t);
            }
        }
        // build item set
        ItemDao iDao = conf.get(ItemDao.class);
        Iterator<Item> iIterator = iDao.get(new DaoFilter()).iterator();
        while (items.size() < SIZE && iIterator.hasNext()) {
            items.add(iIterator.next());
        }
    }

    public static void main(String[] args)     throws ConfigurationException, DaoException {
        Benchmark benchmark = new Benchmark();

        benchmark.benchmarkConceptSimilarity();
        benchmark.benchmarkTagAppSimilarity();
        benchmark.benchmarkItemSimilarity();

        benchmark.benchmarkConceptMostSimilar();
        benchmark.benchmarkTagAppMostSimilar();
        benchmark.benchmarkItemMostSimilar();

        benchmark.benchmarkConceptCosimilarity();
        benchmark.benchmarkTagAppCosimilarity();
        benchmark.benchmarkItemCosimilarity();

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
            list.add(tSim.similarity(x, y));
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
            SimilarResultList list = cSim.mostSimilar(c, MAX_RESULTS);
            for (SimilarResult result : list) {
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
            SimilarResultList list = tSim.mostSimilar(t, MAX_RESULTS);
            for (SimilarResult result : list) {
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
            SimilarResultList list = tSim.mostSimilar(t.getTag(), MAX_RESULTS);
            for (SimilarResult result : list) {
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
            SimilarResultList tagAppIds = tSim.mostSimilar(t, MAX_RESULTS);
            List<SimilarResult> result = new ArrayList<SimilarResult>();
            for (SimilarResult sr : tagAppIds) {
                TagApp app = (TagApp) sr.getObj();
                String tag = app.getTag().getRawTag();
                if (tag != null) {
                    result.add(new SimilarResult(tag, sr.getValue()));
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Ellapsed time: " + (end-start));
        System.out.println("Unit time: " + (end-start)/SIZE);
    }

    public void benchmarkItemMostSimilar()     throws ConfigurationException, DaoException {
        System.out.println("Start ItemMostSimilar");
        long start = System.currentTimeMillis();
        for (Item item : items) {
            TLongSet set = new TLongHashSet();
            SimilarResultList list = iSim.mostSimilar(item, MAX_RESULTS);
            for (SimilarResult result : list) {
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
        double[][] matrix = tSim.cosimilarity(tagApps.toArray(new TagApp[tagApps.size()]));
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
}
