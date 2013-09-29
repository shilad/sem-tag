package org.semtag.cookbook;

import org.semtag.dao.*;
import org.semtag.model.Item;
import org.semtag.model.Tag;
import org.semtag.model.TagApp;
import org.semtag.model.concept.Concept;
import org.semtag.sim.*;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Test the various similarity methods for the Similar classes and
 * prints out the results. Calculates similarity and most similar
 * SIZE times and builds a SIZE x SIZE cosimilarity matrix.
 *
 * @author Ari Weiland
 */
public class SimTest {

    public static final int SIZE = 20;

    private final ConceptSimilar cSim;
    private final TagAppSimilar tSim;
    private final ItemSimilar iSim;
    private final TagAppDao tDao;

    private final Set<Concept> concepts;
    private final Set<TagApp> tagApps;
    private final Set<Item> items;

    public SimTest() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        cSim = conf.get(ConceptSimilar.class);
        tSim = conf.get(TagAppSimilar.class);
        iSim = conf.get(ItemSimilar.class);
        tDao = conf.get(TagAppDao.class);
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
        Iterator<TagApp> tIterator = tDao.get(new DaoFilter()).iterator();
        while (tagApps.size() < SIZE && tIterator.hasNext()) {
            TagApp t = tIterator.next();
            if (t.getConcept() != null) {
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

    public static void main(String[] args) throws ConfigurationException, DaoException {
        SimTest simTest = new SimTest();
        simTest.testConceptSim();
        simTest.testTagAppSim();
        simTest.testItemSim();
    }

    public void testConceptSim() throws ConfigurationException, DaoException {
        Concept x = concepts.iterator().next();
        for (Concept y : concepts) {
            System.out.println(x + " to " + y + " : " + cSim.similarity(x, y));
            SimilarResultList<Concept> list = cSim.mostSimilar(y, 10);
            for (SimilarResult<Concept> result : list) {
                System.out.println("Most sim to " + y.getConceptId() + ": " + result.getIntId() + " - " + result.getValue());
            }
        }
        double[][] matrix = cSim.cosimilarity(concepts.toArray(new Concept[concepts.size()]));
        for (Concept c : concepts) {
            System.out.print(c.getConceptId() + " \t");
        }
        System.out.println();
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%.4f\t", value);
            }
            System.out.println();
        }
    }

    public void testTagAppSim()  throws ConfigurationException, DaoException {
        TagApp x = tagApps.iterator().next();
        for (TagApp y : tagApps) {
            System.out.println(x.getTag() + " to " + y.getTag() + " : " + tSim.similarity(x, y));
            SimilarResultList<TagApp> list = tSim.mostSimilar(y, 10);
            for (SimilarResult<TagApp> result : list) {
                System.out.println("Most sim to " + y.getTag() + ": " + tDao.getByTagAppId(result.getIntId()).getTag() + " - " + result.getValue());
            }
        }
        double[][] matrix = tSim.cosimilarity(tagApps.toArray(new TagApp[tagApps.size()]));
        for (TagApp t : tagApps) {
            System.out.print(t.getTagAppId() + "   \t");
        }
        System.out.println();
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%.4f\t", value);
            }
            System.out.println();
        }
    }

    public void testItemSim()    throws ConfigurationException, DaoException {
        Item x = items.iterator().next();
        for (Item y : items) {
            System.out.println(x.getItemId() + " to " + y.getItemId() + " : " + iSim.similarity(x, y));
            SimilarResultList<Item> list = iSim.mostSimilar(y, 10);
            for (SimilarResult<Item> result : list) {
                System.out.println("Most sim to " + y.getItemId() + ": " + result.getStringId() + " - " + result.getValue());
            }
        }
        double[][] matrix = iSim.cosimilarity(items.toArray(new Item[items.size()]));
        for (Item item : items) {
            System.out.print(item.getItemId() + "  \t");
        }
        System.out.println();
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%.4f\t", value);
            }
            System.out.println();
        }
    }
}
