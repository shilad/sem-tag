package org.semtag.sim;

import com.typesafe.config.Config;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagAppDao;
import org.semtag.model.TagApp;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Similar methods for TagApps.
 *
 * @author Ari Weiland
 */
public class TagAppSimilar implements Similar<TagApp> {

    private final TagAppDao dao;
    private final ConceptSimilar sim;

    public TagAppSimilar(TagAppDao dao, ConceptSimilar sim) {
        this.dao = dao;
        this.sim = sim;
    }

    @Override
    public double similarity(TagApp x, TagApp y) throws DaoException {
        return sim.similarity(x.getConcept(), y.getConcept());
    }

    @Override
    public SimilarResultList<TagApp> mostSimilar(TagApp obj, int maxResults) throws DaoException {
        return mostSimilar(obj, maxResults, 0);
    }

    @Override
    public SimilarResultList<TagApp> mostSimilar(TagApp obj, int maxResults, double threshold) throws DaoException {
        long l1 = System.currentTimeMillis();
        SimilarResultList<Concept> concepts = sim.mostSimilar(obj.getConcept(), maxResults);

        long l2 = System.currentTimeMillis();
        Map<String, TagApp> tags = new HashMap<String, TagApp>();
        for (TagApp t : dao.get(new DaoFilter().setConcepts(concepts.getObjects()))) {
            tags.put(t.getTag().getNormalizedTag(), t);
        }
        SimilarResultList<TagApp> list = new SimilarResultList<TagApp>(maxResults, threshold);
        for (TagApp t : tags.values()) {
            list.add(new SimilarResult<TagApp>(t.getTagAppId(), t, concepts.getValue(t.getConcept())));
        }
        list.lock();
        long l3 = System.currentTimeMillis();
        System.err.println("elapsed time are " + (l2 - l1) + " and " + (l3 - l2));
        return list;
    }

    @Override
    public double[][] cosimilarity(TagApp[] objs) throws DaoException {
        return sim.cosimilarity(getVector(objs));
    }

    @Override
    public double[][] cosimilarity(TagApp[] xObjs, TagApp[] yObjs) throws DaoException {
        return sim.cosimilarity(getVector(xObjs), getVector(yObjs));
    }

    private Concept[] getVector(TagApp[] objs) {
        Concept[] concepts = new Concept[objs.length];
        for (int i=0; i<objs.length; i++) {
            concepts[i] = objs[i].getConcept();
        }
        return concepts;
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagAppSimilar> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagAppSimilar.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.sim.tagApp";
        }

        @Override
        public TagAppSimilar get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("tagApp")) {
                return null;
            }
            return new TagAppSimilar(
                    getConfigurator().get(TagAppDao.class, config.getString("tagAppDao")),
                    getConfigurator().get(ConceptSimilar.class, config.getString("conceptSim"))
            );
        }
    }
}
