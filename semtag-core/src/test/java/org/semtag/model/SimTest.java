package org.semtag.model;

import org.junit.Ignore;
import org.junit.Test;
import org.semtag.dao.ConceptDao;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.concept.Concept;
import org.semtag.sim.ConceptSimilarity;
import org.semtag.sim.SimilarResult;
import org.semtag.sim.SimilarResultList;
import org.semtag.sim.TagAppSimilarity;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class SimTest {

    @Ignore
    @Test
    public void testConceptSim() throws ConfigurationException, DaoException {
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
            if (i>=20) break;
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
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void testTagAppSim() throws ConfigurationException, DaoException {
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
            if (i>=10) break;
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
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
