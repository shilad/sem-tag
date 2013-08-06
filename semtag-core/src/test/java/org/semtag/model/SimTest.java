package org.semtag.model;

import org.junit.Test;
import org.semtag.dao.*;
import org.semtag.model.concept.Concept;
import org.semtag.sim.*;
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
public class SimTest {

//    @Ignore
    @Test
    public void testConceptSim() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        ConceptDao dao = conf.get(ConceptDao.class);
        ConceptSimilarity sim = conf.get(ConceptSimilarity.class);
        List<Concept> concepts = new ArrayList<Concept>();
        Iterable<Concept> iterable = dao.get(new DaoFilter());
        for (Concept concept : iterable) {
            if (concept.getConceptId() > -1) {
                concepts.add(concept);
            }
            if (concepts.size()>=20) break;
        }
        Concept x = concepts.get(0);
        for (Concept y : concepts) {
            System.out.println(x.getConceptObj() + " to " + y.getConceptObj() + " : " + sim.similarity(x, y));
            SimilarResultList list = sim.mostSimilar(y, 10);
            for (SimilarResult result : list) {
                System.out.println("Most sim to " + y.getConceptObj() + ": " + dao.getByConceptId(result.getIntId()).getConceptObj() + " - " + result.getValue());
            }
        }
        double[][] matrix = sim.cosimilarity(concepts.toArray(new Concept[concepts.size()]));
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

//    @Ignore
    @Test
    public void testTagAppSim() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        TagAppSimilarity sim = conf.get(TagAppSimilarity.class);
        List<TagApp> tagApps = new ArrayList<TagApp>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                tagApps.add(t);
            }
            if (tagApps.size()>=20) break;
        }
        TagApp x = tagApps.get(0);
        for (TagApp y : tagApps) {
            System.out.println(x.getTag() + " to " + y.getTag() + " : " + sim.similarity(x, y));
            SimilarResultList list = sim.mostSimilar(y, 10);
            for (SimilarResult result : list) {
                System.out.println("Most sim to " + y.getTag() + ": " + dao.getByTagAppId(result.getIntId()).getTag() + " - " + result.getValue());
            }
        }
        double[][] matrix = sim.cosimilarity(tagApps.toArray(new TagApp[tagApps.size()]));
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

//    @Ignore
    @Test
    public void testItemSim() throws ConfigurationException, DaoException {
        Configurator conf = new Configurator(new Configuration());
        TagAppDao dao = conf.get(TagAppDao.class);
        ItemDao itemDao = conf.get(ItemDao.class);
        ItemSimilarity sim = conf.get(ItemSimilarity.class);
        Set<Item> items = new HashSet<Item>();
        Iterable<TagApp> iterable = dao.get(new DaoFilter());
        for (TagApp t : iterable) {
            if (t.getConceptId() > -1) {
                items.add(t.getItem());
            }
            if (items.size()>=20) break;
        }
        Item x = items.iterator().next();
        for (Item y : items) {
            System.out.println(x.getItemId() + " to " + y.getItemId() + " : " + sim.similarity(x, y));
            SimilarResultList list = sim.mostSimilar(y, 10);
            for (SimilarResult result : list) {
                System.out.println("Most sim to " + y.getItemId() + ": " + itemDao.getByItemId(result.getStringId()).getItemId() + " - " + result.getValue());
            }
        }
        double[][] matrix = sim.cosimilarity(items.toArray(new Item[items.size()]));
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
