package org.semtag.sim;

import com.typesafe.config.Config;
import org.semtag.SemTagException;
import org.semtag.dao.DaoException;
import org.semtag.dao.DaoFilter;
import org.semtag.dao.TagDao;
import org.semtag.mapper.ConceptMapper;
import org.semtag.model.Tag;
import org.semtag.model.concept.Concept;
import org.wikapidia.conf.Configuration;
import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.conf.Configurator;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Similar methods for TagApps.
 *
 * @author Ari Weiland, Shilad Sen
 */
public class TagSimilar implements Similar<Tag> {

    private final TagDao dao;
    private final ConceptSimilar sim;
    private final ConceptMapper mapper;

    public TagSimilar(TagDao dao, ConceptSimilar sim, ConceptMapper mapper) {
        this.mapper = mapper;
        this.dao = dao;
        this.sim = sim;
    }

    @Override
    public double similarity(Tag x, Tag y) throws DaoException {
        try {
            mapIfNecessary(x);
            mapIfNecessary(y);
        } catch (SemTagException e) {
            throw new DaoException(e);
        }
        if (!x.hasConcept() || !y.hasConcept()) {
            return Double.NaN;
        }
        return sim.similarity(x.getConcept(), y.getConcept());
    }

    private void mapIfNecessary(Tag tag) throws SemTagException {
        if (!tag.hasConcept()) {
            mapper.map(tag);
        }
    }

    /**
     * Returns a list of the most similar raw tags to this tag.
     * Note that this method is 3-6 times slower than the mostSimilar(TagApp),
     * and you should try to use that one when possible.
     * @param tag
     * @param maxResults
     * @return
     * @throws org.semtag.dao.DaoException
     */
    public SimilarResultList<Tag> mostSimilar(Tag tag, int maxResults) throws DaoException {
        return mostSimilar(tag, maxResults, 0);
    }

    /**
     * Returns a list of the most similar raw tags to this tag that pass the threshold.
     * Note that this method is 3-6 times slower than the mostSimilar(TagApp),
     * and you should try to use that one when possible.
     * @param tag
     * @param maxResults
     * @return
     * @throws org.semtag.dao.DaoException
     */
    public SimilarResultList<Tag> mostSimilar(Tag tag, int maxResults, double threshold) throws DaoException {
        try {
            mapIfNecessary(tag);
        } catch (SemTagException e) {
            throw new DaoException(e);
        }
        if (!tag.hasConcept()) {
            return null;
        }

        // build map of concept to similarity score
        Map<Concept, Double> conceptScores = new HashMap<Concept, Double>();
        for (SimilarResult<Concept> sr : sim.mostSimilar(tag.getConcept(), maxResults, threshold)) {
            conceptScores.put(sr.getObj(), sr.getValue());
        }

        // get tags with those concepts and add them to the results list
        SimilarResultList<Tag> results = new SimilarResultList<Tag>(maxResults, threshold);
        for (Tag t : dao.get(new DaoFilter().setConcepts(conceptScores.keySet()))) {
            if (!conceptScores.containsKey(t.getConcept())) {
                throw new IllegalStateException();
            }
            Double score = conceptScores.get(t.getConcept());
            if (score == null) throw new IllegalStateException();
            SimilarResult<Tag> sr = new SimilarResult<Tag>(t.getTagId(), t, score);
            results.add(sr);
        }
        results.lock();
        return results;
    }

    @Override
    public double[][] cosimilarity(Tag[] objs) throws DaoException {
        return sim.cosimilarity(getVector(objs));
    }

    @Override
    public double[][] cosimilarity(Tag[] xObjs, Tag[] yObjs) throws DaoException {
        return sim.cosimilarity(getVector(xObjs), getVector(yObjs));
    }

    private Concept[] getVector(Tag[] objs) {
        Concept[] ids = new Concept[objs.length];
        for (int i=0; i<objs.length; i++) {
            ids[i] = objs[i].getConcept();
        }
        return ids;
    }

    public static class Provider extends org.wikapidia.conf.Provider<TagSimilar> {
        public Provider(Configurator configurator, Configuration config) throws ConfigurationException {
            super(configurator, config);
        }

        @Override
        public Class getType() {
            return TagSimilar.class;
        }

        @Override
        public String getPath() {
            return "sem-tag.sim.tag";
        }

        @Override
        public TagSimilar get(String name, Config config, Map<String, String> runtimeParams) throws ConfigurationException {
            if (!config.getString("type").equals("tag")) {
                return null;
            }
            return new TagSimilar(
                    getConfigurator().get(TagDao.class, config.getString("tagDao")),
                    getConfigurator().get(ConceptSimilar.class, config.getString("conceptSim")),
                    getConfigurator().get(ConceptMapper.class, config.getString("conceptMapper"))
            );
        }
    }
}
